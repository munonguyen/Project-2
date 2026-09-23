package com.devon.building.service.impl;

import com.devon.building.converter.RentAreaConverter;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.RentAreaEntity;
import com.devon.building.service.RentAreaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RentAreaServiceImpl implements RentAreaService {
    private final RentAreaConverter rentAreaConverter;

    @Override
    @Transactional
    public void saveOrUpdateRentArea(BuildingEntity buildingEntity, String rentArea) {
        if(buildingEntity.getId() != null){
            buildingEntity.getRentArea().clear();
        }
        if (rentArea == null || rentArea.isBlank()) return;
        String[] areas = rentArea.split(",");
        for(String value : areas){
            RentAreaEntity rentAreaEntity = rentAreaConverter.toRentAreaEntity(buildingEntity, Long.valueOf(value.trim()));
            buildingEntity.getRentArea().add(rentAreaEntity);
        }
    }
}
