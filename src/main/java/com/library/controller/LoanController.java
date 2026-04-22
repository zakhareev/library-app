package com.library.controller;

import com.library.dto.LoanDTO;
import com.library.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@Valid @RequestBody LoanDTO loanDTO) {
        LoanDTO createdLoan = loanService.createLoan(loanDTO);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long id) {
        LoanDTO returnedLoan = loanService.returnBook(id);
        return ResponseEntity.ok(returnedLoan);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id) {
        LoanDTO loan = loanService.getLoanById(id);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<LoanDTO>> getLoansByReader(@PathVariable Long readerId) {
        List<LoanDTO> loans = loanService.getLoansByReader(readerId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoans() {
        List<LoanDTO> loans = loanService.getActiveLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<LoanDTO>> getOverdueLoans() {
        List<LoanDTO> loans = loanService.getOverdueLoans();
        return ResponseEntity.ok(loans);
    }
}