package com.devon.building.converter;

import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.RentAreaEntity;
import org.springframework.stereotype.Component;

@Component
public class RentAreaConverter {
    public RentAreaEntity toRentAreaEntity(BuildingEntity buildingEntity, Long value){
        RentAreaEntity rentAreaEntity = new RentAreaEntity();
        rentAreaEntity.setValue(value);
        rentAreaEntity.setBuilding(buildingEntity);
        return rentAreaEntity;
    }
}
