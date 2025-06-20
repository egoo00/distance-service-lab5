package com.example.distanceservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.distanceservice.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleCityNotFoundException_ReturnsBadRequest() {
        CityNotFoundException exception = new CityNotFoundException("City not found");

        ResponseEntity<String> response = globalExceptionHandler.handleCityNotFoundException(exception);

        assertNotNull(response);
        assertEquals(BAD_REQUEST_STATUS, response.getStatusCode());
        assertEquals("City not found", response.getBody());
    }

    @Test
    void testHandleGeneralException_ReturnsInternalServerError() {
        Exception exception = new Exception("Test error");

        ResponseEntity<String> response = globalExceptionHandler.handleGeneralException(exception);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR_STATUS, response.getStatusCode());
        assertEquals(UNEXPECTED_ERROR_MESSAGE + "Test error", response.getBody());
    }
}
