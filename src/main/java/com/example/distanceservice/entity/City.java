package com.example.distanceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    private String name;
    private double latitude;
    private double longitude;

    @ManyToOne
    private Country country;

    @OneToMany(mappedBy = "city1", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CityPair> cityPairs = new ArrayList<>();

    public City(String name, double latitude, double longitude, Country country, List<CityPair> cityPairs) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.cityPairs = cityPairs != null ? cityPairs : new ArrayList<>();
    }
}
