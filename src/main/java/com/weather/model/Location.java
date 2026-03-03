package com.weather.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City name is required")
    @Column(nullable = false)
    private String cityName;

    @Column
    private String country;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(name = "is_favorite")
    private boolean favorite = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_searched")
    private LocalDateTime lastSearched;

    @PrePersist
    protected void onCreate() {
        createdAt     = LocalDateTime.now();
        lastSearched  = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastSearched = LocalDateTime.now();
    }
}
