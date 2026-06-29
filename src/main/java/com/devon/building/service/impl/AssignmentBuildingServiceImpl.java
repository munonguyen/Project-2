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
        ResponseDTO responseDTO = new ResponseDTO();

        List<User> staffs = userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE);
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new DataBuildingInvalidException(
                        "Không tìm thấy tòa nhà có ID " + buildingId));
        Set<Long> assignedStaffs = building.getAssignmentBuildings().stream()
                .map(AssignmentBuilding::getStaff)
                .filter(Objects::nonNull)
                .map(User::getId)
                .collect(Collectors.toSet());

        List<StaffResponseDTO> staffResponseDTOS = new ArrayList<>();
        for (User user : staffs) {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setId(user.getId());
            staffResponseDTO.setUserName(user.getUserName());
            staffResponseDTO.setChecked("");
            if (assignedStaffs.contains(user.getId())) {
                staffResponseDTO.setChecked("checked");
            }
            staffResponseDTOS.add(staffResponseDTO);
        }
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
        List<User> staffs = findAssignableStaffs(staffIds);

        List<AssignmentBuilding> assignments = new ArrayList<>();
        for (User staff : staffs) {
            assignments.add(new AssignmentBuilding(building, staff));
        }

        deleteByBuildingId(building.getId());
        if (!assignments.isEmpty()) {
            assignmentBuildingRepository.saveAll(assignments);
        }
        return building;
    }

    @Override
    public void deleteByBuildingId(Long buildingId) {
        assignmentBuildingRepository.deleteByBuildingId(buildingId);
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
