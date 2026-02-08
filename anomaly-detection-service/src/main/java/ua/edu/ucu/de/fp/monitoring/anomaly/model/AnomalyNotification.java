package ua.edu.ucu.de.fp.monitoring.anomaly.model;

import java.time.LocalDateTime;

public record AnomalyNotification(
    String groupName,
    String groupLink,
    Double latitude,
    Double longitude,
    String keyword,
    String content,
    LocalDateTime timestamp
) {
    public static AnomalyNotification fromEvent(TelegramEvent event, String keyword) {
        return new AnomalyNotification(
            event.groupName(),
            event.groupLink(),
            event.latitude(),
            event.longitude(),
            keyword,
            event.content(),
            event.timestamp()
        );
    }
}
