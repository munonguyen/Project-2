package com.devon.building.repository;

import com.devon.building.entity.Building;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.pagination.PaginationResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class BuildingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public PaginationResult<Building> queryBuilding(
            int page,
            int maxResult,
            int maxNavigationPage,
            BuildingSearchRequest buildingSearchRequest
    ) {
        StringBuilder sql = new StringBuilder("select distinct b from ")
                .append(Building.class.getName())
                .append(" b where 1 = 1");
        StringBuilder countSql = new StringBuilder("select count(distinct b.id) from ")
                .append(Building.class.getName())
                .append(" b where 1 = 1");

        Map<String, Object> parameters = new HashMap<>();
        appendConditions(sql, countSql, parameters, buildingSearchRequest);
        sql.append(" order by b.createdDate desc");

        TypedQuery<Building> query = entityManager.createQuery(sql.toString(), Building.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql.toString(), Long.class);

        parameters.forEach((key, value) -> {
            query.setParameter(key, value);
            countQuery.setParameter(key, value);
        });

        return new PaginationResult<>(query, countQuery, page, maxResult, maxNavigationPage);
    }

    private void appendConditions(
            StringBuilder sql,
            StringBuilder countSql,
            Map<String, Object> parameters,
            BuildingSearchRequest request
    ) {
        appendContains(sql, countSql, parameters, "b.name", "name", request.getName());
        appendEquals(sql, countSql, parameters, "b.floorArea", "floorArea", request.getFloorArea());
        appendEquals(sql, countSql, parameters, "b.district", "district", request.getDistrict());
        appendContains(sql, countSql, parameters, "b.ward", "ward", request.getWard());
        appendContains(sql, countSql, parameters, "b.street", "street", request.getStreet());
        appendNumberOfBasement(sql, countSql, parameters, request.getNumberOfBasement());
        appendEquals(sql, countSql, parameters, "b.direction", "direction", request.getDirection());
        appendEquals(sql, countSql, parameters, "b.level", "level", request.getLevel());
        appendRange(sql, countSql, parameters, "b.price", "rentPriceFrom", "rentPriceTo",
                request.getRentPriceFrom(), request.getRentPriceTo());
        appendContains(sql, countSql, parameters, "b.managerName", "managerName", request.getManagerName());
        appendContains(sql, countSql, parameters, "b.managerPhoneNumber", "managerPhoneNumber",
                request.getManagerPhoneNumber());
        appendTypeCodes(sql, countSql, parameters, request.getTypeCodes());
        appendStaff(sql, countSql, parameters, request.getStaffId());
        appendRentArea(sql, countSql, parameters, request.getRentAreaFrom(), request.getRentAreaTo());
    }

    private void appendContains(
            StringBuilder sql,
            StringBuilder countSql,
            Map<String, Object> parameters,
            String field,
            String parameterName,
            String value
    ) {
        if (value == null || value.isBlank()) {
            return;
        }
        String clause = " and lower(" + field + ") like :" + parameterName;
        sql.append(clause);
        countSql.append(clause);
        parameters.put(parameterName, "%" + value.trim().toLowerCase() + "%");
    }

    private void appendEquals(
            StringBuilder sql,
            StringBuilder countSql,
            Map<String, Object> parameters,
            String field,
            String parameterName,
            Object value
    ) {
        if (value == null || (value instanceof String stringValue && stringValue.isBlank())) {
            return;
        }
        String clause = " and " + field + " = :" + parameterName;
        sql.append(clause);
        countSql.append(clause);
        parameters.put(parameterName, value);
    }

    private void appendRange(
            StringBuilder sql,
            StringBuilder countSql,
            Map<String, Object> parameters,
            String field,
            String fromParameter,
            String toParameter,
            Long fromValue,
            Long toValue
    ) {
        if (fromValue != null) {
            String clause = " and " + field + " >= :" + fromParameter;
            sql.append(clause);
            countSql.append(clause);
            parameters.put(fromParameter, fromValue.doubleValue());
        }
        if (toValue != null) {
            String clause = " and " + field + " <= :" + toParameter;
            sql.append(clause);
            countSql.append(clause);
            parameters.put(toParameter, toValue.doubleValue());
        }
    }

    private void appendNumberOfBasement(
            StringBuilder sql,
            StringBuilder countSql,
            Map<String, Object> parameters,
            String numberOfBasement
    ) {
        if (numberOfBasement == null || numberOfBasement.isBlank()) {
            return;
        }
        try {
            int parsedBasement = Integer.parseInt(numberOfBasement.trim());
            String clause = " and b.numberOfBasement = :numberOfBasement";
            sql.append(clause);
            countSql.append(clause);
            parameters.put("numberOfBasement", parsedBasement);
        } catch (NumberFormatException e) {
            // Ignore invalid number format for search criteria
        }
    }

    private void appendTypeCodes(
            StringBuilder sql,
            StringBuilder countSql,
            Map<String, Object> parameters,
            List<String> typeCodes
    ) {
        if (typeCodes == null || typeCodes.isEmpty()) {
            return;
        }

        StringBuilder clause = new StringBuilder(" and (");
        for (int i = 0; i < typeCodes.size(); i++) {
            String parameterName = "typeCode" + i;
            if (i > 0) {
                clause.append(" or ");
            }
            clause.append("lower(b.type) like :").append(parameterName);
            parameters.put(parameterName, "%" + typeCodes.get(i).trim().toLowerCase() + "%");
        }
        clause.append(')');
        sql.append(clause);
        countSql.append(clause);
    }

    private void appendStaff(
            StringBuilder sql,
            StringBuilder countSql,
            Map<String, Object> parameters,
            Long staffId
    ) {
        if (staffId == null) {
            return;
        }
        String clause = " and exists (select 1 from b.staffs staff where staff.id = :staffId)";
        sql.append(clause);
        countSql.append(clause);
        parameters.put("staffId", staffId);
    }

    private void appendRentArea(
            StringBuilder sql,
            StringBuilder countSql,
            Map<String, Object> parameters,
            Long rentAreaFrom,
            Long rentAreaTo
    ) {
        if (rentAreaFrom == null && rentAreaTo == null) {
            return;
        }

        StringBuilder clause = new StringBuilder(" and exists (select 1 from b.rentAreas rentArea where 1 = 1");
        if (rentAreaFrom != null) {
            clause.append(" and rentArea.value >= :rentAreaFrom");
            parameters.put("rentAreaFrom", rentAreaFrom);
        }
        if (rentAreaTo != null) {
            clause.append(" and rentArea.value <= :rentAreaTo");
            parameters.put("rentAreaTo", rentAreaTo);
        }
        clause.append(')');
        sql.append(clause);
        countSql.append(clause);
    }
}
