package com.devon.building.service;

import com.devon.building.entity.Building;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;

import java.util.List;

public interface BuildingService {

    ResponseDTO loadStaffs(Long buildingId);

    List<BuildingSearchResponse> getAllBuildings(BuildingSearchRequest buildingSearchRequest);

    Building createBuilding(BuildingDTO dto);

    Building updateBuilding(BuildingDTO dto);

    Building findById(Long id);

    void deleteBuilding(List<Long> ids);

    Building saveAssignBuilding(AssignBuildingDTO assignBuildingDTO);
}
