package com.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    private Loan loan;
    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        reader = new Reader();
        reader.setId(1L);
        reader.setFirstName("John");

        loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setReader(reader);
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        loan.setStatus("ACTIVE");
    }

    @Test
    void testNoArgsConstructor() {
        Loan emptyLoan = new Loan();
        assertNotNull(emptyLoan);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Loan fullLoan = new Loan(2L, book, reader, now, now.plusDays(7), null, "RETURNED");
        assertEquals(2L, fullLoan.getId());
        assertEquals(book, fullLoan.getBook());
        assertEquals(reader, fullLoan.getReader());
        assertEquals(now, fullLoan.getLoanDate());
        assertEquals(now.plusDays(7), fullLoan.getDueDate());
        assertNull(fullLoan.getReturnDate());
        assertEquals("RETURNED", fullLoan.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, loan.getId());
        assertEquals(book, loan.getBook());
        assertEquals(reader, loan.getReader());
        assertNotNull(loan.getDueDate());
        assertEquals("ACTIVE", loan.getStatus());
    }

    @Test
    void testPrePersist() {
        loan.onCreate();
        assertNotNull(loan.getLoanDate());
    }

    @Test
    void testSetReturnDate() {
        LocalDateTime returnDate = LocalDateTime.now();
        loan.setReturnDate(returnDate);
        assertEquals(returnDate, loan.getReturnDate());
    }

    @Test
    void testSetStatus() {
        loan.setStatus("RETURNED");
        assertEquals("RETURNED", loan.getStatus());

        loan.setStatus("OVERDUE");
        assertEquals("OVERDUE", loan.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Loan loan1 = new Loan();
        loan1.setId(1L);

        Loan loan2 = new Loan();
        loan2.setId(1L);

        Loan loan3 = new Loan();
        loan3.setId(2L);

        assertEquals(loan1, loan2);
        assertNotEquals(loan1, loan3);
        assertEquals(loan1.hashCode(), loan2.hashCode());
    }

    @Test
    void testToString() {
        String toString = loan.toString();
        assertTrue(toString.contains("ACTIVE"));
        assertTrue(toString.contains("Test Book"));
    }

    @Test
    void testIsOverdue() {
        loan.setDueDate(LocalDateTime.now().minusDays(1));
        assertTrue(loan.getDueDate().isBefore(LocalDateTime.now()));

        loan.setDueDate(LocalDateTime.now().plusDays(1));
        assertTrue(loan.getDueDate().isAfter(LocalDateTime.now()));
    }
}