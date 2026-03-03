package com.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponse {

    @JsonProperty("list")
    private List<ForecastItem> list;

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastItem {

        @JsonProperty("dt_txt")  private String dtTxt;
        @JsonProperty("main")    private WeatherResponse.Main main;
        @JsonProperty("weather") private List<WeatherResponse.Weather> weather;
        @JsonProperty("wind")    private WeatherResponse.Wind wind;

        public String getIconUrl() {
            if (weather != null && !weather.isEmpty())
                return "https://openweathermap.org/img/wn/" + weather.get(0).getIcon() + "@2x.png";
            return "";
        }

        public String getWeatherDescription() {
            if (weather == null || weather.isEmpty()) return "";
            String d = weather.get(0).getDescription();
            return d.substring(0, 1).toUpperCase() + d.substring(1);
        }
    }
}
