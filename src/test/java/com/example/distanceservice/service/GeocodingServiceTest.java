package com.example.distanceservice.service;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.cache.SimpleCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    void shouldReturnCachedWhenGetCityMinskCached() {
        City city = mock(City.class);
        when(simpleCache.get(anyString())).thenReturn(city);

        City result = geocodingService.getCity("Minsk");

        assertNotNull(result);
        assertEquals(city, result);
    }

    @Test
    void shouldReturnNewCityWhenGetCityMinskNotCached() {
        when(simpleCache.get(anyString())).thenReturn(null);

        City result = geocodingService.getCity("Minsk");

        assertNotNull(result);
        assertEquals("Minsk", result.getName());
        verify(simpleCache).put(anyString(), result);
    }

    @Test
    void shouldReturnNewCityWhenGetCityWarsawNotCached() {
        when(simpleCache.get(anyString())).thenReturn(null);

        City result = geocodingService.getCity("Warsaw");

        assertNotNull(result);
        assertEquals("Warsaw", result.getName());
        verify(simpleCache).put(anyString(), result);
    }

    @Test
    void shouldReturnNullWhenGetCityInvalidCity() {
        when(simpleCache.get(anyString())).thenReturn(null);

        City result = geocodingService.getCity("Invalid");

        assertNull(result);
        verify(simpleCache).put(anyString(), null);
    }
}
