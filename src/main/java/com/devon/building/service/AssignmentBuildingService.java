package com.devon.building.service;

import com.devon.building.entity.Building;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.response.StaffResponseDTO;

import java.util.List;

public interface AssignmentBuildingService {

    ResponseDTO<List<StaffResponseDTO>> loadStaffs(Long buildingId);

    Building saveAssignBuilding(AssignBuildingDTO assignBuildingDTO);

    void deleteByBuildingId(Long buildingId);

    void deleteByBuildingIds(List<Long> buildingIds);
}
