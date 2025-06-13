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

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private SimpleCache simpleCache;

    @GetMapping("/api/cities-by-country")
    public List<City> getCitiesByCountry(@RequestParam String countryName) {
        String cacheKey = "cities_" + countryName;
        List<City> cachedCities = (List<City>) simpleCache.get(cacheKey);

        if (cachedCities != null) {
            return cachedCities;
        }

        List<City> cities = cityRepository.findCitiesByCountryName(countryName);
        simpleCache.put(cacheKey, cities);
        return cities;
    }

    @GetMapping("/api/cities-by-country-native")
    public List<City> getCitiesByCountryNative(@RequestParam String countryName) {
        String cacheKey = "cities_native_" + countryName;
        List<City> cachedCities = (List<City>) simpleCache.get(cacheKey);

        if (cachedCities != null) {
            return cachedCities;
        }

        List<City> cities = cityRepository.findCitiesByCountryNameNative(countryName);
        simpleCache.put(cacheKey, cities);
        return cities;
    }
}