package com.library.repository;

import com.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReaderId(Long readerId);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByStatus(String status);

    @Query("SELECT l FROM Loan l WHERE l.dueDate < :date AND l.status = 'ACTIVE'")
    List<Loan> findOverdueLoans(@Param("date") LocalDateTime date);

    boolean existsByBookIdAndStatus(Long bookId, String status);
}