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

import static com.example.distanceservice.TestConstants.*;
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
    void testCalculateDistance_ValidCities_ReturnsDistance() {
        City minsk = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        City warsaw = new City(CITY_WARSAW, WARSAW_LAT, WARSAW_LON);
        when(cityRepository.findByName(CITY_MINSK.toLowerCase())).thenReturn(Optional.of(minsk));
        when(cityRepository.findByName(CITY_WARSAW.toLowerCase())).thenReturn(Optional.of(warsaw));
        when(simpleCache.get(DistanceService.CACHE_KEY_DISTANCE + CITY_MINSK.toLowerCase() + "_" + CITY_WARSAW.toLowerCase())).thenReturn(null);

        DistanceResponse response = distanceService.calculateDistance(CITY_MINSK, CITY_WARSAW);

        assertNotNull(response);
        assertEquals(CITY_MINSK, response.getFromCity());
        assertEquals(CITY_WARSAW, response.getToCity());
        assertEquals(UNIT_KM, response.getUnit());
        assertTrue(response.getDistance() > 0);
        verify(requestCounter).increment();
        verify(simpleCache).put(DistanceService.CACHE_KEY_DISTANCE + CITY_MINSK.toLowerCase() + "_" + CITY_WARSAW.toLowerCase(), response);
    }

    @Test
    void testCalculateDistance_CityNotFound_ThrowsException() {
        when(cityRepository.findByName(CITY_MINSK.toLowerCase())).thenReturn(Optional.empty());
        when(geocodingService.getCity(CITY_MINSK)).thenReturn(null);
        when(cityRepository.findByName(CITY_WARSAW.toLowerCase())).thenReturn(Optional.of(new City(CITY_WARSAW, WARSAW_LAT, WARSAW_LON)));

        assertThrows(CityNotFoundException.class, () -> distanceService.calculateDistance(CITY_MINSK, CITY_WARSAW));
        verify(requestCounter).increment();
    }

    @Test
    void testCalculateDistance_CachedResponse_ReturnsCached() {
        DistanceResponse cachedResponse = new DistanceResponse(CITY_MINSK, CITY_WARSAW, EXPECTED_DISTANCE_MINSK_WARSAW, UNIT_KM);
        when(simpleCache.get(DistanceService.CACHE_KEY_DISTANCE + CITY_MINSK.toLowerCase() + "_" + CITY_WARSAW.toLowerCase())).thenReturn(cachedResponse);

        DistanceResponse response = distanceService.calculateDistance(CITY_MINSK, CITY_WARSAW);

        assertNotNull(response);
        assertEquals(cachedResponse, response);
        verify(requestCounter).increment();
        verifyNoInteractions(cityRepository, geocodingService);
    }

    @Test
    void testCalculateBulkDistances_ValidPairs_ReturnsResponses() {
        City minsk = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        City warsaw = new City(CITY_WARSAW, WARSAW_LAT, WARSAW_LON);
        when(cityRepository.findByName(CITY_MINSK.toLowerCase())).thenReturn(Optional.of(minsk));
        when(cityRepository.findByName(CITY_WARSAW.toLowerCase())).thenReturn(Optional.of(warsaw));
        when(simpleCache.get(DistanceService.CACHE_KEY_DISTANCE + CITY_MINSK.toLowerCase() + "_" + CITY_WARSAW.toLowerCase())).thenReturn(null);

        List<String[]> pairs = List.of(new String[]{CITY_MINSK, CITY_WARSAW});
        List<DistanceResponse> responses = distanceService.calculateBulkDistances(pairs);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(CITY_MINSK, responses.get(0).getFromCity());
        assertEquals(CITY_WARSAW, responses.get(0).getToCity());
        assertTrue(responses.get(0).getDistance() > 0);
        verify(requestCounter, times(1)).increment();
    }

    @Test
    void testCalculateBulkDistances_CityNotFound_ReturnsNegativeDistance() {
        when(cityRepository.findByName(CITY_MINSK.toLowerCase())).thenReturn(Optional.empty());
        when(geocodingService.getCity(CITY_MINSK)).thenReturn(null);
        when(cityRepository.findByName(CITY_WARSAW.toLowerCase())).thenReturn(Optional.of(new City(CITY_WARSAW, WARSAW_LAT, WARSAW_LON)));

        List<String[]> pairs = List.of(new String[]{CITY_MINSK, CITY_WARSAW});
        List<DistanceResponse> responses = distanceService.calculateBulkDistances(pairs);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(CITY_MINSK, responses.get(0).getFromCity());
        assertEquals(CITY_WARSAW, responses.get(0).getToCity());
        assertEquals(-1, responses.get(0).getDistance());
        verify(requestCounter, times(1)).increment();
    }

    @Test
    void testCalculateBulkDistances_EmptyPairs_ReturnsEmptyList() {
        List<String[]> pairs = List.of();
        List<DistanceResponse> responses = distanceService.calculateBulkDistances(pairs);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verifyNoInteractions(requestCounter, cityRepository, geocodingService, simpleCache);
    }
}
