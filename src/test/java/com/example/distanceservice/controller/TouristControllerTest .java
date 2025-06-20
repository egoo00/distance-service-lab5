package com.example.distanceservice.controller;

import com.example.distanceservice.entity.Tourist;
import com.example.distanceservice.service.TouristService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;




import java.util.Collections;
import java.util.Optional;

import static com.example.distanceservice.TestConstants.*;
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
    void testGetAllTourists_ReturnsList() {
        Tourist tourist = new Tourist();
        tourist.setName("John");
        when(touristService.getAllTourists()).thenReturn(Collections.singletonList(tourist));

        ResponseEntity<List<Tourist>> response = (ResponseEntity<List<Tourist>>) (Object) touristController.getAllTourists();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(touristService).getAllTourists();
    }

    @Test
    void testGetTouristById_Exists_ReturnsTourist() {
        Tourist tourist = new Tourist();
        tourist.setId(1L);
        tourist.setName("John");
        when(touristService.getTouristById(1L)).thenReturn(Optional.of(tourist));

        ResponseEntity<Optional<Tourist>> response = (ResponseEntity<Optional<Tourist>>) (Object) touristController.getTouristById(1L);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
        verify(touristService).getTouristById(1L);
    }

    @Test
    void testSaveTourist_ReturnsSavedTourist() {
        Tourist tourist = new Tourist();
        tourist.setName("John");
        when(touristService.saveTourist(tourist)).thenReturn(tourist);

        ResponseEntity<Tourist> response = (ResponseEntity<Tourist>) (Object) touristController.saveTourist(tourist);

        assertNotNull(response.getBody());
        assertEquals(tourist, response.getBody());
        verify(touristService).saveTourist(tourist);
    }

    @Test
    void testDeleteTourist_CallsService() {
        touristController.deleteTourist(1L);

        verify(touristService).deleteTourist(1L);
    }
}
