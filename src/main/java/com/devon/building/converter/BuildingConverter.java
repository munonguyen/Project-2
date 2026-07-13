package com.devon.building.converter;


import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.enums.District;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuildingConverter {

  private final ModelMapper modelMapper;

  public BuildingSearchBuilder toBuildingSearchBuilder(BuildingSearchRequest request) {
    return BuildingSearchBuilder.builder()
        .name(request.getName())
        .floorArea(request.getFloorArea())
        .district(request.getDistrict())
        .ward(request.getWard())
        .street(request.getStreet())
        .numberOfBasement(request.getNumberOfBasement())
        .direction(request.getDirection())
        .level(request.getLevel())
        .areaFrom(request.getRentAreaFrom())
        .areaTo(request.getRentAreaTo())
        .rentPriceFrom(request.getRentPriceFrom())
        .rentPriceTo(request.getRentPriceTo())
        .managerName(request.getManagerName())
        .managerPhone(request.getManagerPhoneNumber())
        .staffId(request.getStaffId())
        .typeCode(request.getTypeCodes())
        .build();
  }

  public Building toBuildingEntity(BuildingDTO buildingDTO) {
    return modelMapper.map(buildingDTO, Building.class);
  }

  public void updateBuildingEntity(BuildingDTO buildingDTO, Building buildingEntity) {
    byte[] existingImage = buildingEntity.getImage();
    modelMapper.map(buildingDTO, buildingEntity);
    if (buildingDTO.getImage() == null) {
      buildingEntity.setImage(existingImage);
    }
  }

  public BuildingSearchResponse toBuildingResponse(Building buildingEntity) {
    BuildingSearchResponse buildingSearchResponse =
        modelMapper.map(buildingEntity, BuildingSearchResponse.class);
    buildingSearchResponse.setRentPrice((long) buildingEntity.getPrice());
    buildingSearchResponse.setRentArea(buildRentArea(buildingEntity));

    Map<String, String> district = District.getDistrictMap();
    String districtName = district.get(buildingEntity.getDistrict());
    if (districtName != null) {
      buildingSearchResponse.setAddress(
          Stream.of(buildingEntity.getStreet(), buildingEntity.getWard(), districtName)
              .filter(it -> it != null && !it.isBlank())
              .collect(Collectors.joining(", ")));
    }

    return buildingSearchResponse;
  }

  public BuildingDTO toBuildingDTO(Building buildingEntity) {
    BuildingDTO buildingDTO = modelMapper.map(buildingEntity, BuildingDTO.class);
    buildingDTO.setRentArea(buildRentArea(buildingEntity));
    if (buildingEntity.getImage() != null && buildingEntity.getImage().length > 0) {
      buildingDTO.setImageBase64(Base64.getEncoder().encodeToString(buildingEntity.getImage()));
    }
    return buildingDTO;
  }

  public void parseRentAreas(Building building, String rentAreaValues) {
    if (rentAreaValues == null || rentAreaValues.isBlank()) {
      return;
    }
    java.util.Arrays.stream(rentAreaValues.split(","))
        .map(String::trim)
        .filter(value -> !value.isEmpty())
        .map(Long::parseLong)
        .forEach(
            value -> {
              com.devon.building.entity.RentArea rentArea =
                  new com.devon.building.entity.RentArea();
              rentArea.setValue(value);
              rentArea.setBuilding(building);
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
}
