package com.example.distanceservice.service;

import com.example.distanceservice.entity.Tourist;
import com.example.distanceservice.repository.TouristRepository;
import com.example.distanceservice.cache.SimpleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TouristService {
    private final TouristRepository touristRepository;
    private final SimpleCache simpleCache;

    @Autowired
    public TouristService(TouristRepository touristRepository, SimpleCache simpleCache) {
        this.touristRepository = touristRepository;
        this.simpleCache = simpleCache;
    }

    public List<Tourist> getAllTourists() {
        String cacheKey = "all_tourists";
        @SuppressWarnings("unchecked")
        List<Tourist> cachedTourists = (List<Tourist>) simpleCache.get(cacheKey);
        if (cachedTourists != null) {
            return cachedTourists;
        }
        List<Tourist> tourists = touristRepository.findAll();
        simpleCache.put(cacheKey, tourists);
        return tourists;
    }

    public Optional<Tourist> getTouristById(Long id) {
        String cacheKey = "tourist_" + id;
        @SuppressWarnings("unchecked")
        Optional<Tourist> cachedTourist = (Optional<Tourist>) simpleCache.get(cacheKey);
        if (cachedTourist != null) {
            return cachedTourist;
        }
        Optional<Tourist> tourist = touristRepository.findById(id);
        simpleCache.put(cacheKey, tourist);
        return tourist;
    }

    public Tourist saveTourist(Tourist tourist) {
        Tourist savedTourist = touristRepository.save(tourist);
        simpleCache.put("all_tourists", null); 
        simpleCache.put("tourist_" + savedTourist.getId(), savedTourist); 
        return savedTourist;
    }

    public void deleteTourist(Long id) {
        touristRepository.deleteById(id);
        simpleCache.put("all_tourists", null); 
        simpleCache.put("tourist_" + id, null); 
    }
}
