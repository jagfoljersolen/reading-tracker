package com.example.readingtracker.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Books", description = "Reading List API")
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books")
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.info("GET /api/v1/books - Loading all books");
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        log.info("GET /api/v1/books/{} - Loading book", id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get books by reading status")
    public ResponseEntity<List<BookResponse>> getBooksByStatus(@PathVariable BookStatus status) {
        log.info("GET /api/v1/books/status/{}", status);
        return ResponseEntity.ok(bookService.getBooksByStatus(status));
    }

    @GetMapping("/author/{author}")
    @Operation(summary = "Get books by author (partial match)")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(@PathVariable String author) {
        log.info("GET /api/v1/books/author/{}", author);
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by title (partial match)")
    public ResponseEntity<List<BookResponse>> searchBooks(@RequestParam String title) {
        log.info("GET /api/v1/books/search?title={}", title);
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @GetMapping("/rating-range")
    @Operation(summary = "Get books within a rating range")
    public ResponseEntity<List<BookResponse>> getBooksByRatingRange(
            @RequestParam BigDecimal minRating,
            @RequestParam BigDecimal maxRating) {
        log.info("GET /api/v1/books/rating-range?minRating={}&maxRating={}", minRating, maxRating);
        return ResponseEntity.ok(bookService.getBooksByRatingRange(minRating, maxRating));
    }

    @PostMapping
    @Operation(summary = "Add a new book")
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookRequest request) {
        log.info("POST /api/v1/books - Adding book {}", request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        log.info("PUT /api/v1/books/{} - Updating book", id);
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("DELETE /api/v1/books/{} - Deleting book", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}