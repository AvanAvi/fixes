package com.attsw.bookstore.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.service.BookService;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired private MockMvc mvc;
    @MockitoBean private BookService bookService;

    /* GET /api/books */
    @Test
    void shouldReturnAllBooks() throws Exception {
        Book b = new Book();
        b.setId(1L);
        b.setTitle("Clean Code");
        when(bookService.getAllBooks()).thenReturn(List.of(b));

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

        when(bookService.saveBook(any(Book.class))).thenReturn(saved);

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
        when(bookService.getBookById(2L)).thenReturn(b);

        mvc.perform(get("/api/books/2"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    /* PUT /api/books/{id} */
    @Test
    void shouldUpdateBook() throws Exception {
        Book updated = new Book();
        updated.setId(3L);
        updated.setTitle("New");

        when(bookService.updateBook(eq(3L), any(Book.class))).thenReturn(updated);

        mvc.perform(put("/api/books/3")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("{\"title\":\"New\",\"author\":\"B\",\"isbn\":\"1\"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("New"));
    }

    /* DELETE /api/books/{id} */
    @Test
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(4L);

        mvc.perform(delete("/api/books/4"))
           .andExpect(status().isNoContent());
    }
}