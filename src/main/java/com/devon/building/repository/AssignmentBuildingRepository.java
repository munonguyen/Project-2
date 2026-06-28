package com.devon.building.repository;

import com.devon.building.entity.AssignmentBuilding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentBuildingRepository extends JpaRepository<AssignmentBuilding, Long> {

    @Modifying
    @Query("DELETE FROM AssignmentBuilding a WHERE a.building.id = ?1")
    void deleteByBuildingId(Long buildingId);
}
