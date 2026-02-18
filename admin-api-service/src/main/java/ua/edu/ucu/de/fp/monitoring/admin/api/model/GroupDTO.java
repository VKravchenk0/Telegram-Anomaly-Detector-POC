package ua.edu.ucu.de.fp.monitoring.admin.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTOs using records for immutability (functional approach)
public class GroupDTO {
    
    public record TelegramGroupRequest(
            @NotBlank String name,
            @NotBlank String link,
            @NotNull Double latitude,
            @NotNull Double longitude
    ) {}
    
    public record TelegramGroupResponse(
            Long id,
            String name,
            String link,
            Double latitude,
            Double longitude
    ) {}
    
    public record ZoneRequest(
            @NotNull Double minLatitude,
            @NotNull Double minLongitude,
            @NotNull Double maxLatitude,
            @NotNull Double maxLongitude
    ) {}
    
    public record ZoneResponse(
            Long id,
            Double minLatitude,
            Double minLongitude,
            Double maxLatitude,
            Double maxLongitude,
            boolean active
    ) {}
}
