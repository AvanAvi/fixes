package com.attsw.bookstore.web;

import static org.mockito.Mockito.when;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;


import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.repository.BookRepository;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository repo;

    @Test
    void shouldReturnJsonListOfBooks() throws Exception {
        Book b = Book.withTitle("Clean Code");
        when(repo.findAll()).thenReturn(Arrays.asList(b));

        mvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }
    
    @Test
    void shouldCreateBookViaPost() throws Exception {
        Book toSave = Book.withTitle("Refactoring");
        toSave.setAuthor("Martin Fowler");
        toSave.setIsbn("0201485672");

        Book saved = Book.withTitle("Refactoring");
        saved.setId(1L);          // simulate DB assign
        saved.setAuthor("Martin Fowler");
        saved.setIsbn("0201485672");

        when(repo.save(org.mockito.ArgumentMatchers.any(Book.class))).thenReturn(saved);

        mvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "Refactoring",
                      "author": "Martin Fowler",
                      "isbn": "0201485672"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Refactoring"));
    }
    
    @Test
    void shouldReturnSingleBookById() throws Exception {
        Book saved = Book.withTitle("Clean Code");
        saved.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(saved));

        mvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Clean Code"));
    }
}