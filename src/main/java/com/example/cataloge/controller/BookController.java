package com.example.cataloge.controller;

import com.example.cataloge.entity.Book;
import com.example.cataloge.repository.BookRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/books")
@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Retry(name = "retryApi", fallbackMethod = "fallbackAfterRetry")
    public ResponseEntity createBook(@RequestBody Book book) {
        System.out.println(book.getISBN());
        if (book.getISBN() == null) {
            return ResponseEntity.badRequest().body("insufficient content provided");
        } else {
            if (book.getISBN().equals(bookRepository.findBookByISBN(book.getISBN()))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Book with this ISBN already exists");
            } else {
                book = bookRepository.save(book);
                return ResponseEntity.ok(book);
            }
        }
    }

    private ResponseEntity fallbackCreateBook(Book book, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("an error occured while creating the book " + book.getTitle());
    }

    @GetMapping(path = "/search/{searchTerm}")
    @Retry(name = "retryApi", fallbackMethod = "fallbackSearchBook")
    public List<Book> searchBook(@PathVariable String searchTerm) {
        List<Book> searchResult = bookRepository.searchBooks(searchTerm);
        return searchResult;
    }

    private List<Book> fallbackSearchBook(String searchTerm, Throwable throwable) {
        return new ArrayList<>();
    }

    @GetMapping
    @Retry(name = "retryApi", fallbackMethod = "fallbackGetAllBooks")
    public List<Book> getAllBooks() {
        List<Book> allBooks = bookRepository.findAll();
        return allBooks;
    }

    private List<Book> fallbackGetAllBooks(String searchTerm, Throwable throwable) {
        return new ArrayList<>();
    }

    @GetMapping(path = "{id}")
    @Retry(name = "retryApi", fallbackMethod = "fallbackBookById")
    public Optional<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book;
    }

    private Optional<Book> fallbackBookById(Long id, Throwable throwable) {
        return Optional.empty();
    }

    @PostMapping(path = "/inCart")
    @Retry(name = "retryApi", fallbackMethod = "fallbackBooksInCart")
    public ResponseEntity getBooksInCart(@RequestBody GetBooksInCartWrapper requestWrapper) {
        List<Long> bookIdsInCart = requestWrapper.getBookIds();
        List<Book> booksInCart = bookRepository.findAllById(bookIdsInCart);
        return ResponseEntity.ok(booksInCart);
    }

    private ResponseEntity fallbackBooksInCart(GetBooksInCartWrapper requestWrapper, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("an error occured while retrieving your cart " + requestWrapper.getBookIds().toString());
    }
}
