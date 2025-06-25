package com.example.distanceservice.controller;

import com.example.distanceservice.entity.Tourist;
import com.example.distanceservice.service.TouristService;
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

public class TouristControllerTest {

    @Mock
    private TouristService touristService;

    @InjectMocks
    private TouristController touristController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnListWhenGetAllTourists() {
        List<Tourist> tourists = Collections.emptyList();
        when(touristService.getAllTourists()).thenReturn(tourists);

        List<Tourist> response = touristController.getAllTourists();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(touristService).getAllTourists();
    }

    @Test
    void shouldReturnTouristWhenGetTouristByIdExists() {
        Optional<Tourist> tourist = Optional.empty();
        when(touristService.getTouristById(anyLong())).thenReturn(tourist);

        Optional<Tourist> response = touristController.getTouristById(1L);

        assertNotNull(response);
        assertFalse(response.isPresent());
        verify(touristService).getTouristById(1L);
    }

    @Test
    void shouldReturnSavedTouristWhenSaveTourist() {
        Tourist tourist = mock(Tourist.class);
        when(touristService.saveTourist(any(Tourist.class))).thenReturn(tourist);

        Tourist response = touristController.saveTourist(tourist);

        assertNotNull(response);
        assertEquals(tourist, response);
        verify(touristService).saveTourist(tourist);
    }

    @Test
    void shouldCallServiceWhenDeleteTourist() {
        touristController.deleteTourist(1L);

        verify(touristService).deleteTourist(1L);
    }
}
