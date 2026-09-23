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

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class BuildingRepositoryImpl implements BuildingRepositoryCustom {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    private void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder sql) {
        Long staffId = buildingSearchBuilder.getStaffId();
        if(staffId != null) {
            sql.append(" INNER JOIN assignmentbuilding ON b.id = assignmentbuilding.buildingid ");
        }
    }
    private void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
        try {
            Field[] field = BuildingSearchBuilder.class.getDeclaredFields();
            for(Field item : field) {
                item.setAccessible(true);
                String fieldName = item.getName();
                if(!fieldName.equals("staffId") && !fieldName.equals("district") && !fieldName.equals("typeCode") &&
                        !fieldName.startsWith("area") && !fieldName.startsWith("rentPrice")) {
                    Object value= item.get(buildingSearchBuilder);
                    if(value != null && !value.toString().isBlank()) {
                        if(value.toString().matches("\\d+(\\.\\d+)?$")) {
                            where.append(" AND b.")
                                    .append(fieldName.toLowerCase())
                                    .append(" = ")
                                    .append(value);
                        } else {
                            where.append(" AND b.")
                                    .append(fieldName.toLowerCase())
                                    .append(" LIKE '%")
                                    .append(value)
                                    .append("%'");
                        }
                    }
                }
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    private void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
        Long staffId = buildingSearchBuilder.getStaffId();
        if(staffId != null) {
            where.append(" AND assignmentbuilding.staffid = ").append(staffId);
        }

        Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
        Long rentAreaTo = buildingSearchBuilder.getAreaTo();
        if(rentAreaFrom != null || rentAreaTo != null) {
            where.append(" AND EXISTS  (SELECT * FROM rentarea WHERE b.id = rentarea.buildingid ");
            if(rentAreaFrom != null) {
                where.append(" AND rentarea.value >= ").append(rentAreaFrom);
            }
            if(rentAreaTo != null) {
                where.append(" AND rentarea.value <= ").append(rentAreaTo);
            }
            where.append(") ");
        }


        Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
        Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
        if(rentPriceFrom != null) {
            where.append(" AND b.rentprice >= ").append(rentPriceFrom);
        }
        if(rentPriceTo != null) {
            where.append(" AND b.rentprice <= ").append(rentPriceTo);
        }

        if(buildingSearchBuilder.getTypeCode() != null && !buildingSearchBuilder.getTypeCode().isEmpty()) {
            where.append(" AND (");

            where.append(buildingSearchBuilder.getTypeCode()
                    .stream()
                    .map(type -> "b.type LIKE '%" + type + "%'")
                    .collect(Collectors.joining(" OR "))).append(") ");
        }
        if (buildingSearchBuilder.getDistrict() != null
                && !buildingSearchBuilder.getDistrict().isBlank()) {
            where.append(" AND b.district = '")
                    .append(buildingSearchBuilder.getDistrict())
                    .append("'");
        }

    }
    @Override
    public PaginationResult<BuildingEntity> findALlBuilding(BuildingSearchBuilder buildingSearchBuilder, int page, int maxPageItem, int maxNavigationPage) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT b.* FROM building b ");
        joinTable(buildingSearchBuilder, sql);
        StringBuilder where = new StringBuilder(" Where 1 = 1 ");
        queryNormal(buildingSearchBuilder, where);
        querySpecial(buildingSearchBuilder, where);
        sql.append(where);
        Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
        return new PaginationResult<>(query, query.getResultList().size(), page, maxPageItem, maxNavigationPage);
    }
}
