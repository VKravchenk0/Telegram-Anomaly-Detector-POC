package ua.edu.ucu.de.fp.monitoring.admin.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

@Entity
@Table(name = "zone_of_interest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneOfInterest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Zone is required")
    @Column(columnDefinition = "geometry(Polygon,4326)", nullable = false)
    private Polygon zone;
    
    @Column(nullable = false)
    private boolean active = true;
    
    // Functional helper
    public ZoneOfInterest withZone(Polygon zone) {
        return new ZoneOfInterest(this.id, zone, this.active);
    }
    
    public ZoneOfInterest activate() {
        return new ZoneOfInterest(this.id, this.zone, true);
    }
    
    public ZoneOfInterest deactivate() {
        return new ZoneOfInterest(this.id, this.zone, false);
    }
}
