package com.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    private Reader reader;

    @BeforeEach
    void setUp() {
        reader = new Reader();
        reader.setId(1L);
        reader.setFirstName("John");
        reader.setLastName("Doe");
        reader.setEmail("john@example.com");
        reader.setPhone("1234567890");
        reader.setAddress("123 Main St");
    }

    @Test
    void testNoArgsConstructor() {
        Reader emptyReader = new Reader();
        assertNotNull(emptyReader);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Reader fullReader = new Reader(2L, "Jane", "Smith", "jane@example.com", "0987654321", "456 Oak St", now);
        assertEquals(2L, fullReader.getId());
        assertEquals("Jane", fullReader.getFirstName());
        assertEquals("Smith", fullReader.getLastName());
        assertEquals("jane@example.com", fullReader.getEmail());
        assertEquals("0987654321", fullReader.getPhone());
        assertEquals("456 Oak St", fullReader.getAddress());
        assertEquals(now, fullReader.getRegisteredDate());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, reader.getId());
        assertEquals("John", reader.getFirstName());
        assertEquals("Doe", reader.getLastName());
        assertEquals("john@example.com", reader.getEmail());
        assertEquals("1234567890", reader.getPhone());
        assertEquals("123 Main St", reader.getAddress());
    }

    @Test
    void testPrePersist() {
        reader.onCreate();
        assertNotNull(reader.getRegisteredDate());
    }

    @Test
    void testEqualsAndHashCode() {
        Reader reader1 = new Reader();
        reader1.setId(1L);
        reader1.setEmail("same@example.com");

        Reader reader2 = new Reader();
        reader2.setId(1L);
        reader2.setEmail("same@example.com");

        Reader reader3 = new Reader();
        reader3.setId(2L);
        reader3.setEmail("different@example.com");

        assertEquals(reader1, reader2);
        assertNotEquals(reader1, reader3);
        assertEquals(reader1.hashCode(), reader2.hashCode());
    }

    @Test
    void testToString() {
        String toString = reader.toString();
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("john@example.com"));
    }

    @Test
    void testSetEmail() {
        reader.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", reader.getEmail());
    }

    @Test
    void testSetPhone() {
        reader.setPhone("5555555555");
        assertEquals("5555555555", reader.getPhone());
    }

    @Test
    void testSetAddress() {
        reader.setAddress("New Address");
        assertEquals("New Address", reader.getAddress());
    }
}