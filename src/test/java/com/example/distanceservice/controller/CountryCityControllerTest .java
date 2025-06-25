package com.example.distanceservice.controller;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.service.CountryCityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CountryCityControllerTest {

    @Mock
    private CountryCityService countryCityService;

    @InjectMocks
    private CountryCityController countryCityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnListWhenGetCitiesByCountryValid() {
        List<City> cities = Collections.emptyList();
        when(countryCityService.getCitiesByCountry(anyString())).thenReturn(cities);

        List<City> response = countryCityController.getCitiesByCountry("country");

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(countryCityService).getCitiesByCountry("country");
    }

    @Test
    void shouldReturnListWhenGetCitiesByCountryNativeValid() {
        List<City> cities = Collections.emptyList();
        when(countryCityService.getCitiesByCountryNative(anyString())).thenReturn(cities);

        List<City> response = countryCityController.getCitiesByCountryNative("country");

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(countryCityService).getCitiesByCountryNative("country");
    }
}
