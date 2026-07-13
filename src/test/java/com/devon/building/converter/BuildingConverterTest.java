package com.devon.building.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

class BuildingConverterTest {

  private BuildingConverter buildingConverter;

  @BeforeEach
  void setUp() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    buildingConverter = new BuildingConverter(modelMapper);
  }

  @Test
  void toBuildingResponseMapsRentPriceAndRentAreas() {
    Building building = buildingWithRentAreas();
    building.setPrice(1750);

    BuildingSearchResponse response = buildingConverter.toBuildingResponse(building);

    assertEquals(1750L, response.getRentPrice());
    assertEquals("150,250", response.getRentArea());
  }

  @Test
  void toBuildingDtoMapsRentAreasForEditForm() {
    BuildingDTO dto = buildingConverter.toBuildingDTO(buildingWithRentAreas());

    assertEquals("150,250", dto.getRentArea());
  }

  private Building buildingWithRentAreas() {
    Building building = new Building();
    building.getRentAreas().add(rentArea(150L));
    building.getRentAreas().add(rentArea(250L));
    return building;
  }

  private RentArea rentArea(Long value) {
    RentArea rentArea = new RentArea();
    rentArea.setValue(value);
    return rentArea;
  }
}
