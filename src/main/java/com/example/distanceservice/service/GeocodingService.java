package com.example.distanceservice.service;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.exception.CityNotFoundException;
import com.example.distanceservice.repository.CityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {
    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;

    @Value("${geocoding.api.url}")
    private String apiUrl;

    @Value("${geocoding.api.key}")
    private String apiKey;

    public GeocodingService(RestTemplateBuilder restTemplateBuilder, CityRepository cityRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.cityRepository = cityRepository;
    }

    public City getCity(String cityName) {
        City city = cityRepository.findByName(cityName).orElse(null);
        if (city != null) {
            return city;
        }

        String url = String.format("%s?q=%s&key=%s", apiUrl, cityName, apiKey);
        ResponseEntity<com.example.distanceservice.dto.GeocodingResponse> response = restTemplate.getForEntity(url, com.example.distanceservice.dto.GeocodingResponse.class);

// Исправлено: Вместо выброса исключения возвращаем null, так как обработка ошибок не требуется
        if (response.getBody() == null || response.getBody().getResults() == null || response.getBody().getResults().isEmpty()) {
// throw new CityNotFoundException("City not found: " + cityName);
            return null;
        }

        com.example.distanceservice.dto.Geometry geometry = response.getBody().getResults().get(0).getGeometry();
        City newCity = new City(cityName, geometry.getLat(), geometry.getLng());
        return cityRepository.save(newCity);
    }
}