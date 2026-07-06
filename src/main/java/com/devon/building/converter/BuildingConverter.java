package com.devon.building.converter;

import com.devon.building.builder.BuildingDTOBuilder;
import com.devon.building.builder.BuildingSearchResponseBuilder;
import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.enums.District;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BuildingConverter {

    private final ModelMapper modelMapper;

    public BuildingSearchResponse toBuildingSearchResponse(Building building) {
        BuildingSearchResponseBuilder builder = new BuildingSearchResponseBuilder()
                .id(building.getId())
                .name(building.getName())
                .address(buildAddress(building))
                .managerName(building.getManagerName())
                .managerPhoneNumber(building.getManagerPhoneNumber())
                .floorArea(building.getFloorArea())
                .rentArea(buildRentArea(building))
                .rentPrice((long) building.getPrice())
                .serviceFee(building.getServiceFee());
        if (building.getBrokerageFee() != null) {
            builder.brokerageFee(building.getBrokerageFee().doubleValue());
        }
        if (building.getNumberOfBasement() != null) {
            builder.numberOfBasement(String.valueOf(building.getNumberOfBasement()));
        }
        return builder.build();
    }

    public BuildingDTO toBuildingDTO(Building building) {
        BuildingDTOBuilder builder = new BuildingDTOBuilder()
                .id(building.getId())
                .name(building.getName())
                .street(building.getStreet())
                .ward(building.getWard())
                .districtId(building.getDistrict())
                .rentPrice((long) building.getPrice())
                .floorArea(building.getFloorArea())
                .structure(building.getStructure())
                .direction(building.getDirection())
                .rentPriceDescription(building.getRentPriceDescription())
                .serviceFee(building.getServiceFee())
                .carFee(building.getCarFee())
                .overTimeFee(building.getOverTimeFee())
                .note(building.getNote())
                .managerName(building.getManagerName())
                .managerPhoneNumber(building.getManagerPhoneNumber())
                .rentArea(buildRentArea(building))
                .typeCode(splitTypeCode(building.getType()));
        if (building.getNumberOfBasement() != null) {
            builder.numberOfBasement(building.getNumberOfBasement().longValue());
        }
        if (building.getBrokerageFee() != null) {
            builder.brokerageFee(building.getBrokerageFee().doubleValue());
        }
        if (building.getLevel() != null && !building.getLevel().isBlank()) {
            builder.level(Long.valueOf(building.getLevel()));
        }
        if (building.getImage() != null && building.getImage().length > 0) {
            builder.uploadImage(Base64.getEncoder().encodeToString(building.getImage()));
        }
        return builder.build();
    }

    public Building toBuilding(BuildingDTO dto) {
        Building building = new Building();
        modelMapper.map(dto, building);
        mapCustomFields(dto, building);
        if (building.getCreatedDate() == null) {
            building.setCreatedDate(new Date());
        }
        return building;
    }

    public void mapToExistingBuilding(BuildingDTO dto, Building building) {
        ModelMapper skipNullMapper = new ModelMapper();
        skipNullMapper.getConfiguration().setSkipNullEnabled(true).setFullTypeMatchingRequired(true);
        skipNullMapper.map(dto, building);

        mapCustomFields(dto, building);
    }

    private void mapCustomFields(BuildingDTO dto, Building building) {
        if (dto.getDistrictId() != null) {
            building.setDistrict(dto.getDistrictId());
        }
        if (dto.getNumberOfBasement() != null) {
            building.setNumberOfBasement(dto.getNumberOfBasement().intValue());
        }
        if (dto.getLevel() != null) {
            building.setLevel(dto.getLevel().toString());
        }
        if (dto.getRentPrice() != null) {
            building.setPrice(dto.getRentPrice());
        }
        if (dto.getBrokerageFee() != null) {
            building.setBrokerageFee(BigDecimal.valueOf(dto.getBrokerageFee()));
        }
        if (dto.getTypeCode() != null) {
            building.setType(String.join(",", dto.getTypeCode()));
        }
        if (dto.getUploadImage() != null && !dto.getUploadImage().isBlank()) {
            building.setImage(toImageBytes(dto.getUploadImage()));
        }
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

    public void parseRentAreas(Building building, String rentAreaValues) {
        if (rentAreaValues == null || rentAreaValues.isBlank()) {
            return;
        }
        Arrays.stream(rentAreaValues.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(Long::parseLong)
                .forEach(value -> {
                    RentArea rentArea = new RentArea();
                    rentArea.setValue(value);
                    rentArea.setBuilding(building);
                    building.getRentAreas().add(rentArea);
                });
    }

    private byte[] toImageBytes(String uploadImage) {
        String base64Image = uploadImage;
        if (base64Image.contains(",")) {
            base64Image = base64Image.split(",")[1];
        }
        return Base64.getDecoder().decode(base64Image);
    }

}
