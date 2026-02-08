package ua.edu.ucu.de.fp.monitoring.notification.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ua.edu.ucu.de.fp.monitoring.notification.model.Notification;

@Repository
public interface NotificationRepository extends ReactiveCrudRepository<Notification, Long> {
    
    Flux<Notification> findAllByOrderByTimestampDesc();
}
