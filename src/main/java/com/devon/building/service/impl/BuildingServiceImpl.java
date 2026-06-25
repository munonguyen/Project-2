package com.devon.building.service.impl;

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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final RentAreaRepository rentAreaRepository;
    private final AssignmentBuildingRepository assignmentBuildingRepository;
    private final UserRepository userRepository;
    private final BuildingConverter buildingConverter;
    private final BuildingSearchMatcher buildingSearchMatcher;

    @Override
    public ResponseDTO loadStaffs(Long buildingId) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<User> staffs = userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE);

        List<AssignmentBuilding> assignments = assignmentBuildingRepository.findByBuildingId(buildingId);
        Set<Long> assignedStaffs = assignments.stream()
                .map(a -> a.getStaff().getId())
                .collect(Collectors.toSet());

        List<StaffResponseDTO> staffResponseDTOS = new ArrayList<>();
        for (User user : staffs) {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setId(user.getId());
            staffResponseDTO.setUserName(user.getUserName());
            staffResponseDTO.setChecked(assignedStaffs.contains(user.getId()) ? "checked" : "");
            staffResponseDTOS.add(staffResponseDTO);
        }

        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("Tải danh sách nhân viên thành công");
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
        if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                rentAreaRepository.deleteByBuildingId(id);
                assignmentBuildingRepository.deleteByBuildingId(id);
            }
            buildingRepository.deleteAllById(ids);
        }
    }

    @Override
    public Building saveAssignBuilding(AssignBuildingDTO assignBuildingDTO) {
        Building building = buildingRepository.findById(assignBuildingDTO.getBuildingId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy tòa nhà có ID " + assignBuildingDTO.getBuildingId()));

        assignmentBuildingRepository.deleteByBuildingId(building.getId());

        if (assignBuildingDTO.getStaffIds() != null) {
            List<User> staffs = userRepository.findAllById(assignBuildingDTO.getStaffIds());
            List<AssignmentBuilding> assignments = new ArrayList<>();
            for (User staff : staffs) {
                AssignmentBuilding assignment = new AssignmentBuilding();
                assignment.setBuilding(building);
                assignment.setStaff(staff);
                assignments.add(assignment);
            }
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
}
