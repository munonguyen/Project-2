package com.devon.building.api.admin;

import com.devon.building.entity.Building;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.AssignmentBuildingService;
import com.devon.building.service.BuildingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingAPI {

    private final BuildingService buildingService;
    private final AssignmentBuildingService assignmentBuildingService;

    public BuildingAPI(BuildingService buildingService, AssignmentBuildingService assignmentBuildingService) {
        this.buildingService = buildingService;
        this.assignmentBuildingService = assignmentBuildingService;
    }

    @GetMapping("/{id}/staffs")
    public ResponseEntity<ResponseDTO> loadStaffs(@PathVariable Long id) {
        return ResponseEntity.ok().body(assignmentBuildingService.loadStaffs(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createBuilding(
            @RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult) {

        ResponseDTO response = new ResponseDTO();

        if (bindingResult.hasErrors()) {
            response.setMessage("Dữ liệu không hợp lệ");
            List<String> details = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            response.setDetail(details);
            return ResponseEntity.badRequest().body(response);
        }

        Building savedBuilding = buildingService.createBuilding(buildingDTO);
        response.setMessage("Tạo tòa nhà thành công");
        response.setData(savedBuilding.getId());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateBuilding(
            @RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult) {

        ResponseDTO response = new ResponseDTO();

        if (bindingResult.hasErrors()) {
            response.setMessage("Dữ liệu không hợp lệ");
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            response.setDetail(errors);
            return ResponseEntity.badRequest().body(response);
        }

        if (buildingDTO.getId() == null) {
            response.setMessage("Phải chọn tòa nhà cần cập nhật");
            return ResponseEntity.badRequest().body(response);
        }

        Building savedBuilding = buildingService.updateBuilding(buildingDTO);
        response.setMessage("Cập nhật tòa nhà thành công");
        response.setData(savedBuilding.getId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{ids}")
    public ResponseEntity<ResponseDTO> deleteBuilding(@PathVariable List<Long> ids) {
        ResponseDTO response = new ResponseDTO();

        if (ids == null || ids.isEmpty()) {
            response.setMessage("Không có ID tòa nhà nào được cung cấp");
            return ResponseEntity.badRequest().body(response);
        }

        buildingService.deleteBuilding(ids);
        response.setMessage("Xóa thành công");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/assign")
    public ResponseEntity<ResponseDTO> assignBuilding(
            @RequestBody @Valid AssignBuildingDTO assignBuildingDTO, BindingResult bindingResult) {

        ResponseDTO response = new ResponseDTO();

        if (bindingResult.hasErrors()) {
            response.setMessage("Dữ liệu không hợp lệ");
            List<String> details = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            response.setDetail(details);
            return ResponseEntity.badRequest().body(response);
        }

        Building savedAssignBuilding = assignmentBuildingService.saveAssignBuilding(assignBuildingDTO);
        response.setMessage("Giao tòa nhà thành công");
        response.setData(savedAssignBuilding.getId());
        return ResponseEntity.ok().body(response);
    }
}
