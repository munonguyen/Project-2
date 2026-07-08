package com.devon.building.api.admin;

import com.devon.building.entity.Building;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.response.StaffResponseDTO;
import com.devon.building.service.BuildingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buildings")
public class BuildingAPI {

  private final BuildingService buildingService;

  @GetMapping("/{id}/staffs")
  public ResponseEntity<ResponseDTO<List<StaffResponseDTO>>> loadStaffs(@PathVariable Long id) {
    return ResponseEntity.ok(buildingService.loadStaffs(id));
  }

  @PostMapping
  public ResponseEntity<ResponseDTO<Long>> createBuilding(
      @RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return validationError(bindingResult);
    }

    Building savedBuilding = buildingService.createBuilding(buildingDTO);
    ResponseDTO<Long> response = new ResponseDTO<>();
    response.setMessage("Tạo tòa nhà thành công");
    response.setData(savedBuilding.getId());
    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<ResponseDTO<Long>> updateBuilding(
      @RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return validationError(bindingResult);
    }

    ResponseDTO<Long> response = new ResponseDTO<>();
    if (buildingDTO.getId() == null) {
      response.setMessage("Phải chọn tòa nhà cần cập nhật");
      return ResponseEntity.badRequest().body(response);
    }

    Building savedBuilding = buildingService.updateBuilding(buildingDTO);
    response.setMessage("Cập nhật tòa nhà thành công");
    response.setData(savedBuilding.getId());
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{ids}")
  public ResponseEntity<ResponseDTO<Void>> deleteBuilding(@PathVariable List<Long> ids) {
    ResponseDTO<Void> response = new ResponseDTO<>();

    if (ids == null || ids.isEmpty()) {
      response.setMessage("Không có ID tòa nhà nào được cung cấp");
      return ResponseEntity.badRequest().body(response);
    }

    buildingService.deleteBuilding(ids);
    response.setMessage("Xóa thành công");
    return ResponseEntity.ok(response);
  }

  @PutMapping("/assign")
  public ResponseEntity<ResponseDTO<Long>> assignBuilding(
      @RequestBody @Valid AssignBuildingDTO assignBuildingDTO, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return validationError(bindingResult);
    }

    Building savedAssignBuilding = buildingService.saveAssignBuilding(assignBuildingDTO);
    ResponseDTO<Long> response = new ResponseDTO<>();
    response.setMessage("Giao tòa nhà thành công");
    response.setData(savedAssignBuilding.getId());
    return ResponseEntity.ok(response);
  }

  private static <T> ResponseEntity<ResponseDTO<T>> validationError(BindingResult bindingResult) {
    ResponseDTO<T> response = new ResponseDTO<>();
    response.setMessage("Dữ liệu không hợp lệ");
    response.setDetail(
        bindingResult.getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList());
    return ResponseEntity.badRequest().body(response);
  }
}
