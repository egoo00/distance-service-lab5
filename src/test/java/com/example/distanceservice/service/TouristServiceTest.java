package com.example.distanceservice.service;

import com.example.distanceservice.entity.Tourist;
import com.example.distanceservice.repository.TouristRepository;
import com.example.distanceservice.cache.SimpleCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static com.example.distanceservice.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TouristServiceTest {

    @Mock
    private TouristRepository touristRepository;

    @Mock
    private SimpleCache simpleCache;

    @InjectMocks
    private TouristService touristService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTourists_Cached_ReturnsCached() {
        Tourist tourist = new Tourist();
        tourist.setName("John");
        @SuppressWarnings("unchecked")
        List<Tourist> cachedTourists = Collections.singletonList(tourist);
        when(simpleCache.get(TouristService.CACHE_KEY_ALL_TOURISTS)).thenReturn(cachedTourists);

        List<Tourist> result = touristService.getAllTourists();

        assertNotNull(result);
        assertEquals(cachedTourists, result);
        verifyNoInteractions(touristRepository);
    }

    @Test
    void testGetAllTourists_NotCached_ReturnsFromRepo() {
        Tourist tourist = new Tourist();
        tourist.setName("John");
        List<Tourist> tourists = Collections.singletonList(tourist);
        when(touristRepository.findAll()).thenReturn(tourists);
        when(simpleCache.get(TouristService.CACHE_KEY_ALL_TOURISTS)).thenReturn(null);

        List<Tourist> result = touristService.getAllTourists();

        assertNotNull(result);
        assertEquals(tourists, result);
        verify(simpleCache).put(TouristService.CACHE_KEY_ALL_TOURISTS, tourists);
    }

    @Test
    void testGetTouristById_Cached_ReturnsCached() {
        Tourist tourist = new Tourist();
        tourist.setId(1L);
        tourist.setName("John");
        Optional<Tourist> cachedTourist = Optional.of(tourist);
        when(simpleCache.get(TouristService.CACHE_KEY_TOURIST + 1L)).thenReturn(cachedTourist);

        Optional<Tourist> result = touristService.getTouristById(1L);

        assertNotNull(result);
        assertEquals(cachedTourist, result);
        verifyNoInteractions(touristRepository);
    }

    @Test
    void testGetTouristById_NotCached_ReturnsFromRepo() {
        Tourist tourist = new Tourist();
        tourist.setId(1L);
        tourist.setName("John");
        Optional<Tourist> optionalTourist = Optional.of(tourist);
        when(touristRepository.findById(1L)).thenReturn(optionalTourist);
        when(simpleCache.get(TouristService.CACHE_KEY_TOURIST + 1L)).thenReturn(null);

        Optional<Tourist> result = touristService.getTouristById(1L);

        assertNotNull(result);
        assertEquals(optionalTourist, result);
        verify(simpleCache).put(TouristService.CACHE_KEY_TOURIST + 1L, optionalTourist);
    }

    @Test
    void testSaveTourist_UpdatesCache() {
        Tourist tourist = new Tourist();
        tourist.setName("John");
        Tourist savedTourist = new Tourist();
        savedTourist.setId(1L);
        savedTourist.setName("John");
        when(touristRepository.save(tourist)).thenReturn(savedTourist);

        Tourist result = touristService.saveTourist(tourist);

        assertNotNull(result);
        assertEquals(savedTourist, result);
        verify(simpleCache).put(TouristService.CACHE_KEY_ALL_TOURISTS, null);
        verify(simpleCache).put(TouristService.CACHE_KEY_TOURIST + savedTourist.getId(), savedTourist);
    }

    @Test
    void testDeleteTourist_UpdatesCache() {
        touristService.deleteTourist(1L);

        verify(touristRepository).deleteById(1L);
        verify(simpleCache).put(TouristService.CACHE_KEY_ALL_TOURISTS, null);
        verify(simpleCache).put(TouristService.CACHE_KEY_TOURIST + 1L, null);
    }
}