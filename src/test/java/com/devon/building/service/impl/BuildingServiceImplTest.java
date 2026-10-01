package com.devon.building.service.impl;

import com.devon.building.converter.BuildingConverter;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.RentAreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuildingServiceImplTest {

    @Mock
    private RentAreaService rentAreaService;
    @Mock
    private BuildingConverter buildingConverter;
    @Mock
    private BuildingRepository buildingRepository;
    @Mock
    private UserRepository userRepository;

    private BuildingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BuildingServiceImpl(
                rentAreaService,
                buildingConverter,
                buildingRepository,
                userRepository);
    }

    @Test
    void createBuildingPersistsConvertedEntityAndRentAreas() {
        BuildingDTO dto = new BuildingDTO();
        dto.setTypeCode(List.of("office", "retail"));
        dto.setRentArea("100, 200");
        BuildingEntity entity = new BuildingEntity();

        when(buildingConverter.toBuildingEntity(dto)).thenReturn(entity);

        service.createBuilding(dto);

        assertEquals("office, retail", entity.getRentType());
        verify(rentAreaService).saveOrUpdateRentArea(entity, "100, 200");
        verify(buildingRepository).save(entity);
    }

    @Test
    void updateBuildingUsesExistingEntity() {
        BuildingDTO dto = new BuildingDTO();
        dto.setId(10L);
        dto.setTypeCode(List.of("office"));
        dto.setRentArea("150");
        BuildingEntity entity = new BuildingEntity();

        when(buildingRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(buildingConverter.toBuildingDTO(entity)).thenReturn(dto);

        service.updateBuilding(dto);

        verify(buildingConverter).updateBuildingEntity(dto, entity);
        verify(rentAreaService).saveOrUpdateRentArea(entity, "150");
        verify(buildingRepository).save(entity);
    }

    @Test
    void deleteBuildingClearsAssignmentsThenDeletesEntities() {
        BuildingEntity first = new BuildingEntity();
        BuildingEntity second = new BuildingEntity();
        List<Long> ids = List.of(1L, 2L);
        List<BuildingEntity> entities = List.of(first, second);

        when(buildingRepository.findAllById(ids)).thenReturn(entities);

        service.deleteBuilding(ids);

        verify(buildingRepository).deleteAll(entities);
        assertEquals(0, first.getUser().size());
        assertEquals(0, second.getUser().size());
    }
}
