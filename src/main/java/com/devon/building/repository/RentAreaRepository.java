package com.devon.building.repository;

import com.devon.building.entity.RentArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentAreaRepository extends JpaRepository<RentArea, Long> {

    @Modifying
    @Query("DELETE FROM RentArea r WHERE r.building.id = ?1")
    void deleteByBuildingId(Long buildingId);

    @Modifying
    @Query("DELETE FROM RentArea r WHERE r.building.id IN ?1")
    void deleteByBuildingIdIn(List<Long> buildingIds);
}
