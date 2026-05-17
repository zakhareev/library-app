package com.library.controller;

import com.library.dto.LoanDTO;
import com.library.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        loanDTO = new LoanDTO();
        loanDTO.setId(1L);
        loanDTO.setBookId(1L);
        loanDTO.setReaderId(1L);
        loanDTO.setBookTitle("Test Book");
        loanDTO.setReaderName("John Doe");
        loanDTO.setLoanDate(LocalDateTime.now());
        loanDTO.setDueDate(LocalDateTime.now().plusDays(14));
        loanDTO.setStatus("ACTIVE");
    }

    @Test
    void createLoan_ShouldReturnCreated() {
        when(loanService.createLoan(any(LoanDTO.class))).thenReturn(loanDTO);

        ResponseEntity<LoanDTO> response = loanController.createLoan(loanDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo("ACTIVE");
        verify(loanService, times(1)).createLoan(any(LoanDTO.class));
    }

    @Test
    void returnBook_ShouldReturnOk() {
        when(loanService.returnBook(1L)).thenReturn(loanDTO);

        ResponseEntity<LoanDTO> response = loanController.returnBook(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(loanService, times(1)).returnBook(1L);
    }

    @Test
    void getLoanById_ShouldReturnOk() {
        when(loanService.getLoanById(1L)).thenReturn(loanDTO);

        ResponseEntity<LoanDTO> response = loanController.getLoanById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(loanService, times(1)).getLoanById(1L);
    }

    @Test
    void getLoansByReader_ShouldReturnOk() {
        when(loanService.getLoansByReader(1L)).thenReturn(Arrays.asList(loanDTO));

        ResponseEntity<List<LoanDTO>> response = loanController.getLoansByReader(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(loanService, times(1)).getLoansByReader(1L);
    }

    @Test
    void getActiveLoans_ShouldReturnOk() {
        when(loanService.getActiveLoans()).thenReturn(Arrays.asList(loanDTO));

        ResponseEntity<List<LoanDTO>> response = loanController.getActiveLoans();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(loanService, times(1)).getActiveLoans();
    }

    @Test
    void getOverdueLoans_ShouldReturnOk() {
        when(loanService.getOverdueLoans()).thenReturn(Arrays.asList(loanDTO));

        ResponseEntity<List<LoanDTO>> response = loanController.getOverdueLoans();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(loanService, times(1)).getOverdueLoans();
    }
}