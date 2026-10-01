package com.devon.building.repository.impl;

import com.devon.building.builder.CustomerSearchBuilder;
import com.devon.building.entity.Customer;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.repository.custom.CustomerRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    @Override
    public PaginationResult<Customer> findAllCustomer(CustomerSearchBuilder customerSearchBuilder, int page, int maxPageItem, int maxNavigationPage) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT c.* FROM customer c ");

        if (customerSearchBuilder.getStaffId() != null) {
            sql.append(" INNER JOIN assignmentcustomer ac ON c.id = ac.customerid ");
        }

        sql.append(" WHERE c.is_active = 1 ");

        if (customerSearchBuilder.getStaffId() != null) {
            sql.append(" AND ac.staffid = ").append(customerSearchBuilder.getStaffId()).append(" ");
        }
        if (customerSearchBuilder.getFullName() != null && !customerSearchBuilder.getFullName().isBlank()) {
            sql.append(" AND c.fullname LIKE '%").append(customerSearchBuilder.getFullName()).append("%' ");
        }
        if (customerSearchBuilder.getPhoneNumber() != null && !customerSearchBuilder.getPhoneNumber().isBlank()) {
            sql.append(" AND c.phone LIKE '%").append(customerSearchBuilder.getPhoneNumber()).append("%' ");
        }
        if (customerSearchBuilder.getEmail() != null && !customerSearchBuilder.getEmail().isBlank()) {
            sql.append(" AND c.email LIKE '%").append(customerSearchBuilder.getEmail()).append("%' ");
        }
        if (customerSearchBuilder.getStatus() != null) {
            sql.append(" AND c.status = '").append(customerSearchBuilder.getStatus().name()).append("' ");
        }

        sql.append(" ORDER BY c.createddate DESC, c.id DESC ");

        Query query = entityManager.createNativeQuery(sql.toString(), Customer.class);
        return new PaginationResult<>(query, query.getResultList().size(), page, maxPageItem, maxNavigationPage);
    }
}
