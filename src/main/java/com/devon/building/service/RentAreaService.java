package com.devon.building.service;

import com.devon.building.entity.Building;

public interface RentAreaService {

    void saveByBuilding(Building building, String rentAreaValues);

    void replaceByBuilding(Building building, String rentAreaValues);

    void deleteByBuildingId(Long buildingId);
}
