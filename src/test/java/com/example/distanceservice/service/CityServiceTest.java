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
import java.util.Optional;

import static com.example.distanceservice.TestConstants.*;
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
    void testGetAllCities_Cached_ReturnsCached() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        @SuppressWarnings("unchecked")
        List<City> cachedCities = Collections.singletonList(city);
        when(simpleCache.get(CityService.CACHE_KEY_ALL_CITIES)).thenReturn(cachedCities);

        List<City> result = cityService.getAllCities();

        assertNotNull(result);
        assertEquals(cachedCities, result);
        verifyNoInteractions(cityRepository);
    }

    @Test
    void testGetAllCities_NotCached_ReturnsFromRepo() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        List<City> cities = Collections.singletonList(city);
        when(cityRepository.findAll()).thenReturn(cities);
        when(simpleCache.get(CityService.CACHE_KEY_ALL_CITIES)).thenReturn(null);

        List<City> result = cityService.getAllCities();

        assertNotNull(result);
        assertEquals(cities, result);
        verify(simpleCache).put(CityService.CACHE_KEY_ALL_CITIES, cities);
    }

    @Test
    void testGetCityById_Cached_ReturnsCached() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        Optional<City> cachedCity = Optional.of(city);
        when(simpleCache.get(CityService.CACHE_KEY_CITY + 1L)).thenReturn(cachedCity);

        Optional<City> result = cityService.getCityById(1L);

        assertNotNull(result);
        assertEquals(cachedCity, result);
        verifyNoInteractions(cityRepository);
    }

    @Test
    void testGetCityById_NotCached_ReturnsFromRepo() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        Optional<City> optionalCity = Optional.of(city);
        when(cityRepository.findById(1L)).thenReturn(optionalCity);
        when(simpleCache.get(CityService.CACHE_KEY_CITY + 1L)).thenReturn(null);

        Optional<City> result = cityService.getCityById(1L);

        assertNotNull(result);
        assertEquals(optionalCity, result);
        verify(simpleCache).put(CityService.CACHE_KEY_CITY + 1L, optionalCity);
    }

    @Test
    void testSaveCity_UpdatesCache() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        City savedCity = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        when(cityRepository.save(city)).thenReturn(savedCity);

        City result = cityService.saveCity(city);

        assertNotNull(result);
        assertEquals(savedCity, result);
        verify(simpleCache).put(CityService.CACHE_KEY_ALL_CITIES, null);
        verify(simpleCache).put(CityService.CACHE_KEY_CITY + savedCity.getId(), savedCity);
    }

    @Test
    void testDeleteCity_UpdatesCache() {
        cityService.deleteCity(1L);

        verify(cityRepository).deleteById(1L);
        verify(simpleCache).put(CityService.CACHE_KEY_ALL_CITIES, null);
        verify(simpleCache).put(CityService.CACHE_KEY_CITY + 1L, null);
    }
}