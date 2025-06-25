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
    void shouldLoadDataWhenRun() {
        dataLoader.run();

        verify(countryRepository).save(argThat(country -> "Russia".equals(country.getName())));
        verify(cityRepository).save(argThat(city -> "Moscow".equals(city.getName()) && 55.7558 == city.getLatitude() && 37.6173 == city.getLongitude()));
        verify(cityRepository).save(argThat(city -> "London".equals(city.getName()) && 51.5074 == city.getLatitude() && -0.1278 == city.getLongitude()));
    }
}
