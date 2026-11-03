package com.example.readingtracker.book;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private BookStatus status;
    private Integer totalPages;
    private Integer pagesRead;
    private BigDecimal rating;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
