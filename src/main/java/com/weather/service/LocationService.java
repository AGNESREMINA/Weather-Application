package com.weather.service;

import com.weather.model.Location;
import com.weather.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location saveLocation(String cityName, String country, Double lat, Double lon) {
        Optional<Location> existing = locationRepository.findByCityNameIgnoreCase(cityName);
        if (existing.isPresent()) {
            Location loc = existing.get();
            loc.setLastSearched(LocalDateTime.now());
            return locationRepository.save(loc);
        }
        Location location = new Location();
        location.setCityName(cityName);
        location.setCountry(country);
        location.setLatitude(lat);
        location.setLongitude(lon);
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations()      { return locationRepository.findAllByOrderByLastSearchedDesc(); }
    public List<Location> getFavoriteLocations() { return locationRepository.findByFavoriteTrueOrderByLastSearchedDesc(); }

    public Location toggleFavorite(Long id) {
        Location loc = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));
        loc.setFavorite(!loc.isFavorite());
        return locationRepository.save(loc);
    }

    public void deleteLocation(Long id) { locationRepository.deleteById(id); }
}
