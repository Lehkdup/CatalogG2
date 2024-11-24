package com.example.cataloge;

import com.example.cataloge.entity.Book;
import com.example.cataloge.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookRepositorySearchTest {

    @Mock
    private BookRepository bookRepository;

    @Test
    void addBookToDatabaseAndShouldBeFound(){
        Book book1 = new Book();
        book1.setTitle("Start with WHY");
        book1.setAuthor("Simon Sinek");
        book1.setDescription("In this book, Simon shows that the leaders who’ve had the greatest influence in the world all think, act, and communicate the same way—and it’s the opposite of what everyone else does.");
        book1.setISBN("978-0241958223");

        Book book2 = new Book();
        book2.setTitle("Give and Take");
        book2.setAuthor("Adam Grant");
        book2.setDescription("A Revolutionary Approach to Success, Adam Grant categorizes people into three types: givers, matchers, and takers.");
        book2.setISBN("978-0670026555");

        Book book3 = new Book();
        book3.setTitle("Rich Dad Poor Dad");
        book3.setAuthor("Robert Kiyosaki and Sharon L. Lechter\n");
        book3.setDescription("Rich Dad Poor Dad... Explodes the myth that you need to earn a high income to become rich Challenges the belief that your house is an asset");
        book3.setISBN("978-1612680194");

        List<Book> books = List.of(book1, book2, book3);

        when(bookRepository.searchBooks("dad")).thenReturn(books);
        when(bookRepository.saveAll(books)).thenReturn(books);

        bookRepository.saveAll(books);

        List<Book> searchResult = bookRepository.searchBooks("dad");
        System.out.println(searchResult.size());
        List<Book> filteredResult = searchResult.stream()
                .filter(bookToFind -> bookToFind.getISBN().equals(book3.getISBN()))
                .toList();
        System.out.println(filteredResult.size());
        assertEquals(filteredResult.get(0).getISBN(), book3.getISBN());
    }
}
