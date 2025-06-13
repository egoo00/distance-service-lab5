package com.example.distanceservice.service;

import com.example.distanceservice.dto.DistanceResponse;
import com.example.distanceservice.entity.City;
import com.example.distanceservice.exception.CityNotFoundException;
import com.example.distanceservice.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DistanceServiceTest {
    @Mock
    private CityRepository cityRepository;

    @Mock
    private GeocodingService geocodingService;

    @InjectMocks
    private DistanceService distanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateDistance_ValidCities_ReturnsDistance() {
        City minsk = new City("Минск", 53.9, 27.5667);
        City warsaw = new City("Варшава", 52.2297, 21.0122);

        when(cityRepository.findByName("Минск")).thenReturn(Optional.of(minsk));
        when(cityRepository.findByName("Варшава")).thenReturn(Optional.of(warsaw));

        DistanceResponse response = distanceService.calculateDistance("Минск", "Варшава");

        assertNotNull(response);
        assertEquals("Минск", response.getFromCity());
        assertEquals("Варшава", response.getToCity());
        assertTrue(response.getDistance() > 0);
        assertEquals("km", response.getUnit());
    }

    @Test
    void calculateDistance_CityNotFound_ThrowsException() {
        when(cityRepository.findByName("Минск")).thenReturn(Optional.empty());
        when(geocodingService.getCity("Минск")).thenReturn(null);

        assertThrows(CityNotFoundException.class, () -> {
            distanceService.calculateDistance("Минск", "Варшава");
        });
    }

    @Test
    void calculateDistance_Test500_ThrowsException() {
        City minsk = new City("Минск", 53.9, 27.5667);
        when(cityRepository.findByName("Минск")).thenReturn(Optional.of(minsk));
        when(cityRepository.findByName("test500")).thenReturn(Optional.empty());
        when(geocodingService.getCity("test500")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            distanceService.calculateDistance("Минск", "test500");
        });
    }

    @Test
    void calculateBulkDistances_ValidPairs_ReturnsResponses() {
        City minsk = new City("Минск", 53.9, 27.5667);
        City warsaw = new City("Варшава", 52.2297, 21.0122);

        when(cityRepository.findByName("Минск")).thenReturn(Optional.of(minsk));
        when(cityRepository.findByName("Варшава")).thenReturn(Optional.of(warsaw));

        List<String[]> pairs = List.of(new String[]{"Минск", "Варшава"});
        List<DistanceResponse> responses = distanceService.calculateBulkDistances(pairs);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertTrue(responses.get(0).getDistance() > 0);
    }

    @Test
    void calculateBulkDistances_CityNotFound_ReturnsNegativeDistance() {
        when(cityRepository.findByName("Минск")).thenReturn(Optional.empty());
        when(geocodingService.getCity("Минск")).thenReturn(null);
        when(cityRepository.findByName("Варшава")).thenReturn(Optional.of(new City("Варшава", 52.2297, 21.0122)));

        List<String[]> pairs = List.of(new String[]{"Минск", "Варшава"});
        List<DistanceResponse> responses = distanceService.calculateBulkDistances(pairs);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(-1, responses.get(0).getDistance());
    }
}