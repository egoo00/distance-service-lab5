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
    private final CityRepository cityRepository;
    private final SimpleCache simpleCache;

    @Autowired
    public CityService(CityRepository cityRepository, SimpleCache simpleCache) {
        this.cityRepository = cityRepository;
        this.simpleCache = simpleCache;
    }

    public List<City> getAllCities() {
        String cacheKey = "all_cities";
        @SuppressWarnings("unchecked")
        List<City> cachedCities = (List<City>) simpleCache.get(cacheKey);
        if (cachedCities != null) {
            return cachedCities;
        }
        List<City> cities = cityRepository.findAll();
        simpleCache.put(cacheKey, cities);
        return cities;
    }

    public Optional<City> getCityById(Long id) {
        String cacheKey = "city_" + id;
        @SuppressWarnings("unchecked")
        Optional<City> cachedCity = (Optional<City>) simpleCache.get(cacheKey);
        if (cachedCity != null) {
            return cachedCity;
        }
        Optional<City> city = cityRepository.findById(id);
        simpleCache.put(cacheKey, city);
        return city;
    }

    public City saveCity(City city) {
        City savedCity = cityRepository.save(city);
      
        simpleCache.put("all_cities", null); 
        simpleCache.put("city_" + savedCity.getId(), savedCity); 
        return savedCity;
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
        simpleCache.put("all_cities", null); 
        simpleCache.put("city_" + id, null);
    }
}
