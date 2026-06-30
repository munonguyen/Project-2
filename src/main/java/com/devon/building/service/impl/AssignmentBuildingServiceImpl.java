package com.devon.building.service.impl;

import com.devon.building.CustomException.DataBuildingInvalidException;
import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.AssignmentBuilding;
import com.devon.building.entity.Building;
import com.devon.building.entity.User;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.response.StaffResponseDTO;
import com.devon.building.repository.AssignmentBuildingRepository;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.AssignmentBuildingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentBuildingServiceImpl implements AssignmentBuildingService {

    private final AssignmentBuildingRepository assignmentBuildingRepository;
    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseDTO loadStaffs(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new DataBuildingInvalidException(
                        "Không tìm thấy tòa nhà có ID " + buildingId));

        Set<Long> assignedStaffIds = building.getAssignmentBuildings().stream()
                .map(AssignmentBuilding::getStaff)
                .filter(Objects::nonNull)
                .map(User::getId)
                .collect(Collectors.toSet());

        List<StaffResponseDTO> staffResponseDTOS = userRepository
                .findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE).stream()
                .map(user -> {
                    StaffResponseDTO dto = new StaffResponseDTO();
                    dto.setId(user.getId());
                    dto.setUserName(user.getUserName());
                    dto.setChecked(assignedStaffIds.contains(user.getId()) ? "checked" : "");
                    return dto;
                })
                .toList();

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("Load staff list successfully");
        return responseDTO;
    }

    @Override
    public Building saveAssignBuilding(AssignBuildingDTO assignBuildingDTO) {
        Building building = buildingRepository.findById(assignBuildingDTO.getBuildingId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy tòa nhà có ID " + assignBuildingDTO.getBuildingId()));

        List<Long> staffIds = normalizeIds(assignBuildingDTO.getStaffIds(), "Danh sách ID nhân viên");
        
        deleteByBuildingId(building.getId());
        
        List<AssignmentBuilding> assignments = findAssignableStaffs(staffIds).stream()
                .map(staff -> new AssignmentBuilding(building, staff))
                .toList();

        if (!assignments.isEmpty()) {
            assignmentBuildingRepository.saveAll(assignments);
        }
        return building;
    }

    @Override
    public void deleteByBuildingId(Long buildingId) {
        assignmentBuildingRepository.deleteByBuildingId(buildingId);
    }

    @Override
    public void deleteByBuildingIds(List<Long> buildingIds) {
        assignmentBuildingRepository.deleteByBuildingIdIn(buildingIds);
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

    private List<User> findAssignableStaffs(List<Long> staffIds) {
        if (staffIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<User> staffs = userRepository.findAllById(staffIds);
        Set<Long> foundStaffIds = staffs.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        List<Long> missingStaffIds = staffIds.stream()
                .filter(id -> !foundStaffIds.contains(id))
                .toList();
        if (!missingStaffIds.isEmpty()) {
            throw new DataBuildingInvalidException("Không tìm thấy nhân viên có ID " + formatIds(missingStaffIds));
        }

        List<Long> invalidStaffIds = staffs.stream()
                .filter(staff -> !staff.isActive() || !SystemConstant.STAFF_ROLE.equals(staff.getUserRole()))
                .map(User::getId)
                .toList();
        if (!invalidStaffIds.isEmpty()) {
            throw new DataBuildingInvalidException("Nhân viên không hợp lệ có ID " + formatIds(invalidStaffIds));
        }
        return staffs;
    }

    private String formatIds(List<Long> ids) {
        return ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
