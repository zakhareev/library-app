package com.library.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoanDTO {
    private Long id;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Reader ID is required")
    private Long readerId;

    private String bookTitle;
    private String readerName;
    private LocalDateTime loanDate;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    private LocalDateTime returnDate;
    private String status;
}