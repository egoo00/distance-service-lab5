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
    void shouldReturnListWhenGetAllCities() {
        List<City> cities = Collections.emptyList();
        when(cityService.getAllCities()).thenReturn(cities);

        List<City> response = cityController.getAllCities();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(cityService).getAllCities();
    }

    @Test
    void shouldReturnCityWhenGetCityByIdExists() {
        Optional<City> city = Optional.empty();
        when(cityService.getCityById(anyString())).thenReturn(city);

        Optional<City> response = cityController.getCityById("id");

        assertNotNull(response);
        assertFalse(response.isPresent());
        verify(cityService).getCityById("id");
    }

    @Test
    void shouldReturnSavedCityWhenSaveCity() {
        City city = mock(City.class);
        when(cityService.saveCity(any(City.class))).thenReturn(city);

        City response = cityController.saveCity(city);

        assertNotNull(response);
        assertEquals(city, response);
        verify(cityService).saveCity(city);
    }

    @Test
    void shouldCallServiceWhenDeleteCity() {
        cityController.deleteCity("id");

        verify(cityService).deleteCity("id");
    }
}
