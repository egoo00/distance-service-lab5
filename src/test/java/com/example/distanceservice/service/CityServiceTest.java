package com.example.distanceservice.service;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.cache.SimpleCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private SimpleCache simpleCache;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCachedWhenGetAllCitiesCached() {
        List<City> cities = Collections.emptyList();
        when(simpleCache.get("all_cities")).thenReturn(cities);

        List<City> result = cityService.getAllCities();

        assertNotNull(result);
        assertEquals(cities, result);
        verifyNoInteractions(cityRepository);
    }

    @Test
    void shouldReturnFromRepoWhenGetAllCitiesNotCached() {
        List<City> cities = Collections.emptyList();
        when(cityRepository.findAll()).thenReturn(cities);
        when(simpleCache.get("all_cities")).thenReturn(null);

        List<City> result = cityService.getAllCities();

        assertNotNull(result);
        assertEquals(cities, result);
        verify(simpleCache).put("all_cities", cities);
    }

    @Test
    void shouldReturnCachedWhenGetCityByIdCached() {
        Optional<City> city = Optional.empty();
        when(simpleCache.get("city_id")).thenReturn(city);

        Optional<City> result = cityService.getCityById("id");

        assertNotNull(result);
        assertEquals(city, result);
        verifyNoInteractions(cityRepository);
    }

    @Test
    void shouldReturnFromRepoWhenGetCityByIdNotCached() {
        Optional<City> city = Optional.empty();
        when(cityRepository.findById("id")).thenReturn(city);
        when(simpleCache.get("city_id")).thenReturn(null);

        Optional<City> result = cityService.getCityById("id");

        assertNotNull(result);
        assertEquals(city, result);
        verify(simpleCache).put("city_id", city);
    }

    @Test
    void shouldReturnSavedCityWhenSaveCity() {
        City city = mock(City.class);
        when(cityRepository.save(any(City.class))).thenReturn(city);

        City result = cityService.saveCity(city);

        assertNotNull(result);
        assertEquals(city, result);
        verify(simpleCache).put("all_cities", null);
        verify(simpleCache).put("city_" + city.getName(), city);
    }

    @Test
    void shouldUpdateCacheWhenDeleteCity() {
        cityService.deleteCity("id");

        verify(cityRepository).deleteById("id");
        verify(simpleCache).put("all_cities", null);
        verify(simpleCache).put("city_id", null);
    }
}
