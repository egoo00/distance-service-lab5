package com.example.distanceservice.repository;

import com.example.distanceservice.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE c.country.name = ?1")
    List<City> findCitiesByCountryName(String countryName);

    @Query(value = "SELECT * FROM city c JOIN country co ON c.country_id = co.id WHERE co.name = ?1", nativeQuery = true)
    List<City> findCitiesByCountryNameNative(String countryName);

    Optional<City> findByName(String name); // Добавленный метод
}