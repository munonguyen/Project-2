package com.devon.building.service;

import com.devon.building.entity.Customer;
import com.devon.building.model.dto.AssignCustomerDTO;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerSearchResponse;
import com.devon.building.pagination.PaginationResult;

import java.util.List;

public interface CustomerService {
    Customer sendDemand(CustomerDTO customerDTO);

    PaginationResult<CustomerSearchResponse> findCustomer(CustomerSearchRequest customerSearchRequest, int page, int maxPageItem, int maxNavigationPage);

    ResponseDTO createCustomer(CustomerDTO customerDTO);

    ResponseDTO updateCustomer(CustomerDTO customerDTO);

    ResponseDTO deleteCustomer(List<Long> ids);

    CustomerDTO findById(Long id);

    ResponseDTO loadStaffs(Long customerId);

    ResponseDTO assignmentCustomer(AssignCustomerDTO assignCustomerDTO);
}
