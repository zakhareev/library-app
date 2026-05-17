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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    @Transactional
    public LoanDTO createLoan(LoanDTO loanDTO) {
        Book book = findBookById(loanDTO.getBookId());
        Reader reader = findReaderById(loanDTO.getReaderId());

        if (!book.getAvailable()) {
            throw new BookNotAvailableException("Book is not available for loan");
        }

        if (loanRepository.existsByBookIdAndStatus(book.getId(), "ACTIVE")) {
            throw new BookNotAvailableException("Book is already on loan");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setReader(reader);
        loan.setDueDate(loanDTO.getDueDate());
        loan.setStatus("ACTIVE");

        Loan savedLoan = loanRepository.save(loan);
        return convertToDTO(savedLoan);
    }

    @Transactional
    public LoanDTO returnBook(Long id) {
        Loan loan = findLoanById(id);

        if (!"ACTIVE".equals(loan.getStatus())) {
            throw new IllegalStateException("Loan is not active");
        }

        loan.setReturnDate(LocalDateTime.now());
        loan.setStatus("RETURNED");

        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        Loan updatedLoan = loanRepository.save(loan);
        return convertToDTO(updatedLoan);
    }

    public List<LoanDTO> getLoansByReader(Long readerId) {
        findReaderById(readerId);
        return loanRepository.findByReaderId(readerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> getActiveLoans() {
        return loanRepository.findByStatus("ACTIVE").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LoanDTO> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LoanDTO getLoanById(Long id) {
        Loan loan = findLoanById(id);
        return convertToDTO(loan);
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private Reader findReaderById(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with id: " + id));
    }

    private Loan findLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }

    private LoanDTO convertToDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setBookId(loan.getBook().getId());
        dto.setReaderId(loan.getReader().getId());
        dto.setBookTitle(loan.getBook().getTitle());
        dto.setReaderName(loan.getReader().getFirstName() + " " + loan.getReader().getLastName());
        dto.setLoanDate(loan.getLoanDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setStatus(loan.getStatus());
        return dto;
    }
}