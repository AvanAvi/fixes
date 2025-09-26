package com.attsw.bookstore.web;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.repository.BookRepository;

@WebMvcTest(BookstoreWebController.class)
class BookstoreWebControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository repo;

    @Test
    void shouldShowBookListPage() throws Exception {
        Book b = Book.withTitle("Clean Code");
        when(repo.findAll()).thenReturn(Arrays.asList(b));

        mvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(view().name("books/list"))
            .andExpect(model().attributeExists("books"));
    }
    
    @Test
    void shouldShowAddBookForm() throws Exception {
        mvc.perform(get("/books/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("books/new"))
            .andExpect(model().attributeExists("book"));
    }
    
    @Test
    void shouldSaveBookAndRedirectToList() throws Exception {
        Book saved = Book.withTitle("TDD");
        saved.setId(1L);

        when(repo.save(org.mockito.ArgumentMatchers.any(Book.class))).thenReturn(saved);

        mvc.perform(post("/books")
                .param("title", "TDD")
                .param("author", "Kent")
                .param("isbn", "123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/books"));
    }
    
    @Test
    void shouldShowEditBookForm() throws Exception {
        Book existing = Book.withTitle("Clean Code");
        existing.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        mvc.perform(get("/books/1/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("books/edit"))
            .andExpect(model().attributeExists("book"));
    }
}