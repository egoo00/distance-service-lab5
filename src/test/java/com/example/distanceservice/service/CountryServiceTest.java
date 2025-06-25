package com.example.distanceservice.service;

import com.example.distanceservice.entity.Country;
import com.example.distanceservice.repository.CountryRepository;
import com.example.distanceservice.cache.SimpleCache;
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

public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private SimpleCache simpleCache;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCachedWhenGetAllCountriesCached() {
        List<Country> countries = Collections.emptyList();
        when(simpleCache.get("all_countries")).thenReturn(countries);

        List<Country> result = countryService.getAllCountries();

        assertNotNull(result);
        assertEquals(countries, result);
        verifyNoInteractions(countryRepository);
    }

    @Test
    void shouldReturnFromRepoWhenGetAllCountriesNotCached() {
        List<Country> countries = Collections.emptyList();
        when(countryRepository.findAll()).thenReturn(countries);
        when(simpleCache.get("all_countries")).thenReturn(null);

        List<Country> result = countryService.getAllCountries();

        assertNotNull(result);
        assertEquals(countries, result);
        verify(simpleCache).put("all_countries", countries);
    }

    @Test
    void shouldReturnCachedWhenGetCountryByIdCached() {
        Optional<Country> country = Optional.empty();
        when(simpleCache.get("country_1")).thenReturn(country);

        Optional<Country> result = countryService.getCountryById(1L);

        assertNotNull(result);
        assertEquals(country, result);
        verifyNoInteractions(countryRepository);
    }

    @Test
    void shouldReturnFromRepoWhenGetCountryByIdNotCached() {
        Optional<Country> country = Optional.empty();
        when(countryRepository.findById(1L)).thenReturn(country);
        when(simpleCache.get("country_1")).thenReturn(null);

        Optional<Country> result = countryService.getCountryById(1L);

        assertNotNull(result);
        assertEquals(country, result);
        verify(simpleCache).put("country_1", country);
    }

    @Test
    void shouldReturnSavedCountryWhenSaveCountry() {
        Country country = mock(Country.class);
        when(countryRepository.save(any(Country.class))).thenReturn(country);

        Country result = countryService.saveCountry(country);

        assertNotNull(result);
        assertEquals(country, result);
        verify(simpleCache).put("all_countries", null);
        verify(simpleCache).put("country_" + country.getId(), country);
    }

    @Test
    void shouldUpdateCacheWhenDeleteCountry() {
        countryService.deleteCountry(1L);

        verify(countryRepository).deleteById(1L);
        verify(simpleCache).put("all_countries", null);
        verify(simpleCache).put("country_1", null);
    }
}
