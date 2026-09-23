package com.devon.building.converter;

import com.devon.building.builder.CustomerSearchBuilder;
import com.devon.building.entity.Customer;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerSearchResponse;
import com.devon.building.pagination.PaginationResult;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerConverter {

    private final ModelMapper modelMapper;

    public Customer toCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        if (customer.getDemand() == null && customerDTO.getDemand() != null) {
            customer.setDemand(customerDTO.getDemand());
        }
        return customer;
    }

    public CustomerSearchBuilder toCustomerSearchBuilder(CustomerSearchRequest request) {
        return CustomerSearchBuilder.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .staffId(request.getStaffId())
                .status(request.getStatus())
                .build();
    }

    public CustomerSearchResponse toCustomerSearchResponse(Customer customer) {
        return modelMapper.map(customer, CustomerSearchResponse.class);
    }

    public PaginationResult<CustomerSearchResponse> toPaginationResult(PaginationResult<Customer> entityPagination) {
        return entityPagination.map(this::toCustomerSearchResponse);
    }

    public void updateCustomerEntity(CustomerDTO customerDTO, Customer customer) {
        modelMapper.map(customerDTO, customer);
    }

    public CustomerDTO toCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }
}

