package com.devon.building.api.admin;

import com.devon.building.model.dto.AssignCustomerDTO;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerAPI {
    private final CustomerService customerService;


    @GetMapping("/{id}/staff")
    public ResponseEntity<ResponseDTO> loadStaffs(@PathVariable Long id){
        return ResponseEntity.ok(customerService.loadStaffs(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        return ResponseEntity.ok().body(customerService.createCustomer(customerDTO));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        return ResponseEntity.ok().body(customerService.updateCustomer(customerDTO));
    }


    @DeleteMapping("/{ids}")
    public ResponseEntity<ResponseDTO> deleteCustomer(@PathVariable List<Long> ids) {
        return ResponseEntity.ok().body(customerService.deleteCustomer(ids));
    }

    @PutMapping("/assign")
    public ResponseEntity<ResponseDTO> assignCustomer(@RequestBody @Valid AssignCustomerDTO assignCustomerDTO){
        return ResponseEntity.ok().body(customerService.assignmentCustomer(assignCustomerDTO));
    }

}
