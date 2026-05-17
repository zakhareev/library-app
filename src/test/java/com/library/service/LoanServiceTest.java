package com.library.service;

import com.library.dto.LoanDTO;
import com.library.exception.BookNotAvailableException;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Reader;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private LoanService loanService;

    private Book book;
    private Reader reader;
    private Loan loan;
    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setAvailable(true);

        reader = new Reader();
        reader.setId(1L);
        reader.setFirstName("John");
        reader.setLastName("Doe");
        reader.setEmail("john@example.com");

        loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setReader(reader);
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        loan.setStatus("ACTIVE");

        loanDTO = new LoanDTO();
        loanDTO.setBookId(1L);
        loanDTO.setReaderId(1L);
        loanDTO.setDueDate(LocalDateTime.now().plusDays(14));
    }

    @Test
    void createLoan_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(loanRepository.existsByBookIdAndStatus(1L, "ACTIVE")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanDTO result = loanService.createLoan(loanDTO);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
        verify(bookRepository, times(1)).findById(1L);
        verify(readerRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void createLoan_BookNotFound_ThrowsException() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        loanDTO.setBookId(999L);

        assertThatThrownBy(() -> loanService.createLoan(loanDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found with id: 999");
    }

    @Test
    void createLoan_ReaderNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(999L)).thenReturn(Optional.empty());
        loanDTO.setReaderId(999L);

        assertThatThrownBy(() -> loanService.createLoan(loanDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Reader not found with id: 999");
    }

    @Test
    void createLoan_BookNotAvailable_ThrowsException() {
        book.setAvailable(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));

        assertThatThrownBy(() -> loanService.createLoan(loanDTO))
                .isInstanceOf(BookNotAvailableException.class)
                .hasMessageContaining("Book is not available");
    }

    @Test
    void createLoan_BookAlreadyOnLoan_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(loanRepository.existsByBookIdAndStatus(1L, "ACTIVE")).thenReturn(true);

        assertThatThrownBy(() -> loanService.createLoan(loanDTO))
                .isInstanceOf(BookNotAvailableException.class)
                .hasMessageContaining("already on loan");
    }

    @Test
    void returnBook_Success() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanDTO result = loanService.returnBook(1L);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("RETURNED");
        assertThat(result.getReturnDate()).isNotNull();
        verify(loanRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void returnBook_LoanNotFound_ThrowsException() {
        when(loanRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanService.returnBook(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Loan not found with id: 999");
    }

    @Test
    void getLoansByReader_Success() {
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(loanRepository.findByReaderId(1L)).thenReturn(Arrays.asList(loan));

        List<LoanDTO> result = loanService.getLoansByReader(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookTitle()).isEqualTo("Test Book");
        verify(readerRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).findByReaderId(1L);
    }

    @Test
    void getActiveLoans_Success() {
        when(loanRepository.findByStatus("ACTIVE")).thenReturn(Arrays.asList(loan));

        List<LoanDTO> result = loanService.getActiveLoans();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("ACTIVE");
        verify(loanRepository, times(1)).findByStatus("ACTIVE");
    }

    @Test
    void getOverdueLoans_Success() {
        when(loanRepository.findOverdueLoans(any(LocalDateTime.class))).thenReturn(Arrays.asList(loan));

        List<LoanDTO> result = loanService.getOverdueLoans();

        assertThat(result).hasSize(1);
        verify(loanRepository, times(1)).findOverdueLoans(any(LocalDateTime.class));
    }

    @Test
    void getLoanById_Success() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoanDTO result = loanService.getLoanById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(loanRepository, times(1)).findById(1L);
    }
}