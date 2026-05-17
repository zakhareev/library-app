package com.library.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import static org.junit.jupiter.api.Assertions.*;

class BookDTOTest {

    private Validator validator;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setGenre("Fiction");
        bookDTO.setIsbn("1234567890");
        bookDTO.setPublicationYear(2024);
        bookDTO.setAvailable(true);
    }

    @Test
    void testNoArgsConstructor() {
        BookDTO emptyDTO = new BookDTO();
        assertNotNull(emptyDTO);
    }

    @Test
    void testAllArgsConstructor() {
        BookDTO fullDTO = new BookDTO();
        fullDTO.setId(2L);
        fullDTO.setTitle("Title");
        fullDTO.setAuthor("Author");
        fullDTO.setGenre("Genre");
        fullDTO.setIsbn("ISBN");
        fullDTO.setPublicationYear(2023);
        fullDTO.setAvailable(false);

        assertEquals(2L, fullDTO.getId());
        assertEquals("Title", fullDTO.getTitle());
        assertEquals("Author", fullDTO.getAuthor());
        assertEquals("Genre", fullDTO.getGenre());
        assertEquals("ISBN", fullDTO.getIsbn());
        assertEquals(2023, fullDTO.getPublicationYear());
        assertFalse(fullDTO.getAvailable());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, bookDTO.getId());
        assertEquals("Test Book", bookDTO.getTitle());
        assertEquals("Test Author", bookDTO.getAuthor());
        assertEquals("Fiction", bookDTO.getGenre());
        assertEquals("1234567890", bookDTO.getIsbn());
        assertEquals(2024, bookDTO.getPublicationYear());
        assertTrue(bookDTO.getAvailable());
    }

    @Test
    void testValidationSuccess() {
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenTitleIsNull() {
        bookDTO.setTitle(null);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenAuthorIsNull() {
        bookDTO.setAuthor(null);
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidationTitleMaxLength() {
        bookDTO.setTitle("a".repeat(256));
        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().contains("must be between 1 and 255"));
    }

    @Test
    void testEqualsAndHashCode() {
        BookDTO dto1 = new BookDTO();
        dto1.setId(1L);
        dto1.setTitle("Same");

        BookDTO dto2 = new BookDTO();
        dto2.setId(1L);
        dto2.setTitle("Same");

        BookDTO dto3 = new BookDTO();
        dto3.setId(2L);
        dto3.setTitle("Different");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        String toString = bookDTO.toString();
        assertTrue(toString.contains("Test Book"));
        assertTrue(toString.contains("Test Author"));
    }
}