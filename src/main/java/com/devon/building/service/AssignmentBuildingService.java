package com.devon.building.service;

import com.devon.building.entity.Building;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.ResponseDTO;

public interface AssignmentBuildingService {

    ResponseDTO loadStaffs(Long buildingId);

    Building saveAssignBuilding(AssignBuildingDTO assignBuildingDTO);

    void deleteByBuildingId(Long buildingId);

    void deleteByBuildingIds(java.util.List<Long> buildingIds);
}
