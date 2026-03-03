package com.weather.repository;

import com.weather.model.WeatherSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WeatherSearchRepository extends JpaRepository<WeatherSearch, Long> {
    List<WeatherSearch> findTop5ByOrderBySearchedAtDesc();
    List<WeatherSearch> findTop10ByCityNameIgnoreCaseOrderBySearchedAtDesc(String cityName);
}
