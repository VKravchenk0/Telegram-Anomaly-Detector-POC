package ua.edu.ucu.de.fp.monitoring.admin.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.ucu.de.fp.monitoring.admin.api.model.ZoneOfInterest;

import java.util.Optional;

@Repository
public interface ZoneOfInterestRepository extends JpaRepository<ZoneOfInterest, Long> {
    
    Optional<ZoneOfInterest> findFirstByActiveTrue();
}
