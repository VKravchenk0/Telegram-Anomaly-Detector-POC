package ua.edu.ucu.de.fp.monitoring.groupmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "telegram_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Link is required")
    @Column(nullable = false)
    private String link;
    
    @NotNull(message = "Location is required")
    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point location;
    
    // Helper methods for functional transformations
    public TelegramGroup withName(String name) {
        return new TelegramGroup(this.id, name, this.link, this.location);
    }
    
    public TelegramGroup withLink(String link) {
        return new TelegramGroup(this.id, this.name, link, this.location);
    }
    
    public TelegramGroup withLocation(Point location) {
        return new TelegramGroup(this.id, this.name, this.link, location);
    }
}
