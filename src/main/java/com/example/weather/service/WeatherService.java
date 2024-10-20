package com.example.weather.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.cdimascio.dotenv.Dotenv;


@Service
public class WeatherService {

    @Autowired
    private Dotenv dotenv;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Cacheable(value = "weather", key = "#location")
    public String getWeather(String location) {

        String apiKey = dotenv.get("VISUAL_CROSSING_API_KEY");
        String apiUrl = dotenv.get("VISUAL_CROSSING_API_URL");

        String url = apiUrl + location + "?key=" + apiKey;

        String cachedWeather = redisTemplate.opsForValue().get(location);
        if (cachedWeather != null) {
            System.out.println("Returning cached data for location: " + location);
            return cachedWeather;
        }

        String response = restTemplate.getForObject(url, String.class);

        redisTemplate.opsForValue().set(location, response, 1, TimeUnit.HOURS);

        return response;
    }

    @CachePut(value = "weather", key = "#location")
    public String updateWeatherCache(String location) {
        return getWeather(location);
    }
}
