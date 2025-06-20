package com.example.distanceservice.controller;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
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
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        when(cityService.getAllCities()).thenReturn(Collections.singletonList(city));

        ResponseEntity<List<City>> response = (ResponseEntity<List<City>>) (Object) cityController.getAllCities();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(cityService).getAllCities();
    }

    @Test
    void testGetCityById_Exists_ReturnsCity() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        when(cityService.getCityById(1L)).thenReturn(Optional.of(city));

        ResponseEntity<Optional<City>> response = (ResponseEntity<Optional<City>>) (Object) cityController.getCityById(1L);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        verify(cityService).getCityById(1L);
    }

    @Test
    void testSaveCity_ReturnsSavedCity() {
        City city = new City(CITY_MINSK, MINSK_LAT, MINSK_LON);
        when(cityService.saveCity(city)).thenReturn(city);

        ResponseEntity<City> response = (ResponseEntity<City>) (Object) cityController.saveCity(city);

        assertNotNull(response.getBody());
        assertEquals(city, response.getBody());
        verify(cityService).saveCity(city);
    }

    @Test
    void testDeleteCity_CallsService() {
        cityController.deleteCity(1L);

        verify(cityService).deleteCity(1L);
    }
}
