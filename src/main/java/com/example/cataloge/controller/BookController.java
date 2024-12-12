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
    public ResponseEntity createBook(@RequestBody Book book){
        System.out.println(book.getISBN());
        if(book.getISBN() == null){
            return ResponseEntity.badRequest().body("insufficient content provided");
        } else {
            if(book.getISBN().equals(bookRepository.findBookByISBN(book.getISBN()))){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Book with this ISBN already exists");
            } else {
                book = bookRepository.save(book);
                return ResponseEntity.ok(book);
            }
        }
    }
    private String fallbackCreateBook(Book book, Throwable throwable){
        return "an error occured while creating the book " + book.getTitle();
    }

    @GetMapping(path = "/search/{searchTerm}")
    @Retry(name = "retryApi", fallbackMethod = "fallbackSearchBook")
    public List<Book> searchBook(@PathVariable String searchTerm){
        List<Book> searchResult = bookRepository.searchBooks(searchTerm);
        return searchResult;
    }
    private String fallbackSearchBook(String searchTerm, Throwable throwable){
        return "an error occured while searching for: " + searchTerm;
    }

    @GetMapping
    @Retry(name = "retryApi", fallbackMethod = "fallbackAfterRetry")
    public List<Book> getAllBooks(){
        List<Book> allBooks = bookRepository.findAll();
        return allBooks;
    }

    @GetMapping(path = "{id}")
    @Retry(name = "retryApi", fallbackMethod = "fallbackBookById")
    public Optional<Book> getBookById(@PathVariable Long id){
        Optional<Book> book = bookRepository.findById(id);
        return book;
    }
    private String fallbackBookById(Long id, Throwable throwable){
        return "an error occured while getting your book with id: " + id;
    }

    @PostMapping(path = "/inCart")
    @Retry(name = "retryApi", fallbackMethod = "fallbackBooksInCart")
    public ResponseEntity getBooksInCart(@RequestBody GetBooksInCartWrapper requestWrapper){
        List<Long> bookIdsInCart = requestWrapper.getBookIds();
        List<Book> booksInCart = bookRepository.findAllById(bookIdsInCart);
        return ResponseEntity.ok(booksInCart);
    }
    private String fallbackBooksInCart(GetBooksInCartWrapper requestWrapper, Throwable throwable){
        return "an error occured while retrieving your cart " + requestWrapper.getBookIds().toString();
    }

    private String fallbackAfterRetry(Exception exception){
        return "an error occured, please try again later";
    }
}
