package com.example.distanceservice.controller;

import com.example.distanceservice.dto.DistanceResponse;
import com.example.distanceservice.service.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DistanceController {
    private final DistanceService distanceService;

    @Autowired
    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @GetMapping("/distance")
    public DistanceResponse getDistance(@RequestParam String from, @RequestParam String to) {
        return distanceService.calculateDistance(from, to);
    }

    @PostMapping("/distances/bulk")
    public List<DistanceResponse> getBulkDistances(@RequestBody List<String[]> cityPairs) {
        return distanceService.calculateBulkDistances(cityPairs);
    }
}