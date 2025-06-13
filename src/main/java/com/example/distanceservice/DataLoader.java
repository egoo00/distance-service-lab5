package com.example.distanceservice;

import com.example.distanceservice.entity.City;
import com.example.distanceservice.entity.Country;
import com.example.distanceservice.repository.CityRepository;
import com.example.distanceservice.repository.CountryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

        City moscow = new City();
        moscow.setName("Moscow");
        moscow.setLatitude(55.7558);
        moscow.setLongitude(37.6173);
        moscow.setCountry(russia);
        cityRepository.save(moscow);

        City london = new City();
        london.setName("London");
        london.setLatitude(51.5074);
        london.setLongitude(-0.1278);
        cityRepository.save(london);
    }
}