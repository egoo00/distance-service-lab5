package com.example.distanceservice.service;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.cache.SimpleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService {
    private final SimpleCache simpleCache;

    @Autowired
    public GeocodingService(SimpleCache simpleCache) {
        this.simpleCache = simpleCache;
    }

    public City getCity(String cityName) {
        String cacheKey = "geocode_" + cityName.toLowerCase();
        @SuppressWarnings("unchecked")
        City cachedCity = (City) simpleCache.get(cacheKey);
        if (cachedCity != null) {
            return cachedCity;
        }

        City city = null;
        if ("Минск".equalsIgnoreCase(cityName)) {
            city = new City(cityName, 53.9, 27.5667);
        } else if ("Варшава".equalsIgnoreCase(cityName)) {
            city = new City(cityName, 52.2297, 21.0122);
        }
        simpleCache.put(cacheKey, city);
        return city;
    }
}
