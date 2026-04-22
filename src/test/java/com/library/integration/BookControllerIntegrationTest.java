package com.library.integration;

import com.library.dto.BookDTO;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class BookControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void createBook_ShouldReturnCreatedBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Integration Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setGenre("Test Genre");
        bookDTO.setIsbn("9876543210");
        bookDTO.setPublicationYear(2024);

        ResponseEntity<BookDTO> response = restTemplate.postForEntity("/api/books", bookDTO, BookDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Integration Test Book");
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        Book book = new Book();
        book.setTitle("Test Book 1");
        book.setAuthor("Author 1");
        book.setGenre("Genre 1");
        book.setAvailable(true);
        bookRepository.save(book);

        ResponseEntity<BookDTO[]> response = restTemplate.getForEntity("/api/books", BookDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void getBookById_ShouldReturnBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setGenre("Test Genre");
        Book savedBook = bookRepository.save(book);

        ResponseEntity<BookDTO> response = restTemplate.getForEntity("/api/books/" + savedBook.getId(), BookDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Book");
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        Book book = new Book();
        book.setTitle("Original Title");
        book.setAuthor("Original Author");
        Book savedBook = bookRepository.save(book);

        BookDTO updateDTO = new BookDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setAuthor("Updated Author");

        HttpEntity<BookDTO> requestEntity = new HttpEntity<>(updateDTO);
        ResponseEntity<BookDTO> response = restTemplate.exchange(
                "/api/books/" + savedBook.getId(),
                HttpMethod.PUT,
                requestEntity,
                BookDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void deleteBook_ShouldReturnNoContent() {
        Book book = new Book();
        book.setTitle("Book to Delete");
        book.setAuthor("Author");
        Book savedBook = bookRepository.save(book);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/books/" + savedBook.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.findById(savedBook.getId())).isEmpty();
    }

    @Test
    void searchBooks_ShouldReturnMatchingBooks() {
        Book book1 = new Book();
        book1.setTitle("Spring Framework Guide");
        book1.setAuthor("John Spring");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Java Programming");
        book2.setAuthor("Jane Java");
        bookRepository.save(book2);

        ResponseEntity<BookDTO[]> response = restTemplate.getForEntity("/api/books/search?keyword=Spring", BookDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getTitle()).contains("Spring");
    }
}