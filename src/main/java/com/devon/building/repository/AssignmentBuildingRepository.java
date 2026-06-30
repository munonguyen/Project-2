package com.devon.building.repository;

import com.devon.building.entity.AssignmentBuilding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentBuildingRepository extends JpaRepository<AssignmentBuilding, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM AssignmentBuilding ab WHERE ab.building.id = ?1")
    void deleteByBuildingId(Long buildingId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM AssignmentBuilding ab WHERE ab.building.id IN ?1")
    void deleteByBuildingIdIn(List<Long> buildingIds);
}
