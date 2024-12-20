package com.example.cataloge;

import com.example.cataloge.controller.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Lädt die Spring-Boot-Anwendung mit der Test-Datenbank
class BookControllerIntegrationTest {

    @Autowired
    private BookController bookController;

    private MockMvc mockMvc;

    @Test
    void shouldReturnAllBooks() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        // HTTP GET Anfrage ausführen
        mockMvc.perform(get("/books") // GET /books
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON prüfen
                .andExpect(jsonPath("$.size()").value(2)) // Erwartet 2 Bücher
                .andExpect(jsonPath("$[0].title").value("Start with WHY")) // Buch 1 Titel prüfen
                .andExpect(jsonPath("$[1].title").value("Give and Take")); // Buch 2 Titel prüfen
    }

    @Test
    void shouldReturnBookById() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        // HTTP GET Anfrage ausführen
        mockMvc.perform(get("/books/1") // GET /books/1
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON prüfen
                .andExpect(jsonPath("$.id").value(1)) // Buch 1 ID prüfen
                .andExpect(jsonPath("$.title").value("Start with WHY")) // Buch 1 Titel prüfen
                .andExpect(jsonPath("$.author").value("Simon Sinek")) // Buch 1 Autor prüfen
                .andExpect(jsonPath("$.isbn").value("978-0241958223")); // ISBN prüfen
    }

    @Test
    void shouldReturnBooksBySearchTerm() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        // HTTP GET Anfrage ausführen
        mockMvc.perform(get("/books/search/Start") // GET /books/search/Start
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON prüfen
                .andExpect(jsonPath("$.size()").value(1)) // Erwartet 1 Buch
                .andExpect(jsonPath("$[0].title").value("Start with WHY")) // Buch 1 Titel prüfen
                .andExpect(jsonPath("$[0].author").value("Simon Sinek")); // Buch 1 Autor prüfen
    }
}