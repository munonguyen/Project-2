package com.devon.building.api.admin;


import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingAPI {

    private final BuildingService buildingService;

    @GetMapping("/{id}/staff")
    public ResponseEntity<ResponseDTO> loadStaffs(@PathVariable Long id){
        return ResponseEntity.ok(buildingService.loadStaffs(id));
    }
    @PostMapping
    public ResponseEntity<ResponseDTO> createBuilding(@RequestBody @Valid BuildingDTO buildingDTO) {
        return ResponseEntity.ok().body(buildingService.createBuilding(buildingDTO));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateBuilding(@RequestBody @Valid BuildingDTO buildingDTO) {
        return ResponseEntity.ok().body(buildingService.updateBuilding(buildingDTO));
    }


    @DeleteMapping("/{ids}")
    public ResponseEntity<ResponseDTO> deleteBuilding(@PathVariable List<Long> ids) {
        return ResponseEntity.ok().body(buildingService.deleteBuilding(ids));
    }

    @PutMapping("/assign")
    public ResponseEntity<ResponseDTO> assignBuilding(@RequestBody @Valid AssignBuildingDTO assignBuildingDTO){
        return ResponseEntity.ok().body(buildingService.assignmentBuilding(assignBuildingDTO));
    }
}
