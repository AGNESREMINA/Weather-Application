package com.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private String name;

    @JsonProperty("main")    private Main    main;
    @JsonProperty("weather") private List<Weather> weather;
    @JsonProperty("wind")    private Wind    wind;
    @JsonProperty("sys")     private Sys     sys;
    @JsonProperty("coord")   private Coord   coord;
    @JsonProperty("visibility") private Integer visibility;

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private Double temp;
        @JsonProperty("feels_like") private Double feelsLike;
        @JsonProperty("temp_min")   private Double tempMin;
        @JsonProperty("temp_max")   private Double tempMax;
        private Integer pressure;
        private Integer humidity;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String main;
        private String description;
        private String icon;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        private Double  speed;
        private Integer deg;
        private Double  gust;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        private String country;
        private Long   sunrise;
        private Long   sunset;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coord {
        private Double lon;
        private Double lat;
    }

    // ── Convenience helpers used in Thymeleaf ────────────
    public String getIconUrl() {
        if (weather != null && !weather.isEmpty())
            return "https://openweathermap.org/img/wn/" + weather.get(0).getIcon() + "@2x.png";
        return "";
    }

    public String getWeatherMain() {
        return (weather != null && !weather.isEmpty()) ? weather.get(0).getMain() : "Unknown";
    }

    public String getWeatherDescription() {
        if (weather == null || weather.isEmpty()) return "No data";
        String d = weather.get(0).getDescription();
        return d.substring(0, 1).toUpperCase() + d.substring(1);
    }
}
