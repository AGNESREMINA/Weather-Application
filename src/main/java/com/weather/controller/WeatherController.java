package com.weather.controller;

import com.weather.dto.ForecastResponse;
import com.weather.dto.WeatherResponse;
import com.weather.service.LocationService;
import com.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService  weatherService;
    private final LocationService locationService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recentSearches",    weatherService.getRecentSearches());
        model.addAttribute("favoriteLocations", locationService.getFavoriteLocations());
        return "index";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model,
                             RedirectAttributes redirectAttributes) {
        if (city == null || city.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please enter a city name.");
            return "redirect:/";
        }
        try {
            WeatherResponse  weather  = weatherService.getWeatherByCity(city.trim());
            ForecastResponse forecast = weatherService.getForecastByCity(city.trim());

            // Auto-save the city to DB
            if (weather.getSys() != null && weather.getCoord() != null) {
                locationService.saveLocation(
                    weather.getName(),
                    weather.getSys().getCountry(),
                    weather.getCoord().getLat(),
                    weather.getCoord().getLon()
                );
            }

            // Filter to one entry per day (noon readings give best day summary)
            List<ForecastResponse.ForecastItem> daily = forecast.getList().stream()
                .filter(f -> f.getDtTxt().contains("12:00:00"))
                .limit(5).toList();

            model.addAttribute("weather",  weather);
            model.addAttribute("forecast", daily);
            return "weather";

        } catch (RuntimeException e) {
            model.addAttribute("error",        e.getMessage());
            model.addAttribute("searchedCity", city);
            model.addAttribute("recentSearches", weatherService.getRecentSearches());
            return "index";
        }
    }

    @GetMapping("/locations")
    public String savedLocations(Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("favorites", locationService.getFavoriteLocations());
        return "saved-locations";
    }

    @PostMapping("/locations/{id}/favorite")
    public String toggleFavorite(@PathVariable Long id) {
        locationService.toggleFavorite(id);
        return "redirect:/locations";
    }

    @PostMapping("/locations/{id}/delete")
    public String deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return "redirect:/locations";
    }

    // JSON REST endpoint (bonus)
    @GetMapping("/api/weather")
    @ResponseBody
    public WeatherResponse getWeatherApi(@RequestParam String city) {
        return weatherService.getWeatherByCity(city);
    }
}
