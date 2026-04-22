package com.library.service;

import com.library.dto.LoanDTO;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Reader;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Transactional
    public LoanDTO createLoan(LoanDTO loanDTO) {
        Book book = bookRepository.findById(loanDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + loanDTO.getBookId()));

        Reader reader = readerRepository.findById(loanDTO.getReaderId())
                .orElseThrow(() -> new RuntimeException("Reader not found with id: " + loanDTO.getReaderId()));

        if (!book.getAvailable()) {
            throw new RuntimeException("Book is not available for loan");
        }

        if (loanRepository.existsByBookIdAndStatus(book.getId(), "ACTIVE")) {
            throw new RuntimeException("Book is already on loan");
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
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

        if (!"ACTIVE".equals(loan.getStatus())) {
            throw new RuntimeException("Loan is not active");
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
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        return convertToDTO(loan);
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