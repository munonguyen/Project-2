package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.converter.BuildingConverter;
import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.entity.User;
import com.devon.building.exception.DataBuildingInvalidException;
import com.devon.building.matcher.BuildingSearchMatcher;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import com.devon.building.model.dto.response.StaffResponseDTO;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.BuildingRepositoryCustom;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.BuildingService;
import com.devon.building.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

  private final BuildingRepository buildingRepository;
  private final BuildingConverter buildingConverter;
  private final BuildingSearchMatcher buildingSearchMatcher;
  private final UserRepository userRepository;
  private final BuildingRepositoryCustom buildingRepositoryCustom;

  @Override
  public PaginationResult<BuildingSearchResponse> getAllBuildings(
      BuildingSearchRequest buildingSearchRequest, int page, int maxResult, int maxNavigationPage) {
    PaginationResult<Building> buildingPage =
        buildingRepositoryCustom.queryBuilding(
            page, maxResult, maxNavigationPage, buildingSearchRequest);

    List<BuildingSearchResponse> results =
        buildingPage.getList().stream().map(buildingConverter::toBuildingResponse).toList();

    return new PaginationResult<>(
        results,
        buildingPage.getTotalRecords(),
        buildingPage.getCurrentPage(),
        buildingPage.getMaxResult(),
        maxNavigationPage);
  }

  @Override
  public Building createBuilding(BuildingDTO dto) {
    Building building = buildingConverter.toBuildingEntity(dto);
    buildingConverter.parseRentAreas(building, dto.getRentArea());
    return buildingRepository.save(building);
  }

  @Override
  public Building updateBuilding(BuildingDTO dto) {
    Building building =
        buildingRepository
            .findById(dto.getId())
            .orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy tòa nhà có ID " + dto.getId()));

    buildingConverter.updateBuildingEntity(dto, building);
    clearRentAreas(building);
    buildingConverter.parseRentAreas(building, dto.getRentArea());
    return buildingRepository.save(building);
  }

  @Override
  public Building findById(Long id) {
    return buildingRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tòa nhà có ID " + id));
  }

  @Override
  public void deleteBuilding(List<Long> ids) {
    List<Long> buildingIds = ValidationUtils.normalizeIds(ids, "Danh sách ID tòa nhà");
    if (!buildingIds.isEmpty()) {
      buildingRepository.deleteAllById(buildingIds);
    }
  }

  @Override
  public ResponseDTO<List<StaffResponseDTO>> loadStaffs(Long buildingId) {
    Building building = findById(buildingId);

    Set<Long> assignedStaffIds =
        building.getStaffs().stream()
            .filter(Objects::nonNull)
            .map(User::getId)
            .collect(Collectors.toSet());

    List<StaffResponseDTO> staffResponseDTOS =
        userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE).stream()
            .map(
                user -> {
                  StaffResponseDTO dto = new StaffResponseDTO();
                  dto.setId(user.getId());
                  dto.setUserName(user.getUserName());
                  dto.setChecked(assignedStaffIds.contains(user.getId()) ? "checked" : "");
                  return dto;
                })
            .toList();

    ResponseDTO<List<StaffResponseDTO>> responseDTO = new ResponseDTO<>();
    responseDTO.setData(staffResponseDTOS);
    responseDTO.setMessage("Load staff list successfully");
    return responseDTO;
  }

  @Override
  public Building saveAssignBuilding(AssignBuildingDTO assignBuildingDTO) {
    Building building = findById(assignBuildingDTO.getBuildingId());

    List<Long> staffIds =
        ValidationUtils.normalizeIds(assignBuildingDTO.getStaffIds(), "Danh sách ID nhân viên");

    replaceStaffs(building, findAssignableStaffs(staffIds));

    return buildingRepository.save(building);
  }

  private void clearRentAreas(Building building) {
    List<RentArea> rentAreas = new ArrayList<>(building.getRentAreas());
    rentAreas.forEach(rentArea -> rentArea.setBuilding(null));
    building.getRentAreas().clear();
  }

  private void replaceStaffs(Building building, List<User> staffs) {
    building.getStaffs().forEach(staff -> staff.getBuildings().remove(building));
    building.getStaffs().clear();

    staffs.stream()
        .filter(Objects::nonNull)
        .filter(staff -> !building.getStaffs().contains(staff))
        .forEach(
            staff -> {
              building.getStaffs().add(staff);
              if (!staff.getBuildings().contains(building)) {
                staff.getBuildings().add(building);
              }
            });
  }

  private List<User> findAssignableStaffs(List<Long> staffIds) {
    if (staffIds.isEmpty()) {
      return Collections.emptyList();
    }

    List<User> staffs = userRepository.findAllById(staffIds);
    Set<Long> foundStaffIds = staffs.stream().map(User::getId).collect(Collectors.toSet());
    List<Long> missingStaffIds =
        staffIds.stream().filter(id -> !foundStaffIds.contains(id)).toList();
    if (!missingStaffIds.isEmpty()) {
      throw new DataBuildingInvalidException(
          "Không tìm thấy nhân viên có ID " + formatIds(missingStaffIds));
    }

    List<Long> invalidStaffIds =
        staffs.stream()
            .filter(
                staff ->
                    !staff.isActive() || !SystemConstant.STAFF_ROLE.equals(staff.getUserRole()))
            .map(User::getId)
            .toList();
    if (!invalidStaffIds.isEmpty()) {
      throw new DataBuildingInvalidException(
          "Nhân viên không hợp lệ có ID " + formatIds(invalidStaffIds));
    }
    return staffs;
  }

  private String formatIds(List<Long> ids) {
    return ids.stream().map(String::valueOf).collect(Collectors.joining(", "));
  }
}
