package com.example.distanceservice.controller;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.service.CountryCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryCityController {
    private static final String API_PATH_CITIES_BY_COUNTRY = "/api/cities-by-country";
    private static final String API_PATH_CITIES_BY_COUNTRY_NATIVE = "/api/cities-by-country-native";

    private final CountryCityService countryCityService;

    @Autowired
    public CountryCityController(CountryCityService countryCityService) {
        this.countryCityService = countryCityService;
    }

    @GetMapping(API_PATH_CITIES_BY_COUNTRY)
    public List<City> getCitiesByCountry(@RequestParam String countryName) {
        return countryCityService.getCitiesByCountry(countryName);
    }

    @GetMapping(API_PATH_CITIES_BY_COUNTRY_NATIVE)
    public List<City> getCitiesByCountryNative(@RequestParam String countryName) {
        return countryCityService.getCitiesByCountryNative(countryName);
    }
}
