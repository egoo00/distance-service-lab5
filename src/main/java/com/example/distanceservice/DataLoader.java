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
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public DataLoader(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Country russia = new Country();
        russia.setName("Russia");
        countryRepository.save(russia);

        City moscow = new City("Moscow", 55.7558, 37.6173, russia, Collections.emptyList());
        cityRepository.save(moscow);

        City london = new City("London", 51.5074, -0.1278, null, Collections.emptyList());
        cityRepository.save(london);
    }
}