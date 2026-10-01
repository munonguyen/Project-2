package com.devon.building.repository.impl;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.repository.custom.BuildingRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Primary
public class BuildingRepositoryImpl implements BuildingRepositoryCustom {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    @Override
    public PaginationResult<BuildingEntity> findALlBuilding(
            BuildingSearchBuilder search,
            int page,
            int maxPageItem,
            int maxNavigationPage
    ) {
        StringBuilder from = new StringBuilder(" FROM building b ");
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> parameters = new LinkedHashMap<>();

        if (search.getStaffId() != null) {
            from.append(" INNER JOIN assignmentbuilding ab ON b.id = ab.buildingid ");
            where.append(" AND ab.staffid = :staffId ");
            parameters.put("staffId", search.getStaffId());
        }

        addLike(where, parameters, "name", "b.name", search.getName());
        addEquals(where, parameters, "floorArea", "b.floorarea", search.getFloorArea());
        addLike(where, parameters, "ward", "b.ward", search.getWard());
        addLike(where, parameters, "street", "b.street", search.getStreet());
        addEquals(where, parameters, "numberOfBasement", "b.numberofbasement", search.getNumberOfBasement());
        addLike(where, parameters, "direction", "b.direction", search.getDirection());
        addLike(where, parameters, "level", "b.level", search.getLevel());
        addLike(where, parameters, "managerName", "b.managername", search.getManagerName());
        addLike(where, parameters, "managerPhone", "b.managerphone", search.getManagerPhone());

        if (search.getDistrict() != null && !search.getDistrict().isBlank()) {
            where.append(" AND b.district = :district ");
            parameters.put("district", search.getDistrict());
        }

        if (search.getRentPriceFrom() != null) {
            where.append(" AND b.rentprice >= :rentPriceFrom ");
            parameters.put("rentPriceFrom", search.getRentPriceFrom());
        }
        if (search.getRentPriceTo() != null) {
            where.append(" AND b.rentprice <= :rentPriceTo ");
            parameters.put("rentPriceTo", search.getRentPriceTo());
        }

        appendRentAreaFilter(search, where, parameters);
        appendTypeFilter(search.getTypeCode(), where, parameters);

        String dataSql = "SELECT DISTINCT b.*" + from + where + " ORDER BY b.id DESC ";
        String countSql = "SELECT COUNT(DISTINCT b.id)" + from + where;

        Query dataQuery = entityManager.createNativeQuery(dataSql, BuildingEntity.class);
        Query countQuery = entityManager.createNativeQuery(countSql);
        bind(dataQuery, parameters);
        bind(countQuery, parameters);

        int totalRecords = ((Number) countQuery.getSingleResult()).intValue();
        return new PaginationResult<>(
                dataQuery,
                BuildingEntity.class,
                totalRecords,
                page,
                maxPageItem,
                maxNavigationPage
        );
    }

    private void appendRentAreaFilter(
            BuildingSearchBuilder search,
            StringBuilder where,
            Map<String, Object> parameters
    ) {
        if (search.getAreaFrom() == null && search.getAreaTo() == null) {
            return;
        }

        where.append(" AND EXISTS (SELECT 1 FROM rentarea ra WHERE ra.buildingid = b.id ");
        if (search.getAreaFrom() != null) {
            where.append(" AND ra.value >= :areaFrom ");
            parameters.put("areaFrom", search.getAreaFrom());
        }
        if (search.getAreaTo() != null) {
            where.append(" AND ra.value <= :areaTo ");
            parameters.put("areaTo", search.getAreaTo());
        }
        where.append(") ");
    }

    private void appendTypeFilter(
            List<String> typeCodes,
            StringBuilder where,
            Map<String, Object> parameters
    ) {
        if (typeCodes == null || typeCodes.isEmpty()) {
            return;
        }

        where.append(" AND (");
        for (int i = 0; i < typeCodes.size(); i++) {
            if (i > 0) {
                where.append(" OR ");
            }
            String parameterName = "typeCode" + i;
            where.append("b.type LIKE :").append(parameterName);
            parameters.put(parameterName, "%" + typeCodes.get(i) + "%");
        }
        where.append(") ");
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

    private void addEquals(
            StringBuilder where,
            Map<String, Object> parameters,
            String parameterName,
            String column,
            Number value
    ) {
        if (value == null) {
            return;
        }
        where.append(" AND ").append(column).append(" = :").append(parameterName).append(' ');
        parameters.put(parameterName, value);
    }

    private void bind(Query query, Map<String, Object> parameters) {
        parameters.forEach(query::setParameter);
    }
}
