package com.example.distanceservice.controller;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.service.CityService;
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

public class CityControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCities_ReturnsList() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON, null, Collections.emptyList());
        when(cityService.getAllCities()).thenReturn(Collections.singletonList(city));

        List<City> response = cityController.getAllCities();

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(cityService).getAllCities();
    }

    @Test
    void testGetCityById_Exists_ReturnsCity() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON, null, Collections.emptyList());
        when(cityService.getCityById(CITY_MINSK)).thenReturn(Optional.of(city));

        Optional<City> response = cityController.getCityById(CITY_MINSK);

        assertNotNull(response);
        assertTrue(response.isPresent());
        verify(cityService).getCityById(CITY_MINSK);
    }

    @Test
    void testSaveCity_ReturnsSavedCity() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON, null, Collections.emptyList());
        when(cityService.saveCity(city)).thenReturn(city);

        City response = cityController.saveCity(city);

        assertNotNull(response);
        assertEquals(city, response);
        verify(cityService).saveCity(city);
    }

    @Test
    void testDeleteCity_CallsService() {
        cityController.deleteCity(CITY_MINSK);

        verify(cityService).deleteCity(CITY_MINSK);
    }
}
