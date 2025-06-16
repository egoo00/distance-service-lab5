package com.example.distanceservice.service;

import com.example.distanceservice.entity.Country;
import com.example.distanceservice.repository.CountryRepository;
import com.example.distanceservice.cache.SimpleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final SimpleCache simpleCache;

    @Autowired
    public CountryService(CountryRepository countryRepository, SimpleCache simpleCache) {
        this.countryRepository = countryRepository;
        this.simpleCache = simpleCache;
    }

    public List<Country> getAllCountries() {
        String cacheKey = "all_countries";
        @SuppressWarnings("unchecked")
        List<Country> cachedCountries = (List<Country>) simpleCache.get(cacheKey);
        if (cachedCountries != null) {
            return cachedCountries;
        }
        List<Country> countries = countryRepository.findAll();
        simpleCache.put(cacheKey, countries);
        return countries;
    }

    public Optional<Country> getCountryById(Long id) {
        String cacheKey = "country_" + id;
        @SuppressWarnings("unchecked")
        Optional<Country> cachedCountry = (Optional<Country>) simpleCache.get(cacheKey);
        if (cachedCountry != null) {
            return cachedCountry;
        }
        Optional<Country> country = countryRepository.findById(id);
        simpleCache.put(cacheKey, country);
        return country;
    }

    public Country saveCountry(Country country) {
        Country savedCountry = countryRepository.save(country);
        simpleCache.put("all_countries", null);
        simpleCache.put("country_" + savedCountry.getId(), savedCountry); 
        return savedCountry;
    }

    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
        simpleCache.put("all_countries", null); 
        simpleCache.put("country_" + id, null); 
    }
}
