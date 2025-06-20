package com.example.distanceservice;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.entity.Country;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static com.example.distanceservice.TestConstants.*;
import static org.mockito.Mockito.*;

public class DataLoaderTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun_LoadsData() {
        dataLoader.run();

        verify(countryRepository).save(argThat(country -> COUNTRY_RUSSIA.equals(country.getName())));
        verify(cityRepository).save(argThat(city -> CITY_MOSCOW.equals(city.getName()) && MOSCOW_LAT == city.getLatitude() && MOSCOW_LON == city.getLongitude()));
        verify(cityRepository).save(argThat(city -> CITY_LONDON.equals(city.getName()) && LONDON_LAT == city.getLatitude() && LONDON_LON == city.getLongitude()));
    }
}