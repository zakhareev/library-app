package com.library.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class ReaderDTOTest {

    private Validator validator;
    private ReaderDTO readerDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        readerDTO = new ReaderDTO();
        readerDTO.setId(1L);
        readerDTO.setFirstName("John");
        readerDTO.setLastName("Doe");
        readerDTO.setEmail("john@example.com");
        readerDTO.setPhone("1234567890");
        readerDTO.setAddress("123 Main St");
    }

    @Test
    void testNoArgsConstructor() {
        ReaderDTO emptyDTO = new ReaderDTO();
        assertNotNull(emptyDTO);
    }

    @Test
    void testAllArgsConstructor() {
        ReaderDTO fullDTO = new ReaderDTO();
        fullDTO.setId(2L);
        fullDTO.setFirstName("Jane");
        fullDTO.setLastName("Smith");
        fullDTO.setEmail("jane@example.com");
        fullDTO.setPhone("0987654321");
        fullDTO.setAddress("456 Oak St");

        assertEquals(2L, fullDTO.getId());
        assertEquals("Jane", fullDTO.getFirstName());
        assertEquals("Smith", fullDTO.getLastName());
        assertEquals("jane@example.com", fullDTO.getEmail());
        assertEquals("0987654321", fullDTO.getPhone());
        assertEquals("456 Oak St", fullDTO.getAddress());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, readerDTO.getId());
        assertEquals("John", readerDTO.getFirstName());
        assertEquals("Doe", readerDTO.getLastName());
        assertEquals("john@example.com", readerDTO.getEmail());
        assertEquals("1234567890", readerDTO.getPhone());
        assertEquals("123 Main St", readerDTO.getAddress());
    }

    @Test
    void testValidationSuccess() {
        Set<ConstraintViolation<ReaderDTO>> violations = validator.validate(readerDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenFirstNameIsNull() {
        readerDTO.setFirstName(null);
        Set<ConstraintViolation<ReaderDTO>> violations = validator.validate(readerDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenLastNameIsNull() {
        readerDTO.setLastName(null);
        Set<ConstraintViolation<ReaderDTO>> violations = validator.validate(readerDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenEmailIsInvalid() {
        readerDTO.setEmail("invalid-email");
        Set<ConstraintViolation<ReaderDTO>> violations = validator.validate(readerDTO);
        assertFalse(violations.isEmpty());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    void testValidationFailsWhenEmailIsNull() {
        readerDTO.setEmail(null);
        Set<ConstraintViolation<ReaderDTO>> violations = validator.validate(readerDTO);
        assertFalse(violations.isEmpty());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ReaderDTO dto1 = new ReaderDTO();
        dto1.setId(1L);
        dto1.setEmail("same@example.com");

        ReaderDTO dto2 = new ReaderDTO();
        dto2.setId(1L);
        dto2.setEmail("same@example.com");

        ReaderDTO dto3 = new ReaderDTO();
        dto3.setId(2L);
        dto3.setEmail("different@example.com");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        String toString = readerDTO.toString();
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("john@example.com"));
    }
}