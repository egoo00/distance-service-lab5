package com.example.distanceservice.service;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.cache.SimpleCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.example.distanceservice.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GeocodingServiceTest {

    @Mock
    private SimpleCache simpleCache;

    @InjectMocks
    private GeocodingService geocodingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCity_Minsk_Cached_ReturnsCached() {
        City cachedCity = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        when(simpleCache.get(GeocodingService.CACHE_KEY_GEOCODE + CITY_MINSK.toLowerCase())).thenReturn(cachedCity);

        City result = geocodingService.getCity(CITY_MINSK);

        assertNotNull(result);
        assertEquals(cachedCity, result);
    }

    @Test
    void testGetCity_Minsk_NotCached_ReturnsNewCity() {
        when(simpleCache.get(GeocodingService.CACHE_KEY_GEOCODE + CITY_MINSK.toLowerCase())).thenReturn(null);

        City result = geocodingService.getCity(CITY_MINSK);

        assertNotNull(result);
        assertEquals(CITY_MINSK, result.getName());
        assertEquals(MINSK_LAT, result.getLatitude(), DELTA);
        assertEquals(MINSK_LON, result.getLongitude(), DELTA);
        verify(simpleCache).put(GeocodingService.CACHE_KEY_GEOCODE + CITY_MINSK.toLowerCase(), result);
    }

    @Test
    void testGetCity_Warsaw_NotCached_ReturnsNewCity() {
        when(simpleCache.get(GeocodingService.CACHE_KEY_GEOCODE + CITY_WARSAW.toLowerCase())).thenReturn(null);

        City result = geocodingService.getCity(CITY_WARSAW);

        assertNotNull(result);
        assertEquals(CITY_WARSAW, result.getName());
        assertEquals(WARSAW_LAT, result.getLatitude(), DELTA);
        assertEquals(WARSAW_LON, result.getLongitude(), DELTA);
        verify(simpleCache).put(GeocodingService.CACHE_KEY_GEOCODE + CITY_WARSAW.toLowerCase(), result);
    }

    @Test
    void testGetCity_InvalidCity_ReturnsNull() {
        when(simpleCache.get(GeocodingService.CACHE_KEY_GEOCODE + INVALID_CITY.toLowerCase())).thenReturn(null);

        City result = geocodingService.getCity(INVALID_CITY);

        assertNull(result);
        verify(simpleCache).put(GeocodingService.CACHE_KEY_GEOCODE + INVALID_CITY.toLowerCase(), null);
    }
}