package com.example.distanceservice.controller;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.cache.SimpleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryCityController {

    private final CityRepository cityRepository;
    private final SimpleCache simpleCache;

    @Autowired
    public CountryCityController(CityRepository cityRepository, SimpleCache simpleCache) {
        this.cityRepository = cityRepository;
        this.simpleCache = simpleCache;
    }

    @GetMapping("/api/cities-by-country")
    public List<City> getCitiesByCountry(@RequestParam String countryName) {
        String cacheKey = "cities_" + countryName;
        Object cached = simpleCache.get(cacheKey);
        if (cached instanceof List<?> cachedCities) {
            return (List<City>) cachedCities;
        }

        List<City> cities = cityRepository.findCitiesByCountryName(countryName);
        simpleCache.put(cacheKey, cities);
        return cities;
    }

    @GetMapping("/api/cities-by-country-native")
    public List<City> getCitiesByCountryNative(@RequestParam String countryName) {
        String cacheKey = "cities_native_" + countryName;
        Object cached = simpleCache.get(cacheKey);
        if (cached instanceof List<?> cachedCities) {
            return (List<City>) cachedCities;
        }

        List<City> cities = cityRepository.findCitiesByCountryNameNative(countryName);
        simpleCache.put(cacheKey, cities);
        return cities;
    }
}