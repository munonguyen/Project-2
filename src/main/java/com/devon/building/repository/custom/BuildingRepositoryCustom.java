package com.devon.building.repository.custom;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.pagination.PaginationResult;

import java.util.List;

public interface BuildingRepositoryCustom {
    PaginationResult<BuildingEntity> findALlBuilding(BuildingSearchBuilder buildingSearchBuilder , int page, int maxPageItem, int maxNavigationPage);
}
