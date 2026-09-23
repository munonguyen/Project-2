package com.devon.building.service;

import com.devon.building.entity.BuildingEntity;


public interface RentAreaService {

    void saveOrUpdateRentArea(BuildingEntity buildingEntity, String rentArea);
}
