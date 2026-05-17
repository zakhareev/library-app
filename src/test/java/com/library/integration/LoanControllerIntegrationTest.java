package com.library.integration;

import com.library.dto.LoanDTO;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Reader;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class LoanControllerIntegrationTest {

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

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private LoanRepository loanRepository;

    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        readerRepository.deleteAll();

        book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setAvailable(true);
        book = bookRepository.save(book);

        reader = new Reader();
        reader.setFirstName("Test");
        reader.setLastName("Reader");
        reader.setEmail("test@example.com");
        reader = readerRepository.save(reader);
    }

    @Test
    void createLoan_ShouldReturnCreatedLoan() {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId(book.getId());
        loanDTO.setReaderId(reader.getId());
        loanDTO.setDueDate(LocalDateTime.now().plusDays(14));

        ResponseEntity<LoanDTO> response = restTemplate.postForEntity("/api/loans", loanDTO, LoanDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo("ACTIVE");

        Book updatedBook = bookRepository.findById(book.getId()).get();
        assertThat(updatedBook.getAvailable()).isFalse();
    }

    @Test
    void returnBook_ShouldUpdateLoanStatus() {
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setReader(reader);
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        loan.setStatus("ACTIVE");
        loan = loanRepository.save(loan);

        book.setAvailable(false);
        bookRepository.save(book);

        ResponseEntity<LoanDTO> response = restTemplate.exchange(
                "/api/loans/" + loan.getId() + "/return",
                HttpMethod.PUT,
                null,
                LoanDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo("RETURNED");
        assertThat(response.getBody().getReturnDate()).isNotNull();

        Book updatedBook = bookRepository.findById(book.getId()).get();
        assertThat(updatedBook.getAvailable()).isTrue();
    }

    @Test
    void getActiveLoans_ShouldReturnList() {
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setReader(reader);
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        loan.setStatus("ACTIVE");
        loanRepository.save(loan);

        ResponseEntity<LoanDTO[]> response = restTemplate.getForEntity("/api/loans/active", LoanDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void createLoan_WithUnavailableBook_ShouldReturnBadRequest() {
        book.setAvailable(false);
        bookRepository.save(book);

        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setBookId(book.getId());
        loanDTO.setReaderId(reader.getId());
        loanDTO.setDueDate(LocalDateTime.now().plusDays(14));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/loans", loanDTO, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Book is not available");
    }
}