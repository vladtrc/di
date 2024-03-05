package org.example;

import java.util.function.Supplier;

public class YaWeatherProvider implements Supplier<WeatherService> {
    @Override
    public WeatherService get() {
        return new YaWeatherService();
    }

    static public class YaWeatherService implements WeatherService {

        @Override
        public String getWeather() {
            return "ya weather";
        }
    }
}
