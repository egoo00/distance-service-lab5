package com.example.distanceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DistanceResponse {
    private String fromCity;
    private String toCity;
    private double distance;
    private String unit;
}