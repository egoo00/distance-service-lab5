package com.example.distanceservice.controller;

import com.example.distanceservice.entity.Country;
import com.example.distanceservice.service.CountryService;
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

public class CountryControllerTest {

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCountries_ReturnsList() {
        Country country = new Country();
        country.setName(COUNTRY_RUSSIA);
        when(countryService.getAllCountries()).thenReturn(Collections.singletonList(country));

        ResponseEntity<List<Country>> response = (ResponseEntity<List<Country>>) (Object) countryController.getAllCountries();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(countryService).getAllCountries();
    }

    @Test
    void testGetCountryById_Exists_ReturnsCountry() {
        Country country = new Country();
        country.setId(1L);
        country.setName(COUNTRY_RUSSIA);
        when(countryService.getCountryById(1L)).thenReturn(Optional.of(country));

        ResponseEntity<Optional<Country>> response = (ResponseEntity<Optional<Country>>) (Object) countryController.getCountryById(1L);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        verify(countryService).getCountryById(1L);
    }

    @Test
    void testSaveCountry_ReturnsSavedCountry() {
        Country country = new Country();
        country.setName(COUNTRY_RUSSIA);
        when(countryService.saveCountry(country)).thenReturn(country);

        ResponseEntity<Country> response = (ResponseEntity<Country>) (Object) countryController.saveCountry(country);

        assertNotNull(response.getBody());
        assertEquals(country, response.getBody());
        verify(countryService).saveCountry(country);
    }

    @Test
    void testDeleteCountry_CallsService() {
        countryController.deleteCountry(1L);

        verify(countryService).deleteCountry(1L);
    }
}
