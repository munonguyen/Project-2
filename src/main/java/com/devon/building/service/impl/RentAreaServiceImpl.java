package com.devon.building.service.impl;

import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.repository.RentAreaRepository;
import com.devon.building.service.RentAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RentAreaServiceImpl implements RentAreaService {

    private final RentAreaRepository rentAreaRepository;

    @Override
    public void saveByBuilding(Building building, String rentAreaValues) {
        if (rentAreaValues == null || rentAreaValues.isBlank()) {
            return;
        }

        List<RentArea> rentAreas = new ArrayList<>();
        Arrays.stream(rentAreaValues.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(Long::parseLong)
                .forEach(value -> {
                    RentArea rentArea = new RentArea();
                    rentArea.setValue(value);
                    rentArea.setBuilding(building);
                    rentArea.setCreatedDate(LocalDate.now());
                    rentAreas.add(rentArea);
                });
        if (!rentAreas.isEmpty()) {
            rentAreaRepository.saveAll(rentAreas);
        }
    }

    @Override
    public void replaceByBuilding(Building building, String rentAreaValues) {
        deleteByBuildingId(building.getId());
        saveByBuilding(building, rentAreaValues);
    }

    @Override
    public void deleteByBuildingId(Long buildingId) {
        rentAreaRepository.deleteByBuildingId(buildingId);
    }

    @Override
    public void deleteByBuildingIds(List<Long> buildingIds) {
        rentAreaRepository.deleteByBuildingIdIn(buildingIds);
    }
}
