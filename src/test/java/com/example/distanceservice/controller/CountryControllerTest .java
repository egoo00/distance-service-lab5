package com.example.distanceservice.controller;

import com.example.distanceservice.entity.Country;
import com.example.distanceservice.service.CountryService;
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
    void shouldReturnListWhenGetAllCountries() {
        List<Country> countries = Collections.emptyList();
        when(countryService.getAllCountries()).thenReturn(countries);

        List<Country> response = countryController.getAllCountries();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(countryService).getAllCountries();
    }

    @Test
    void shouldReturnCountryWhenGetCountryByIdExists() {
        Optional<Country> country = Optional.empty();
        when(countryService.getCountryById(anyLong())).thenReturn(country);

        Optional<Country> response = countryController.getCountryById(1L);

        assertNotNull(response);
        assertFalse(response.isPresent());
        verify(countryService).getCountryById(1L);
    }

    @Test
    void shouldReturnSavedCountryWhenSaveCountry() {
        Country country = mock(Country.class);
        when(countryService.saveCountry(any(Country.class))).thenReturn(country);

        Country response = countryController.saveCountry(country);

        assertNotNull(response);
        assertEquals(country, response);
        verify(countryService).saveCountry(country);
    }

    @Test
    void shouldCallServiceWhenDeleteCountry() {
        countryController.deleteCountry(1L);

        verify(countryService).deleteCountry(1L);
    }
}
