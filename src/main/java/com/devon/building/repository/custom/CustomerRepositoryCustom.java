package com.devon.building.repository.custom;

import com.devon.building.builder.CustomerSearchBuilder;
import com.devon.building.entity.Customer;
import com.devon.building.pagination.PaginationResult;

public interface CustomerRepositoryCustom {
    PaginationResult<Customer> findAllCustomer(CustomerSearchBuilder customerSearchBuilder, int page, int maxPageItem, int maxNavigationPage);
}
