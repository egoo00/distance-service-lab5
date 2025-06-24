package com.example.distanceservice.controller;

import com.example.distanceservice.dto.DistanceResponse;
import com.example.distanceservice.service.DistanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static com.example.distanceservice.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DistanceControllerTest {

    @Mock
    private DistanceService distanceService;

    @InjectMocks
    private DistanceController distanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDistance_ValidCities_ReturnsDistance() {
        DistanceResponse response = new DistanceResponse(CITY_MINSK, CITY_WARSAW, EXPECTED_DISTANCE_MINSK_WARSAW, UNIT_KM);
        when(distanceService.calculateDistance(CITY_MINSK, CITY_WARSAW)).thenReturn(response);

        DistanceResponse result = distanceController.getDistance(CITY_MINSK, CITY_WARSAW);

        assertNotNull(result);
        assertEquals(response, result);
        verify(distanceService).calculateDistance(CITY_MINSK, CITY_WARSAW);
    }

    @Test
    void testGetBulkDistances_ValidPairs_ReturnsList() {
        DistanceResponse response = new DistanceResponse(CITY_MINSK, CITY_WARSAW, EXPECTED_DISTANCE_MINSK_WARSAW, UNIT_KM);
        List<DistanceResponse> responses = Collections.singletonList(response);
        when(distanceService.calculateBulkDistances(anyList())).thenReturn(responses);

        List<DistanceResponse> result = distanceController.getBulkDistances(Collections.singletonList(new String[]{CITY_MINSK, CITY_WARSAW}));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(distanceService).calculateBulkDistances(anyList());
    }

    @Test
    void testGetRequestCount_ReturnsCount() {
        when(distanceService.getRequestCount()).thenReturn(10);

        int result = distanceController.getRequestCount();

        assertEquals(10, result);
        verify(distanceService).getRequestCount();
    }
}
