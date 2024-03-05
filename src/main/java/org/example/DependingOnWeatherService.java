package org.example;

public class DependingOnWeatherService {
    WeatherService weatherService;

    public DependingOnWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void tellWeather() {
        System.out.println(weatherService.getWeather());
    }
}
