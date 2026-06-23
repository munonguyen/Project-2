package com.devon.building.converter;

import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.enums.District;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BuildingConverter {

    public BuildingSearchResponse toBuildingSearchResponse(Building building) {
        BuildingSearchResponse response = new BuildingSearchResponse();
        response.setId(building.getId());
        response.setName(building.getName());
        response.setAddress(buildAddress(building));
        response.setManagerName(building.getManagerName());
        response.setManagePhoneNumber(building.getManagerPhoneNumber());
        response.setFloorArea(building.getFloorArea());
        response.setRentArea(buildRentArea(building));
        response.setRentPrice((long) building.getPrice());
        response.setServiceFee(building.getServiceFee());
        response.setBrokerageFee(toDouble(building.getBrokerageFee()));
        if (building.getNumberOfBasement() != null) {
            response.setNumberOfBasement(String.valueOf(building.getNumberOfBasement()));
        }
        return response;
    }


    public BuildingDTO toBuildingDTO(Building building) {
        BuildingDTO dto = new BuildingDTO();
        dto.setId(building.getId());
        dto.setName(building.getName());
        dto.setStreet(building.getStreet());
        dto.setWard(building.getWard());
        dto.setDistrictId(building.getDistrict());
        dto.setStructure(building.getStructure());
        dto.setNumberOfBasement(toLong(building.getNumberOfBasement()));
        dto.setFloorArea(building.getFloorArea());
        dto.setDirection(building.getDirection());
        dto.setLevel(parseLong(building.getLevel()));
        dto.setRentPrice((long) building.getPrice());
        dto.setRentPriceDescription(building.getRentPriceDescription());
        dto.setServiceFee(building.getServiceFee());
        dto.setCarFee(building.getCarFee());
        dto.setOverTimeFee(building.getOverTimeFee());
        dto.setBrokerageFee(toDouble(building.getBrokerageFee()));
        dto.setNote(building.getNote());
        dto.setManagerName(building.getManagerName());
        dto.setManagerPhoneNumber(building.getManagerPhoneNumber());
        dto.setImage(building.getImage());
        dto.setRentArea(buildRentArea(building));
        dto.setTypeCode(splitTypeCode(building.getType()));
        if (building.getImage() != null && building.getImage().length > 0) {
            dto.setUploadImage(Base64.getEncoder().encodeToString(building.getImage()));
        }
        return dto;
    }
    public Building toBuilding(BuildingDTO dto){
        Building building = new Building();
        updateBuilding(dto, building, false);
        if (building.getCreateDate() == null) {
            building.setCreateDate(new Date());
        }
        return building;
    }

    public void updateBuilding(BuildingDTO dto, Building building){
        updateBuilding(dto, building, true);
    }


    private Long toLong(Integer value) {
        return value == null ? null : value.longValue();
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private Double toDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    private List<String> splitTypeCode(String type) {
        if (type == null || type.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(type.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .toList();
    }
    private String buildAddress(Building building) {
        Map<String, String> districtMap = District.getDistrictMap();
        String district = districtMap.getOrDefault(building.getDistrict(), building.getDistrict());
        return Stream.of(building.getStreet(), building.getWard(), district)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .collect(Collectors.joining(", "));
    }
    private Integer toInteger(Long value) {
        return value == null ? null : value.intValue();
    }

    private BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    private void updateBuilding(BuildingDTO dto, Building building, boolean merge){
        if (!merge || dto.getName() != null) {
            building.setName(dto.getName());
        }
        if (!merge || dto.getStreet() != null) {
            building.setStreet(dto.getStreet());
        }
        if (!merge || dto.getWard() != null) {
            building.setWard(dto.getWard());
        }
        if (!merge || dto.getDistrictId() != null) {
            building.setDistrict(dto.getDistrictId());
        }
        if (!merge || dto.getStructure() != null) {
            building.setStructure(dto.getStructure());
        }
        if (!merge || dto.getNumberOfBasement() != null) {
            building.setNumberOfBasement(toInteger(dto.getNumberOfBasement()));
        }
        if (!merge || dto.getFloorArea() != null) {
            building.setFloorArea(dto.getFloorArea());
        }
        if (!merge || dto.getDirection() != null) {
            building.setDirection(dto.getDirection());
        }
        if (!merge || dto.getLevel() != null) {
            building.setLevel(dto.getLevel() == null ? null : dto.getLevel().toString());
        }
        if (!merge || dto.getRentPrice() != null) {
            building.setPrice(dto.getRentPrice() == null ? 0 : dto.getRentPrice());
        }
        if (!merge || dto.getRentPriceDescription() != null) {
            building.setRentPriceDescription(dto.getRentPriceDescription());
        }
        if (!merge || dto.getServiceFee() != null) {
            building.setServiceFee(dto.getServiceFee());
        }
        if (!merge || dto.getCarFee() != null) {
            building.setCarFee(dto.getCarFee());
        }
        if (!merge || dto.getOverTimeFee() != null) {
            building.setOverTimeFee(dto.getOverTimeFee());
        }
        if (!merge || dto.getBrokerageFee() != null) {
            building.setBrokerageFee(toBigDecimal(dto.getBrokerageFee()));
        }
        if (dto.getTypeCode() != null) {
            building.setType(String.join(",", dto.getTypeCode()));
        }
        if (!merge || dto.getNote() != null) {
            building.setNote(dto.getNote());
        }
        if (!merge || dto.getManagerName() != null) {
            building.setManagerName(dto.getManagerName());
        }
        if (!merge || dto.getManagerPhoneNumber() != null) {
            building.setManagerPhoneNumber(dto.getManagerPhoneNumber());
        }
        if (dto.getImage() != null) {
            building.setImage(dto.getImage());
        }
        if (dto.getUploadImage() != null && !dto.getUploadImage().isBlank()) {
            building.setImage(toImageBytes(dto.getUploadImage()));
        }
        if (!merge || dto.getRentArea() != null) {
            updateRentAreas(dto, building);
        }
    }

    private void updateRentAreas(BuildingDTO dto, Building building) {
        if (building.getRentAreas() == null) {
            building.setRentAreas(new ArrayList<>());
        }
        building.getRentAreas().clear();
        if (dto.getRentArea() == null || dto.getRentArea().isBlank()) {
            return;
        }
        Arrays.stream(dto.getRentArea().split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(Long::parseLong)
                .forEach(value -> {
                    RentArea rentArea = new RentArea();
                    rentArea.setValue(value);
                    rentArea.setBuilding(building);
                    rentArea.setCreatedDate(LocalDate.now());
                    building.getRentAreas().add(rentArea);
                });
    }

    private String buildRentArea(Building building) {
        if (building.getRentAreas() == null || building.getRentAreas().isEmpty()) {
            return null;
        }
        return building.getRentAreas().stream()
                .map(RentArea::getValue)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private byte[] toImageBytes(String uploadImage) {
        String base64Image = uploadImage;
        if (base64Image.contains(",")) {
            base64Image = base64Image.split(",")[1];
        }
        return Base64.getDecoder().decode(base64Image);
    }

}
