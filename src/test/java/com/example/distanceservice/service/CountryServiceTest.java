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
import java.util.Optional;

import static com.example.distanceservice.TestConstants.*;
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
    void testGetAllCountries_Cached_ReturnsCached() {
        Country country = new Country();
        country.setName(COUNTRY_RUSSIA);
        @SuppressWarnings("unchecked")
        List<Country> cachedCountries = Collections.singletonList(country);
        when(simpleCache.get(CountryService.CACHE_KEY_ALL_COUNTRIES)).thenReturn(cachedCountries);

        List<Country> result = countryService.getAllCountries();

        assertNotNull(result);
        assertEquals(cachedCountries, result);
        verifyNoInteractions(countryRepository);
    }

    @Test
    void testGetAllCountries_NotCached_ReturnsFromRepo() {
        Country country = new Country();
        country.setName(COUNTRY_RUSSIA);
        List<Country> countries = Collections.singletonList(country);
        when(countryRepository.findAll()).thenReturn(countries);
        when(simpleCache.get(CountryService.CACHE_KEY_ALL_COUNTRIES)).thenReturn(null);

        List<Country> result = countryService.getAllCountries();

        assertNotNull(result);
        assertEquals(countries, result);
        verify(simpleCache).put(CountryService.CACHE_KEY_ALL_COUNTRIES, countries);
    }

    @Test
    void testGetCountryById_Cached_ReturnsCached() {
        Country country = new Country();
        country.setId(1L);
        country.setName(COUNTRY_RUSSIA);
        Optional<Country> cachedCountry = Optional.of(country);
        when(simpleCache.get(CountryService.CACHE_KEY_COUNTRY + 1L)).thenReturn(cachedCountry);

        Optional<Country> result = countryService.getCountryById(1L);

        assertNotNull(result);
        assertEquals(cachedCountry, result);
        verifyNoInteractions(countryRepository);
    }

    @Test
    void testGetCountryById_NotCached_ReturnsFromRepo() {
        Country country = new Country();
        country.setId(1L);
        country.setName(COUNTRY_RUSSIA);
        Optional<Country> optionalCountry = Optional.of(country);
        when(countryRepository.findById(1L)).thenReturn(optionalCountry);
        when(simpleCache.get(CountryService.CACHE_KEY_COUNTRY + 1L)).thenReturn(null);

        Optional<Country> result = countryService.getCountryById(1L);

        assertNotNull(result);
        assertEquals(optionalCountry, result);
        verify(simpleCache).put(CountryService.CACHE_KEY_COUNTRY + 1L, optionalCountry);
    }

    @Test
    void testSaveCountry_UpdatesCache() {
        Country country = new Country();
        country.setName(COUNTRY_RUSSIA);
        Country savedCountry = new Country();
        savedCountry.setId(1L);
        savedCountry.setName(COUNTRY_RUSSIA);
        when(countryRepository.save(country)).thenReturn(savedCountry);

        Country result = countryService.saveCountry(country);

        assertNotNull(result);
        assertEquals(savedCountry, result);
        verify(simpleCache).put(CountryService.CACHE_KEY_ALL_COUNTRIES, null);
        verify(simpleCache).put(CountryService.CACHE_KEY_COUNTRY + savedCountry.getId(), savedCountry);
    }

    @Test
    void testDeleteCountry_UpdatesCache() {
        countryService.deleteCountry(1L);

        verify(countryRepository).deleteById(1L);
        verify(simpleCache).put(CountryService.CACHE_KEY_ALL_COUNTRIES, null);
        verify(simpleCache).put(CountryService.CACHE_KEY_COUNTRY + 1L, null);
    }
}