package com.example.readingtracker.book;

import com.example.readingtracker.book.exception.BookNotFoundException;
import com.example.readingtracker.book.exception.DuplicateBookException;
import com.example.readingtracker.book.exception.InvalidBookStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        log.info("Getting all books");
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        log.info("Getting book with id {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book with ID: " + id + " not found"
                ));
        return mapToResponse(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getBooksByStatus(BookStatus status) {
        log.info("Getting books by reading status {}", status);
        return bookRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getBooksByAuthor(String author) {
        log.info("Getting books by author {}", author);
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getBooksByTitle(String title) {
        log.info("Getting books by title {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getBooksByRatingRange(BigDecimal minRating, BigDecimal maxRating) {
        log.info("Getting books by rating range {} - {}", minRating, maxRating);
        return bookRepository.findByRatingBetween(minRating, maxRating)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookResponse addBook(BookRequest request) {
        if (bookRepository.existsByTitleAndAuthor(request.getTitle(), request.getAuthor())) {
            throw new DuplicateBookException("Book " + request.getTitle() + " by " + request.getAuthor() + " already exists");
        }
        validatePages(request);
        Book book = new Book();
        applyRequest(book, request);
        return mapToResponse(bookRepository.save(book));
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with ID: " + id + " not found"));
        if (bookRepository.existsByTitleAndAuthorAndIdNot(request.getTitle(), request.getAuthor(), id)) {
            throw new DuplicateBookException("Book " + request.getTitle() + " by " + request.getAuthor() + " already exists");
        }
        validatePages(request);
        applyRequest(book, request);
        return mapToResponse(bookRepository.save(book));
    }
    private void validatePages(BookRequest request){
        if (request.getPagesRead() > request.getTotalPages()){
            throw new InvalidBookStateException("Pages read cannot exceed the total number of pages");
        }
    }

    @CacheEvict(value = "books", allEntries = true)
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book with ID: " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    private void applyRequest(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setStatus(request.getStatus());
        book.setPagesRead(request.getPagesRead());
        book.setTotalPages(request.getTotalPages());
        book.setRating(request.getRating());
        book.setNotes(request.getNotes());
    }

    private  BookResponse mapToResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setStatus(book.getStatus());
        response.setTotalPages(book.getTotalPages());
        response.setPagesRead(book.getPagesRead());
        response.setRating(book.getRating());
        response.setNotes(book.getNotes());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }
}
