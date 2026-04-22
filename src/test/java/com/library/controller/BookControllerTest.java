package com.library.controller;

import com.library.dto.BookDTO;
import com.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
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
    void createBook_ShouldReturnCreated() {
        when(bookService.createBook(any(BookDTO.class))).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.createBook(bookDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Book");
        verify(bookService, times(1)).createBook(any(BookDTO.class));
    }

    @Test
    void getAllBooks_ShouldReturnOk() {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(bookDTO));

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getBookById_ShouldReturnOk() {
        when(bookService.getBookById(1L)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.getBookById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void updateBook_ShouldReturnOk() {
        when(bookService.updateBook(eq(1L), any(BookDTO.class))).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.updateBook(1L, bookDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(bookService, times(1)).updateBook(eq(1L), any(BookDTO.class));
    }

    @Test
    void deleteBook_ShouldReturnNoContent() {
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void searchBooks_ShouldReturnOk() {
        when(bookService.searchBooks("test")).thenReturn(Arrays.asList(bookDTO));

        ResponseEntity<List<BookDTO>> response = bookController.searchBooks("test");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(bookService, times(1)).searchBooks("test");
    }

    @Test
    void getAvailableBooks_ShouldReturnOk() {
        when(bookService.getAvailableBooks()).thenReturn(Arrays.asList(bookDTO));

        ResponseEntity<List<BookDTO>> response = bookController.getAvailableBooks();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(bookService, times(1)).getAvailableBooks();
    }
}