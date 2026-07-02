package com.devon.building.service;

import com.devon.building.entity.Building;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import com.devon.building.model.dto.response.StaffResponseDTO;
import com.devon.building.pagination.PaginationResult;

import java.util.List;

public interface BuildingService {

    PaginationResult<BuildingSearchResponse> getAllBuildings(BuildingSearchRequest buildingSearchRequest, int page, int maxResult, int maxNavigationPage);

    Building createBuilding(BuildingDTO dto);

    Building updateBuilding(BuildingDTO dto);

    Building findById(Long id);

    void deleteBuilding(List<Long> ids);

    ResponseDTO<List<StaffResponseDTO>> loadStaffs(Long buildingId);

    Building saveAssignBuilding(AssignBuildingDTO assignBuildingDTO);
}
