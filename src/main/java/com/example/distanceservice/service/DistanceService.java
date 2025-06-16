package com.example.distanceservice.service;

import com.example.distanceservice.dto.DistanceResponse;
import com.example.distanceservice.entity.City;
import com.example.distanceservice.entity.CityPair;
import com.example.distanceservice.exception.CityNotFoundException;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.service.RequestCounter;
import com.example.distanceservice.cache.SimpleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistanceService {
    private final CityRepository cityRepository;
    private final GeocodingService geocodingService;
    private final RequestCounter requestCounter;
    private final SimpleCache simpleCache;

    @Autowired
    public DistanceService(CityRepository cityRepository, GeocodingService geocodingService, RequestCounter requestCounter, SimpleCache simpleCache) {
        this.cityRepository = cityRepository;
        this.geocodingService = geocodingService;
        this.requestCounter = requestCounter;
        this.simpleCache = simpleCache;
    }

    public DistanceResponse calculateDistance(String city1Name, String city2Name) {
        requestCounter.increment();
        String cacheKey = "distance_" + city1Name.toLowerCase() + "_" + city2Name.toLowerCase();
        @SuppressWarnings("unchecked")
        DistanceResponse cachedResponse = (DistanceResponse) simpleCache.get(cacheKey);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        City city1 = cityRepository.findByName(city1Name.toLowerCase())
                .orElseGet(() -> geocodingService.getCity(city1Name));
        City city2 = cityRepository.findByName(city2Name.toLowerCase())
                .orElseGet(() -> geocodingService.getCity(city2Name));
        if (city1 == null || city2 == null) {
            throw new CityNotFoundException("One or both cities not found");
        }
        double distance = calculateHaversine(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude());
        CityPair cityPair = new CityPair();
        cityPair.setCity1(city1);
        cityPair.setCity2(city2);
        cityPair.setDistance(distance);
        cityPair.setUnit("km");
        city1.getCityPairs().add(cityPair);
        cityRepository.save(city1);
        DistanceResponse response = new DistanceResponse(city1Name, city2Name, distance, "km");
        simpleCache.put(cacheKey, response);
        return response;
    }

    public List<DistanceResponse> calculateBulkDistances(List<String[]> cityPairs) {
        return cityPairs.stream()
                .map(pair -> {
                    String city1 = pair[0];
                    String city2 = pair[1];
                    try {
                        return calculateDistance(city1, city2);
                    } catch (CityNotFoundException e) {
                        return new DistanceResponse(city1, city2, -1, "km");
                    }
                })
                .collect(Collectors.toList());
    }

    private double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
