package org.example;

import java.util.function.Supplier;

public class GoWeatherProvider implements Supplier<WeatherService> {
    @Override
    public WeatherService get() {
        return new GoWeatherService();
    }


    static public class GoWeatherService implements WeatherService {

        @Override
        public String getWeather() {
            return "go weather";
        }
    }
}
