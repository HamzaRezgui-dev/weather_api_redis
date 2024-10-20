package com.example.weather.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.weather.service.WeatherService;

@Controller
@RequestMapping("/api")
public class WeatherController {

  
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/{location}")
    public ResponseEntity<String> getWeather(@PathVariable String location) {
        String weatherData = weatherService.getWeather(location);
        return ResponseEntity.ok(weatherData);
    }
}
