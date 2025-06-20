package com.example.distanceservice.controller;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.service.CountryCityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static com.example.distanceservice.TestConstants.*;
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
    void testGetCitiesByCountry_ValidCountry_ReturnsList() {
        City city = new City(CITY_MOSCOW, MOSCOW_LAT, MOSCOW_LON);
        when(countryCityService.getCitiesByCountry(COUNTRY_RUSSIA)).thenReturn(Collections.singletonList(city));

        ResponseEntity<List<City>> response = (ResponseEntity<List<City>>) (Object) countryCityController.getCitiesByCountry(COUNTRY_RUSSIA);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(countryCityService).getCitiesByCountry(COUNTRY_RUSSIA);
    }

    @Test
    void testGetCitiesByCountryNative_ValidCountry_ReturnsList() {
        City city = new City(CITY_MOSCOW, MOSCOW_LAT, MOSCOW_LON);
        when(countryCityService.getCitiesByCountryNative(COUNTRY_RUSSIA)).thenReturn(Collections.singletonList(city));

        ResponseEntity<List<City>> response = (ResponseEntity<List<City>>) (Object) countryCityController.getCitiesByCountryNative(COUNTRY_RUSSIA);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(countryCityService).getCitiesByCountryNative(COUNTRY_RUSSIA);
    }
}