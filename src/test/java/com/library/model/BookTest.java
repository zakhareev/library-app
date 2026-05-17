package com.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setGenre("Fiction");
        book.setIsbn("1234567890");
        book.setPublicationYear(2024);
        book.setAvailable(true);
    }

    @Test
    void testNoArgsConstructor() {
        Book emptyBook = new Book();
        assertNotNull(emptyBook);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Book fullBook = new Book(2L, "Title", "Author", "Genre", "ISBN", 2023, false, now, now);
        assertEquals(2L, fullBook.getId());
        assertEquals("Title", fullBook.getTitle());
        assertEquals("Author", fullBook.getAuthor());
        assertEquals("Genre", fullBook.getGenre());
        assertEquals("ISBN", fullBook.getIsbn());
        assertEquals(2023, fullBook.getPublicationYear());
        assertFalse(fullBook.getAvailable());
        assertEquals(now, fullBook.getCreatedAt());
        assertEquals(now, fullBook.getUpdatedAt());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, book.getId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("Fiction", book.getGenre());
        assertEquals("1234567890", book.getIsbn());
        assertEquals(2024, book.getPublicationYear());
        assertTrue(book.getAvailable());
    }

    @Test
    void testPrePersist() {
        book.onCreate();
        assertNotNull(book.getCreatedAt());
        assertNotNull(book.getUpdatedAt());
    }

    @Test
    void testPreUpdate() {
        LocalDateTime beforeUpdate = LocalDateTime.now();
        book.onCreate();
        book.onUpdate();
        assertNotNull(book.getUpdatedAt());
        assertTrue(book.getUpdatedAt().isAfter(beforeUpdate) || book.getUpdatedAt().equals(beforeUpdate));
    }

    @Test
    void testEqualsAndHashCode() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Same Title");

        Book book2 = new Book();
        book2.setId(1L);
        book2.setTitle("Same Title");

        Book book3 = new Book();
        book3.setId(2L);
        book3.setTitle("Different Title");

        assertEquals(book1, book2);
        assertNotEquals(book1, book3);
        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void testToString() {
        String toString = book.toString();
        assertTrue(toString.contains("Test Book"));
        assertTrue(toString.contains("Test Author"));
    }

    @Test
    void testSetAvailable() {
        book.setAvailable(false);
        assertFalse(book.getAvailable());

        book.setAvailable(true);
        assertTrue(book.getAvailable());
    }

    @Test
    void testSetTitleNull() {
        book.setTitle(null);
        assertNull(book.getTitle());
    }

    @Test
    void testSetAuthorNull() {
        book.setAuthor(null);
        assertNull(book.getAuthor());
    }
}