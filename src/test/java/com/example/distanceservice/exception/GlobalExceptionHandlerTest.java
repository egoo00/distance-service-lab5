package com.example.distanceservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnBadRequestWhenHandleCityNotFoundException() {
        CityNotFoundException exception = new CityNotFoundException("City not found");

        ResponseEntity<String> response = globalExceptionHandler.handleCityNotFoundException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("City not found", response.getBody());
    }

    @Test
    void shouldReturnInternalServerErrorWhenHandleGeneralException() {
        Exception exception = new Exception("Test error");

        ResponseEntity<String> response = globalExceptionHandler.handleGeneralException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Test error", response.getBody());
    }
}
