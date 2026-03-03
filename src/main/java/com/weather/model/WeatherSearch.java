package com.weather.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_searches")
@Data
@NoArgsConstructor
public class WeatherSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cityName;

    @Column private Double temperature;
    @Column private String description;
    @Column private Integer humidity;
    @Column private Double windSpeed;

    @Column(name = "searched_at")
    private LocalDateTime searchedAt;

    @PrePersist
    protected void onCreate() { searchedAt = LocalDateTime.now(); }
}
