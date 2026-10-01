package com.devon.building.controller;

import com.devon.building.entity.Customer;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContactApiController {

    private final CustomerService customerService;

    @PostMapping("/contact")
    public Customer sendDemand(@RequestBody @Valid CustomerDTO customerDTO) {
        return customerService.sendDemand(customerDTO);
    }
}
