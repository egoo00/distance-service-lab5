package com.example.distanceservice.service;

import com.example.distanceservice.dto.DistanceResponse;
import com.example.distanceservice.entity.City;
import com.example.distanceservice.exception.CityNotFoundException;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.cache.SimpleCache;
import com.example.distanceservice.util.RequestCounter;
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

public class DistanceServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private RequestCounter requestCounter;

    @Mock
    private SimpleCache simpleCache;

    @InjectMocks
    private DistanceService distanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnDistanceWhenCalculateDistanceValid() {
        City city1 = mock(City.class);
        City city2 = mock(City.class);
        when(cityRepository.findByName(anyString())).thenReturn(Optional.of(city1), Optional.of(city2));
        when(simpleCache.get(anyString())).thenReturn(null);

        DistanceResponse response = distanceService.calculateDistance("city1", "city2");

        assertNotNull(response);
        verify(requestCounter).increment();
        verify(simpleCache).put(anyString(), response);
    }

    @Test
    void shouldThrowExceptionWhenCalculateDistanceCityNotFound() {
        when(cityRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(geocodingService.getCity(anyString())).thenReturn(null);

        assertThrows(CityNotFoundException.class, () -> distanceService.calculateDistance("city1", "city2"));
        verify(requestCounter).increment();
    }

    @Test
    void shouldReturnCachedWhenCalculateDistanceCached() {
        DistanceResponse response = mock(DistanceResponse.class);
        when(simpleCache.get(anyString())).thenReturn(response);

        DistanceResponse result = distanceService.calculateDistance("city1", "city2");

        assertNotNull(result);
        assertEquals(response, result);
        verify(requestCounter).increment();
        verifyNoInteractions(cityRepository, geocodingService);
    }

    @Test
    void shouldReturnResponsesWhenCalculateBulkDistancesValid() {
        City city1 = mock(City.class);
        City city2 = mock(City.class);
        when(cityRepository.findByName(anyString())).thenReturn(Optional.of(city1), Optional.of(city2));
        when(simpleCache.get(anyString())).thenReturn(null);

        List<String[]> pairs = Collections.singletonList(new String[]{"city1", "city2"});
        List<DistanceResponse> responses = distanceService.calculateBulkDistances(pairs);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(requestCounter).increment();
    }

    @Test
    void shouldReturnNegativeDistanceWhenCalculateBulkDistancesCityNotFound() {
        when(cityRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(geocodingService.getCity(anyString())).thenReturn(null);

        List<String[]> pairs = Collections.singletonList(new String[]{"city1", "city2"});
        List<DistanceResponse> responses = distanceService.calculateBulkDistances(pairs);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(-1, responses.get(0).getDistance());
        verify(requestCounter).increment();
    }

    @Test
    void shouldReturnEmptyListWhenCalculateBulkDistancesEmptyPairs() {
        List<String[]> pairs = Collections.emptyList();
        List<DistanceResponse> responses = distanceService.calculateBulkDistances(pairs);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verifyNoInteractions(requestCounter, cityRepository, geocodingService, simpleCache);
    }
}
