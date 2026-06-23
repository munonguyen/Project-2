package com.devon.building.service.impl;

import com.devon.building.CustomException.DataBuildingInvalidException;
import com.devon.building.constant.SystemConstant;
import com.devon.building.converter.BuildingConverter;
import com.devon.building.entity.Building;
import com.devon.building.entity.User;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import com.devon.building.model.dto.response.StaffResponseDTO;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.BuildingService;
import com.devon.building.validator.BuildingValidator;
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
        private final UserRepository userRepository;
        private final BuildingConverter buildingConverter;
        private final BuildingValidator buildingValidator;

        @Override
        public ResponseDTO loadStaffs(Long buildingId) {
                ResponseDTO responseDTO = new ResponseDTO();
                List<User> staffs = userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE);
                Building building = buildingRepository.findById(buildingId).orElseThrow(
                                () -> new EntityNotFoundException("Không tìm thấy tòa nhà có ID " + buildingId));
                Set<Long> assignedStaffs = building.getUser().stream().map(User::getId).collect(Collectors.toSet());
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
                responseDTO.setMessage("Tải danh sách nhân viên thành công");
                return responseDTO;

        }

        @Override
        public List<BuildingSearchResponse> getAllBuildings(BuildingSearchRequest buildingSearchRequest) {
                return buildingRepository.findAll()
                                .stream()
                                .filter(buildingSearchRequest::matches)
                                .map(buildingConverter::toBuildingSearchResponse)
                                .toList();

        }

        @Override
        public Building saveBuilding(BuildingDTO dto) {
                boolean isUpdate = dto.getId() != null;
                List<String> errors = buildingValidator.validate(dto, isUpdate);
                if (!errors.isEmpty()) {
                        ResponseDTO errorResponse = new ResponseDTO();
                        errorResponse.setMessage("Dữ liệu không hợp lệ");
                        errorResponse.setDetail(errors);
                        throw new DataBuildingInvalidException(errorResponse);
                }
                if (!isUpdate) {
                        return buildingRepository.save(buildingConverter.toBuilding(dto));
                }
                Building building = buildingRepository.findById(dto.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Không tìm thấy tòa nhà có ID " + dto.getId()));
                buildingConverter.updateBuilding(dto, building);
                return buildingRepository.save(building);
        }

        @Override
        public Building findById(Long id) {
                return buildingRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tòa nhà có ID " + id));

        }

        @Override
        public void deleteBuilding(List<Long> ids) {
                buildingRepository.deleteAllById(ids);
        }

        @Override
        public Building saveAssignBuilding(AssignBuildingDTO assignBuildingDTO) {
                Building building = buildingRepository.findById(assignBuildingDTO.getBuildingId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Không tìm thấy tòa nhà có ID " + assignBuildingDTO.getBuildingId()));
                List<User> staffs = assignBuildingDTO.getStaffIds() == null
                                ? new ArrayList<>()
                                : userRepository.findAllById(assignBuildingDTO.getStaffIds());
                building.setUser(staffs);
                return buildingRepository.save(building);
        }
}
