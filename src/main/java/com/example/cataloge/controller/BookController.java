package com.example.cataloge.controller;

import com.example.cataloge.entity.Book;
import com.example.cataloge.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/books")
@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping(consumes = "application/json", produces = "application/json")
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
    @GetMapping(path = "/search/{searchTerm}")
    public List<Book> searchBook(@PathVariable String searchTerm){
        List<Book> searchResult = bookRepository.searchBooks(searchTerm);
        return searchResult;
    }

    @GetMapping
    public List<Book> getAllBooks(){
        List<Book> allBooks = bookRepository.findAll();
        return allBooks;
    }

    @GetMapping(path = "{id}")
    public Optional<Book> getBookById(@PathVariable Long id){
        Optional<Book> book = bookRepository.findById(id);
        return book;
    }
}
