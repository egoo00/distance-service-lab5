package com.example.distanceservice.controller;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private static final String API_PATH = "/api/cities";

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public Optional<City> getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @PostMapping
    public City saveCity(@RequestBody City city) {
        return cityService.saveCity(city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
    }
}
