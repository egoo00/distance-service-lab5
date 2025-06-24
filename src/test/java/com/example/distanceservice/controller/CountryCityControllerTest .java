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
        City city = new City(CITY_MOSCOW, MOSCOW_LAT, MOSCOW_LON, null, Collections.emptyList());
        when(countryCityService.getCitiesByCountry(COUNTRY_RUSSIA)).thenReturn(Collections.singletonList(city));

        List<City> response = countryCityController.getCitiesByCountry(COUNTRY_RUSSIA);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(countryCityService).getCitiesByCountry(COUNTRY_RUSSIA);
    }

    @Test
    void testGetCitiesByCountryNative_ValidCountry_ReturnsList() {
        City city = new City(CITY_MOSCOW, MOSCOW_LAT, MOSCOW_LON, null, Collections.emptyList());
        when(countryCityService.getCitiesByCountryNative(COUNTRY_RUSSIA)).thenReturn(Collections.singletonList(city));

        List<City> response = countryCityController.getCitiesByCountryNative(COUNTRY_RUSSIA);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(countryCityService).getCitiesByCountryNative(COUNTRY_RUSSIA);
    }
}
