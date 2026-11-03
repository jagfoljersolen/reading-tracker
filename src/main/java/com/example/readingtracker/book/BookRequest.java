package com.example.readingtracker.book;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 200, message = "Title must have between 2 and 200 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 2, max = 200, message = "Author must have between 2 and 200 characters")
    private String author;

    @NotNull(message = "Status is required")
    private BookStatus status;

    @NotNull(message = "Total pages are required")
    @Min(value = 1, message = "Total pages must be at least 1")
    private Integer totalPages;

    @NotNull(message = "Pages read cannot be null")
    @Min(value = 0, message = "Pages read cannot be negative")
    private Integer pagesRead = 0;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating cannot be higher than 5.0")
    @Digits(integer = 1, fraction = 1, message = "Rating must have one digit and at most one decimal place" )
    private BigDecimal rating;

    @Size(max=9000, message = "Notes cannot be longer than 9000 characters")
    private String notes;
}
