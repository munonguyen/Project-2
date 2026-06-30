package com.devon.building.repository;

import com.devon.building.entity.AssignmentBuilding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentBuildingRepository extends JpaRepository<AssignmentBuilding, Long> {

    @Modifying
    @Query("DELETE FROM AssignmentBuilding a WHERE a.building.id = ?1")
    void deleteByBuildingId(Long buildingId);

    @Modifying
    @Query("DELETE FROM AssignmentBuilding a WHERE a.building.id IN ?1")
    void deleteByBuildingIdIn(List<Long> buildingIds);
}
