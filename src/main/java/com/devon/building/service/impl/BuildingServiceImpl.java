package com.devon.building.service.impl;

import com.devon.building.exception.DataBuildingInvalidException;
import com.devon.building.converter.BuildingConverter;
import com.devon.building.entity.Building;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.service.AssignmentBuildingService;
import com.devon.building.service.BuildingService;
import com.devon.building.service.RentAreaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import com.devon.building.util.ValidationUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final BuildingConverter buildingConverter;
    private final AssignmentBuildingService assignmentBuildingService;
    private final RentAreaService rentAreaService;

    @Override
    public List<BuildingSearchResponse> getAllBuildings(BuildingSearchRequest buildingSearchRequest) {
        return buildingRepository.searchBuildings(buildingSearchRequest).stream()
                .map(buildingConverter::toBuildingSearchResponse)
                .toList();
    }

    @Override
    public Building createBuilding(BuildingDTO dto) {
        Building building = buildingConverter.toBuilding(dto);
        Building savedBuilding = buildingRepository.save(building);
        rentAreaService.saveByBuilding(savedBuilding, dto.getRentArea());
        return savedBuilding;
    }

    @Override
    public Building updateBuilding(BuildingDTO dto) {
        Building building = buildingRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tòa nhà có ID " + dto.getId()));

        buildingConverter.mapToExistingBuilding(dto, building);
        Building savedBuilding = buildingRepository.save(building);

        rentAreaService.replaceByBuilding(savedBuilding, dto.getRentArea());
        return savedBuilding;
    }

    @Override
    public Building findById(Long id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tòa nhà có ID " + id));
    }

    @Override
    public void deleteBuilding(List<Long> ids) {
        List<Long> buildingIds = ValidationUtils.normalizeIds(ids, "Danh sách ID tòa nhà");
        if (!buildingIds.isEmpty()) {
            List<Long> existingIds = buildingRepository.findAllById(buildingIds).stream()
                    .map(Building::getId)
                    .toList();

            if (!existingIds.isEmpty()) {
                assignmentBuildingService.deleteByBuildingIds(existingIds);
                rentAreaService.deleteByBuildingIds(existingIds);
                buildingRepository.deleteAllByIdInBatch(existingIds);
            }
        }
    }
}
