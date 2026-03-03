package com.weather.repository;

import com.weather.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCityNameIgnoreCase(String cityName);
    List<Location> findByFavoriteTrueOrderByLastSearchedDesc();
    List<Location> findAllByOrderByLastSearchedDesc();
    boolean existsByCityNameIgnoreCase(String cityName);
}
