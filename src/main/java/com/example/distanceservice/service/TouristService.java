package com.example.distanceservice.service;

import com.example.distanceservice.entity.Tourist;
import com.example.distanceservice.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TouristService {
    private final TouristRepository touristRepository;

    public TouristService(TouristRepository touristRepository) {
        this.touristRepository = touristRepository;
    }

    public List<Tourist> getAllTourists() {
        return touristRepository.findAll();
    }

    public Optional<Tourist> getTouristById(Long id) {
        return touristRepository.findById(id);
    }

    public Tourist saveTourist(Tourist tourist) {
        return touristRepository.save(tourist);
    }

    public void deleteTourist(Long id) {
        touristRepository.deleteById(id);
    }
}