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
import java.util.List;
import java.util.Optional;

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
    void shouldReturnCachedWhenGetAllTouristsCached() {
        List<Tourist> tourists = Collections.emptyList();
        when(simpleCache.get("all_tourists")).thenReturn(tourists);

        List<Tourist> result = touristService.getAllTourists();

        assertNotNull(result);
        assertEquals(tourists, result);
        verifyNoInteractions(touristRepository);
    }

    @Test
    void shouldReturnFromRepoWhenGetAllTouristsNotCached() {
        List<Tourist> tourists = Collections.emptyList();
        when(touristRepository.findAll()).thenReturn(tourists);
        when(simpleCache.get("all_tourists")).thenReturn(null);

        List<Tourist> result = touristService.getAllTourists();

        assertNotNull(result);
        assertEquals(tourists, result);
        verify(simpleCache).put("all_tourists", tourists);
    }

    @Test
    void shouldReturnCachedWhenGetTouristByIdCached() {
        Optional<Tourist> tourist = Optional.empty();
        when(simpleCache.get("tourist_1")).thenReturn(tourist);

        Optional<Tourist> result = touristService.getTouristById(1L);

        assertNotNull(result);
        assertEquals(tourist, result);
        verifyNoInteractions(touristRepository);
    }

    @Test
    void shouldReturnFromRepoWhenGetTouristByIdNotCached() {
        Optional<Tourist> tourist = Optional.empty();
        when(touristRepository.findById(1L)).thenReturn(tourist);
        when(simpleCache.get("tourist_1")).thenReturn(null);

        Optional<Tourist> result = touristService.getTouristById(1L);

        assertNotNull(result);
        assertEquals(tourist, result);
        verify(simpleCache).put("tourist_1", tourist);
    }

    @Test
    void shouldReturnSavedTouristWhenSaveTourist() {
        Tourist tourist = mock(Tourist.class);
        when(touristRepository.save(any(Tourist.class))).thenReturn(tourist);

        Tourist result = touristService.saveTourist(tourist);

        assertNotNull(result);
        assertEquals(tourist, result);
        verify(simpleCache).put("all_tourists", null);
        verify(simpleCache).put("tourist_" + tourist.getId(), tourist);
    }

    @Test
    void shouldUpdateCacheWhenDeleteTourist() {
        touristService.deleteTourist(1L);

        verify(touristRepository).deleteById(1L);
        verify(simpleCache).put("all_tourists", null);
        verify(simpleCache).put("tourist_1", null);
    }
}
