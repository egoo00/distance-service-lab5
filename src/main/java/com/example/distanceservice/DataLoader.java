package com.example.distanceservice;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.entity.Country;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.repository.CountryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {
    private static final String COUNTRY_RUSSIA = "Russia";
    private static final String CITY_MOSCOW = "Moscow";
    private static final double MOSCOW_LAT = 55.7558;
    private static final double MOSCOW_LON = 37.6173;
    private static final String CITY_LONDON = "London";
    private static final double LONDON_LAT = 51.5074;
    private static final double LONDON_LON = -0.1278;

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public DataLoader(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    public void run(String... args) {
        Country russia = new Country();
        russia.setName(COUNTRY_RUSSIA);
        countryRepository.save(russia);

        City moscow = new City(CITY_MOSCOW, MOSCOW_LAT, MOSCOW_LON, russia, Collections.emptyList());
        cityRepository.save(moscow);

        City london = new City(CITY_LONDON, LONDON_LAT, LONDON_LON, null, Collections.emptyList());
        cityRepository.save(london);
    }
}
