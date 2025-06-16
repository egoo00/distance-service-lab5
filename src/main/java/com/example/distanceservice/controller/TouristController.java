package com.example.distanceservice.controller;

import com.example.distanceservice.entity.Tourist;
import com.example.distanceservice.service.TouristService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tourists")
public class TouristController {
    private final TouristService touristService;

    
    public TouristController(TouristService touristService) {
        this.touristService = touristService;
    }

    @GetMapping
    public List<Tourist> getAllTourists() {
        return touristService.getAllTourists();
    }

    @GetMapping("/{id}")
    public Optional<Tourist> getTouristById(@PathVariable Long id) {
        return touristService.getTouristById(id);
    }

    @PostMapping
    public Tourist saveTourist(@RequestBody Tourist tourist) {
        return touristService.saveTourist(tourist);
    }

    @DeleteMapping("/{id}")
    public void deleteTourist(@PathVariable Long id) {
        touristService.deleteTourist(id);
    }
}
