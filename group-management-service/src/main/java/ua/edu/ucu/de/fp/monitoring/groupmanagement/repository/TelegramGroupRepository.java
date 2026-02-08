package ua.edu.ucu.de.fp.monitoring.groupmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.edu.ucu.de.fp.monitoring.groupmanagement.model.TelegramGroup;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

@Repository
public interface TelegramGroupRepository extends JpaRepository<TelegramGroup, Long> {
    
    @Query(value = "SELECT * FROM telegram_groups WHERE ST_Within(location, :zone)", nativeQuery = true)
    List<TelegramGroup> findGroupsWithinZone(@Param("zone") Polygon zone);
}
