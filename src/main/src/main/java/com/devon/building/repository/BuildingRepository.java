package com.devon.building.repository;

import com.devon.building.entity.Building;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findById(Long id);
}
