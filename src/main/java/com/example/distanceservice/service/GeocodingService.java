package com.example.distanceservice.service;

import com.example.distanceservice.entity.City;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService {
    public City getCity(String cityName) {
        if ("Минск".equalsIgnoreCase(cityName)) {
            return new City(cityName, 53.9, 27.5667);
        } else if ("Варшава".equalsIgnoreCase(cityName)) {
            return new City(cityName, 52.2297, 21.0122);
        }
        return null;
    }
}