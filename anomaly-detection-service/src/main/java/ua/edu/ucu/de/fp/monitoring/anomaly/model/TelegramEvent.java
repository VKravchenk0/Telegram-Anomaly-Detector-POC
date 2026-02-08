package ua.edu.ucu.de.fp.monitoring.anomaly.model;

import java.time.LocalDateTime;

// Immutable records for functional programming
public record TelegramEvent(
    String groupName,
    String groupLink,
    Double latitude,
    Double longitude,
    String content,
    LocalDateTime timestamp
) {}
