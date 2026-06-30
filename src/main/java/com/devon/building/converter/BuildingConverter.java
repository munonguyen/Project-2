package com.devon.building.converter;

import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.enums.District;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
        BuildingSearchResponse response = modelMapper.map(building, BuildingSearchResponse.class);
        response.setAddress(buildAddress(building));
        response.setRentArea(buildRentArea(building));
        response.setRentPrice((long) building.getPrice());
        response.setManagerPhoneNumber(building.getManagerPhoneNumber());
        if (building.getBrokerageFee() != null) {
            response.setBrokerageFee(building.getBrokerageFee().doubleValue());
        }
        if (building.getNumberOfBasement() != null) {
            response.setNumberOfBasement(String.valueOf(building.getNumberOfBasement()));
        }
        return response;
    }


    public BuildingDTO toBuildingDTO(Building building) {
        BuildingDTO dto = modelMapper.map(building, BuildingDTO.class);
        dto.setDistrictId(building.getDistrict());
        dto.setRentPrice((long) building.getPrice());
        dto.setRentArea(buildRentArea(building));
        dto.setTypeCode(splitTypeCode(building.getType()));
        if (building.getImage() != null && building.getImage().length > 0) {
            dto.setUploadImage(Base64.getEncoder().encodeToString(building.getImage()));
        }
        return dto;
    }
    public Building toBuilding(BuildingDTO dto){
        Building building = new Building();
        modelMapper.map(dto, building);
        mapCustomFields(dto, building);
        if (building.getCreatedDate() == null) {
            building.setCreatedDate(new Date());
        }
        return building;
    }

    public void mapToExistingBuilding(BuildingDTO dto, Building building){
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

    private byte[] toImageBytes(String uploadImage) {
        String base64Image = uploadImage;
        if (base64Image.contains(",")) {
            base64Image = base64Image.split(",")[1];
        }
        return Base64.getDecoder().decode(base64Image);
    }

}
