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

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
@Primary
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    @Override
    public PaginationResult<Customer> findAllCustomer(
            CustomerSearchBuilder search,
            int page,
            int maxPageItem,
            int maxNavigationPage
    ) {
        StringBuilder from = new StringBuilder(" FROM customer c ");
        StringBuilder where = new StringBuilder(" WHERE c.is_active = 1 ");
        Map<String, Object> parameters = new LinkedHashMap<>();

        if (search.getStaffId() != null) {
            from.append(" INNER JOIN assignmentcustomer ac ON c.id = ac.customerid ");
            where.append(" AND ac.staffid = :staffId ");
            parameters.put("staffId", search.getStaffId());
        }

        addLike(where, parameters, "fullName", "c.fullname", search.getFullName());
        addLike(where, parameters, "phoneNumber", "c.phone", search.getPhoneNumber());
        addLike(where, parameters, "email", "c.email", search.getEmail());

        if (search.getStatus() != null) {
            where.append(" AND c.status = :status ");
            parameters.put("status", search.getStatus().name());
        }

        String dataSql = "SELECT DISTINCT c.*" + from + where + " ORDER BY c.createddate DESC, c.id DESC ";
        String countSql = "SELECT COUNT(DISTINCT c.id)" + from + where;

        Query dataQuery = entityManager.createNativeQuery(dataSql, Customer.class);
        Query countQuery = entityManager.createNativeQuery(countSql);
        bind(dataQuery, parameters);
        bind(countQuery, parameters);

        int totalRecords = ((Number) countQuery.getSingleResult()).intValue();
        return new PaginationResult<>(
                dataQuery,
                Customer.class,
                totalRecords,
                page,
                maxPageItem,
                maxNavigationPage
        );
    }

    private void addLike(
            StringBuilder where,
            Map<String, Object> parameters,
            String parameterName,
            String column,
            String value
    ) {
        if (value == null || value.isBlank()) {
            return;
        }
        where.append(" AND ").append(column).append(" LIKE :").append(parameterName).append(' ');
        parameters.put(parameterName, "%" + value.trim() + "%");
    }

    private void bind(Query query, Map<String, Object> parameters) {
        parameters.forEach(query::setParameter);
    }
}
