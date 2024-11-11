package com.example.cataloge;

import com.example.cataloge.entity.Book;
import com.example.cataloge.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogeApplication {
    @Autowired
    BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(CatalogeApplication.class, args);
    }

    @PostConstruct
    private void initializeBook() throws Exception{
        Book book1 = new Book();
        book1.setTitle("Start with WHY");
        book1.setAuthor("Simon Sinek");
        book1.setDescription("In this book, Simon shows that the leaders who’ve had the greatest influence in the world all think, act, and communicate the same way—and it’s the opposite of what everyone else does.");
        book1.setISBN("978-0241958223");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Give and Take");
        book2.setAuthor("Adam Grant");
        book2.setDescription("A Revolutionary Approach to Success, Adam Grant categorizes people into three types: givers, matchers, and takers.");
        book2.setISBN("978-0670026555");
        bookRepository.save(book2);

    }

}
