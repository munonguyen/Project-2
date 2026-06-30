package com.devon.building.service.impl;

import com.devon.building.CustomException.DataBuildingInvalidException;
import com.devon.building.converter.BuildingConverter;
import com.devon.building.entity.Building;
import com.devon.building.matcher.BuildingSearchMatcher;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final BuildingConverter buildingConverter;
    private final BuildingSearchMatcher buildingSearchMatcher;
    private final AssignmentBuildingService assignmentBuildingService;
    private final RentAreaService rentAreaService;

    @Override
    public List<BuildingSearchResponse> getAllBuildings(BuildingSearchRequest buildingSearchRequest) {
        return buildingRepository.findAll().stream()
                .filter(building -> buildingSearchMatcher.matches(building, buildingSearchRequest))
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

        buildingConverter.updateBuilding(dto, building);
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
        List<Long> buildingIds = normalizeIds(ids, "Danh sách ID tòa nhà");
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

    private List<Long> normalizeIds(List<Long> ids, String fieldName) {
        if (ids == null) {
            return Collections.emptyList();
        }
        if (ids.stream().anyMatch(Objects::isNull)) {
            throw new DataBuildingInvalidException(fieldName + " không hợp lệ");
        }
        return ids.stream().distinct().toList();
    }
}
