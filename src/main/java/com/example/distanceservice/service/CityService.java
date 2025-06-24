package com.example.distanceservice.service;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.cache.SimpleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    public static final String CACHE_KEY_ALL_CITIES = "all_cities";
    public static final String CACHE_KEY_CITY = "city_";

    private final CityRepository cityRepository;
    private final SimpleCache simpleCache;

    @Autowired
    public CityService(CityRepository cityRepository, SimpleCache simpleCache) {
        this.cityRepository = cityRepository;
        this.simpleCache = simpleCache;
    }

    public List<City> getAllCities() {
        @SuppressWarnings("unchecked")
        List<City> cachedCities = (List<City>) simpleCache.get(CACHE_KEY_ALL_CITIES);
        if (cachedCities != null) {
            return cachedCities;
        }
        List<City> cities = cityRepository.findAll();
        simpleCache.put(CACHE_KEY_ALL_CITIES, cities);
        return cities;
    }

    public Optional<City> getCityById(String name) {
        String cacheKey = CACHE_KEY_CITY + name;
        @SuppressWarnings("unchecked")
        Optional<City> cachedCity = (Optional<City>) simpleCache.get(cacheKey);
        if (cachedCity != null) {
            return cachedCity;
        }
        Optional<City> city = cityRepository.findByName(name);
        simpleCache.put(cacheKey, city);
        return city;
    }

    public City saveCity(City city) {
        City savedCity = cityRepository.save(city);
        simpleCache.put(CACHE_KEY_ALL_CITIES, null);
        simpleCache.put(CACHE_KEY_CITY + savedCity.getName(), savedCity);
        return savedCity;
    }

    public void deleteCity(String name) {
        cityRepository.deleteById(name);
        simpleCache.put(CACHE_KEY_ALL_CITIES, null);
        simpleCache.put(CACHE_KEY_CITY + name, null);
    }
}
