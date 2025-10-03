package com.attsw.bookstore.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.repository.BookRepository;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired private MockMvc mvc;
    @MockitoBean   private BookRepository repo;

    /* GET /api/books */
    @Test
    void shouldReturnAllBooks() throws Exception {
        Book b = new Book();
        b.setId(1L);
        b.setTitle("Clean Code");
        when(repo.findAll()).thenReturn(List.of(b));

        mvc.perform(get("/api/books"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    /* POST /api/books */
    @Test
    void shouldCreateBook() throws Exception {
        Book saved = new Book();
        saved.setId(1L);
        saved.setTitle("Refactoring");

        when(repo.save(any(Book.class))).thenReturn(saved);

        mvc.perform(post("/api/books")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("{\"title\":\"Refactoring\"}"))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id").value(1L));
    }

    /* GET /api/books/{id} */
    @Test
    void shouldReturnSingleBook() throws Exception {
        Book b = new Book();
        b.setId(2L);
        b.setTitle("Effective Java");
        when(repo.findById(2L)).thenReturn(Optional.of(b));

        mvc.perform(get("/api/books/2"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    /* PUT /api/books/{id} */
    @Test
    void shouldUpdateBook() throws Exception {
        Book existing = new Book();
        existing.setId(3L);
        existing.setTitle("Old");

        when(repo.findById(3L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Book.class))).thenReturn(existing); 

        mvc.perform(put("/api/books/3")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("{\"title\":\"New\",\"author\":\"B\",\"isbn\":\"1\"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("New"));
    }

    /* DELETE /api/books/{id} */
    @Test
    void shouldDeleteBook() throws Exception {
        doNothing().when(repo).deleteById(4L);

        mvc.perform(delete("/api/books/4"))
           .andExpect(status().isNoContent());
    }
}