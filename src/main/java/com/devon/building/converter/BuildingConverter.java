package com.devon.building.converter;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.enums.District;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@RequiredArgsConstructor
public class BuildingConverter {

    private final ModelMapper modelMapper;

    public BuildingSearchBuilder toBuildingSearchBuilder(BuildingSearchRequest request){
        return BuildingSearchBuilder.builder()
                .name(request.getName())
                .floorArea(request.getFloorArea())
                .district(request.getDistrict())
                .ward(request.getWard())
                .street(request.getStreet())
                .numberOfBasement(request.getNumberOfBasement())
                .direction(request.getDirection())
                .level(request.getLevel())
                .areaFrom(request.getAreaFrom())
                .areaTo(request.getAreaTo())
                .rentPriceFrom(request.getRentPriceFrom())
                .rentPriceTo(request.getRentPriceTo())
                .managerName(request.getManagerName())
                .managerPhone(request.getManagerPhone())
                .staffId(request.getStaffId())
                .typeCode(request.getTypeCode())
                .build();
    }

    public BuildingEntity toBuildingEntity(BuildingDTO buildingDTO) {
        return modelMapper.map(buildingDTO, BuildingEntity.class);
    }

    public void updateBuildingEntity(BuildingDTO buildingDTO, BuildingEntity buildingEntity){
            modelMapper.map(buildingDTO, buildingEntity);
    }

    public BuildingSearchResponse toBuildingResponse(BuildingEntity buildingEntity){
        BuildingSearchResponse buildingSearchResponse = modelMapper.map(buildingEntity, BuildingSearchResponse.class);
        Map<String, String> district = District.getDistrictMap();
        String districtName = district.get(buildingEntity.getDistrict());
        if(districtName != null){
            buildingSearchResponse.setAddress(Stream.of(buildingEntity.getStreet(), buildingEntity.getWard(), districtName)
                    .filter(it -> it != null && !it.isBlank())
                    .collect(Collectors.joining(", ")));
        }
        buildingSearchResponse.setRentArea(buildingEntity.getRentArea().stream().map(item -> item.getValue().toString()).collect(Collectors.joining(", ")));
        return buildingSearchResponse;
    }

    public BuildingDTO toBuildingDTO(BuildingEntity buildingEntity){
        return modelMapper.map(buildingEntity, BuildingDTO.class);
    }

}
