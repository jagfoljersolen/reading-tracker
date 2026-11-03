package com.example.readingtracker.book;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "books",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "author"}))
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 200, message = "Title must have between 2 and 200 characters")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 2, max = 200, message = "Author must have between 2 and 200 characters")
    @Column(nullable = false, length = 200)
    private String author;

    @NotNull(message = "Reading status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @NotNull(message = "Total pages is required")
    @Min(value = 1, message = "Total pages must be at least 1")
    @Column(nullable = false)
    private Integer totalPages;

    @Min(value = 0, message = "Pages read cannot be negative")
    @Column(nullable = false)
    private Integer pagesRead = 0;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    @Digits(integer = 1, fraction = 1, message = "Rating format: one digit, one decimal place")
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @Size(max = 9000, message = "Notes can have at most 9000 characters")
    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}