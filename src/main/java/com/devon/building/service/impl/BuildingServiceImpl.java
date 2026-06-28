package com.devon.building.service.impl;

import com.devon.building.CustomException.DataBuildingInvalidException;
import com.devon.building.constant.SystemConstant;
import com.devon.building.converter.BuildingConverter;
import com.devon.building.entity.AssignmentBuilding;
import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.entity.User;
import com.devon.building.matcher.BuildingSearchMatcher;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import com.devon.building.model.dto.response.StaffResponseDTO;
import com.devon.building.repository.AssignmentBuildingRepository;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.RentAreaRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.BuildingService;
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
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final AssignmentBuildingRepository assignmentBuildingRepository;
    private final RentAreaRepository rentAreaRepository;
    private final UserRepository userRepository;
    private final BuildingConverter buildingConverter;
    private final BuildingSearchMatcher buildingSearchMatcher;

    @Override
    public ResponseDTO loadStaffs(Long buildingId) {
        ResponseDTO responseDTO = new ResponseDTO();

        List<User> staffs = userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE); //get All staffs
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new DataBuildingInvalidException(
                        "Không tìm thấy tòa nhà có ID " + buildingId));
        Set<Long> assignedStaffs = building.getAssignmentBuildings().stream()
                .map(AssignmentBuilding::getStaff)
                .filter(Objects::nonNull)
                .map(User::getId)
                .collect(Collectors.toSet()); // Lay danh sach id cac nhan vien dang quan ly toa nha hien tai
        List<StaffResponseDTO> staffResponseDTOS = new ArrayList<>();
        for (User user : staffs) {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setId(user.getId());
            staffResponseDTO.setUserName(user.getUserName());
            staffResponseDTO.setChecked("");
            if(assignedStaffs.contains(user.getId())) {
                staffResponseDTO.setChecked("checked");
            }
            staffResponseDTOS.add(staffResponseDTO);
        }
        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("Load staff list successfully");
        return responseDTO;
    }

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
        saveRentAreasFromDTO(dto, savedBuilding);
        return savedBuilding;
    }

    @Override
    public Building updateBuilding(BuildingDTO dto) {
        Building building = buildingRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tòa nhà có ID " + dto.getId()));

        // Xóa sạch dữ liệu cũ trong DB
        rentAreaRepository.deleteByBuildingId(building.getId());

        buildingConverter.updateBuilding(dto, building);
        Building savedBuilding = buildingRepository.save(building);

        // Tạo dữ liệu mới từ request và lưu lại
        saveRentAreasFromDTO(dto, savedBuilding);

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
            validateBuildingsExist(buildingIds);
            for (Long id : buildingIds) {
                assignmentBuildingRepository.deleteByBuildingId(id);
                rentAreaRepository.deleteByBuildingId(id);
            }
            buildingRepository.deleteAllById(buildingIds);
        }
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

        assignmentBuildingRepository.deleteByBuildingId(building.getId());
        if (!assignments.isEmpty()) {
            assignmentBuildingRepository.saveAll(assignments);
        }
        return building;
    }

    private void saveRentAreasFromDTO(BuildingDTO dto, Building savedBuilding) {
        if (dto.getRentArea() == null || dto.getRentArea().isBlank()) {
            return;
        }
        List<RentArea> newRentAreas = new ArrayList<>();
        java.util.Arrays.stream(dto.getRentArea().split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(Long::parseLong)
                .forEach(value -> {
                    RentArea rentArea = new RentArea();
                    rentArea.setValue(value);
                    rentArea.setBuilding(savedBuilding);
                    rentArea.setCreatedDate(java.time.LocalDate.now());
                    newRentAreas.add(rentArea);
                });
        if (!newRentAreas.isEmpty()) {
            rentAreaRepository.saveAll(newRentAreas);
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

    private void validateBuildingsExist(List<Long> buildingIds) {
        Set<Long> existingIds = buildingRepository.findAllById(buildingIds).stream()
                .map(Building::getId)
                .collect(Collectors.toSet());
        List<Long> missingIds = buildingIds.stream()
                .filter(id -> !existingIds.contains(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy tòa nhà có ID " + formatIds(missingIds));
        }
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
