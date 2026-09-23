package com.devon.building.service;

import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.pagination.PaginationResult;

import java.util.List;

public interface BuildingService {
    PaginationResult<BuildingSearchResponse> findBuilding(BuildingSearchRequest buildingSearchRequest, int page, int maxPageItem, int maxNavigationPage);

    ResponseDTO createBuilding(BuildingDTO buildingDTO);

    ResponseDTO updateBuilding(BuildingDTO buildingDTO);

    ResponseDTO deleteBuilding(List<Long> ids);

    BuildingDTO findById(Long id);

    ResponseDTO loadStaffs(Long buildingId);

    ResponseDTO assignmentBuilding(AssignBuildingDTO assignBuildingDTO);
}
