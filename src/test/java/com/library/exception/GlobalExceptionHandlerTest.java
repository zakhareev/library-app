package com.library.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Book not found");
        ResponseEntity<Map<String, Object>> response = handler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Book not found", body.get("message"));
        assertEquals(404, body.get("status"));
        assertEquals("Not Found", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testHandleBookNotAvailable() {
        BookNotAvailableException ex = new BookNotAvailableException("Book is already on loan");
        ResponseEntity<Map<String, Object>> response = handler.handleBookNotAvailable(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Book is already on loan", body.get("message"));
        assertEquals(400, body.get("status"));
        assertEquals("Bad Request", body.get("error"));
    }

    @Test
    void testHandleDuplicateResource() {
        DuplicateResourceException ex = new DuplicateResourceException("Email already exists");
        ResponseEntity<Map<String, Object>> response = handler.handleDuplicateResource(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Email already exists", body.get("message"));
        assertEquals(409, body.get("status"));
        assertEquals("Conflict", body.get("error"));
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Internal server error");
        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(((String) body.get("message")).contains("Internal server error"));
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
    }

    @Test
    void testResourceNotFoundExceptionConstructor() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Test message");
        assertEquals("Test message", ex.getMessage());
    }

    @Test
    void testBookNotAvailableExceptionConstructor() {
        BookNotAvailableException ex = new BookNotAvailableException("Test message");
        assertEquals("Test message", ex.getMessage());
    }

    @Test
    void testDuplicateResourceExceptionConstructor() {
        DuplicateResourceException ex = new DuplicateResourceException("Test message");
        assertEquals("Test message", ex.getMessage());
    }
}