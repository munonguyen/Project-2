package com.devon.building.repository;

import com.devon.building.entity.Building;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BuildingRepository
    extends JpaRepository<Building, Long>, JpaSpecificationExecutor<Building> {
  Optional<Building> findById(Long id);
}
