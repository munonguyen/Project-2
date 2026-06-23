package com.devon.building.api.admin;

import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.entity.Building;
import com.devon.building.service.BuildingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingAPI {
    private final BuildingService buildingService;

    public BuildingAPI(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/{id}/staffs")
    public ResponseEntity<ResponseDTO> loadStaffs(@PathVariable Long id) {
        return ResponseEntity.ok().body(buildingService.loadStaffs(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createBuilding(@RequestBody @Validated(BuildingDTO.Create.class) BuildingDTO building, BindingResult bindingResult){
        ResponseDTO response = new ResponseDTO();
        if(bindingResult.hasErrors()){
            response.setMessage("Dữ liệu không hợp lệ");
            List<String> details = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() +": " + error.getDefaultMessage()).toList();
            response.setDetail(details);
            return ResponseEntity.badRequest().body(response);
        }
        Building savedBuilding  = buildingService.saveBuilding(building);
        response.setMessage("Tạo tòa nhà thành công");
        response.setData(savedBuilding.getId());
        return ResponseEntity.ok().body(response);

    }
    @PutMapping
    public ResponseEntity<ResponseDTO> updateBuilding(@RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult){
        ResponseDTO responseDTO = new ResponseDTO();
        if(bindingResult.hasErrors()){
            responseDTO.setMessage("Dữ liệu không hợp lệ");
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();
            responseDTO.setDetail(errors);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        if(buildingDTO.getId() == null){
            responseDTO.setMessage("Phải chọn tòa nhà cần cập nhật");
            return ResponseEntity.badRequest().body(responseDTO);
        }
        Building savedBuilding = buildingService.saveBuilding(buildingDTO);
        responseDTO.setMessage("Cập nhật tòa nhà thành công");
        responseDTO.setData(savedBuilding.getId());
        return ResponseEntity.ok().body(responseDTO);
    }
    @DeleteMapping("/{ids}")
    public ResponseEntity<ResponseDTO> deleteBuilding(@PathVariable List<Long> ids){
        ResponseDTO responseDTO = new ResponseDTO();
        if (ids == null || ids.isEmpty()) {
            responseDTO.setMessage("Không có ID tòa nhà nào được cung cấp");
            return ResponseEntity.badRequest().body(responseDTO);
        }

        buildingService.deleteBuilding(ids);
        responseDTO.setMessage("Xóa thành công");
        return ResponseEntity.ok(responseDTO);
    }
    @PutMapping("/assign")
    public ResponseEntity<ResponseDTO> assignBuilding(@RequestBody @Valid AssignBuildingDTO assignBuildingDTO, BindingResult bindingResult){
        ResponseDTO response = new ResponseDTO();
        if(bindingResult.hasErrors()){
            response.setMessage("Dữ liệu không hợp lệ");
            List<String> details = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() +": " + error.getDefaultMessage()).toList();
            response.setDetail(details);
            return ResponseEntity.badRequest().body(response);
        }
        Building savedAssignBuilding = buildingService.saveAssignBuilding(assignBuildingDTO);
        response.setMessage("Giao tòa nhà thành công");
        response.setData(savedAssignBuilding.getId());
        return ResponseEntity.ok().body(response);

    }
}
