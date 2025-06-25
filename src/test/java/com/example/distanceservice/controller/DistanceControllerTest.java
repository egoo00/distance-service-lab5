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
    void shouldReturnDistanceWhenGetDistanceValid() {
        DistanceResponse response = mock(DistanceResponse.class);
        when(distanceService.calculateDistance(anyString(), anyString())).thenReturn(response);

        DistanceResponse result = distanceController.getDistance("from", "to");

        assertNotNull(result);
        assertEquals(response, result);
        verify(distanceService).calculateDistance("from", "to");
    }

    @Test
    void shouldReturnListWhenGetBulkDistancesValid() {
        List<DistanceResponse> responses = Collections.emptyList();
        when(distanceService.calculateBulkDistances(anyList())).thenReturn(responses);

        List<DistanceResponse> result = distanceController.getBulkDistances(Collections.emptyList());

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(distanceService).calculateBulkDistances(anyList());
    }

    @Test
    void shouldReturnCountWhenGetRequestCount() {
        when(distanceService.getRequestCount()).thenReturn(0);

        int result = distanceController.getRequestCount();

        assertEquals(0, result);
        verify(distanceService).getRequestCount();
    }
}
