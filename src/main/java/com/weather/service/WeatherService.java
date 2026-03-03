package com.weather.service;

import com.weather.config.AppConfig;
import com.weather.dto.ForecastResponse;
import com.weather.dto.WeatherResponse;
import com.weather.model.WeatherSearch;
import com.weather.repository.WeatherSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate           restTemplate;
    private final AppConfig              appConfig;
    private final WeatherSearchRepository searchRepository;

    /**
     * Fetch current weather for a city from OpenWeatherMap.
     * URL pattern: https://api.openweathermap.org/data/2.5/weather?q={city}&appid={key}&units=metric
     */
    public WeatherResponse getWeatherByCity(String city) {
        String url = String.format(
            "%s/weather?q=%s&appid=%s&units=%s",
            appConfig.getBaseUrl(), city,
            appConfig.getApiKey(), appConfig.getUnits()
        );
        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            if (response != null) saveWeatherSearch(response);
            return response;
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("City not found: \"" + city + "\". Please check the spelling.");
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Invalid API key. Please update application.properties.");
        } catch (Exception e) {
            throw new RuntimeException("Could not fetch weather data: " + e.getMessage());
        }
    }

    /**
     * Fetch 5-day / 3-hour forecast.
     * URL pattern: https://api.openweathermap.org/data/2.5/forecast?q={city}&appid={key}&units=metric&cnt=40
     */
    public ForecastResponse getForecastByCity(String city) {
        String url = String.format(
            "%s/forecast?q=%s&appid=%s&units=%s&cnt=40",
            appConfig.getBaseUrl(), city,
            appConfig.getApiKey(), appConfig.getUnits()
        );
        try {
            return restTemplate.getForObject(url, ForecastResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not fetch forecast: " + e.getMessage());
        }
    }

    private void saveWeatherSearch(WeatherResponse w) {
        try {
            WeatherSearch s = new WeatherSearch();
            s.setCityName(w.getName());
            s.setTemperature(w.getMain().getTemp());
            s.setDescription(w.getWeatherDescription());
            s.setHumidity(w.getMain().getHumidity());
            s.setWindSpeed(w.getWind() != null ? w.getWind().getSpeed() : null);
            searchRepository.save(s);
        } catch (Exception ignored) {
            // Never break the main flow for history logging
        }
    }

    public List<WeatherSearch> getRecentSearches() {
        return searchRepository.findTop5ByOrderBySearchedAtDesc();
    }
}
