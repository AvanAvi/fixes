package com.attsw.bookstore.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.service.BookService;

@WebMvcTest(BookstoreWebController.class)
class BookstoreWebControllerTest {

    @Autowired private MockMvc mvc;
    @MockitoBean   private BookService bookService;

    /* ---------- home ---------- */
    @Test
    void shouldReturnHomePage() throws Exception {
        mvc.perform(get("/"))
           .andExpect(status().isOk())
           .andExpect(view().name("index"));
    }

    /* ---------- list ---------- */
    @Test
    void shouldShowBookList() throws Exception {
        Book b = new Book();
        b.setId(1L);
        b.setTitle("Clean Code");
        when(bookService.getAllBooks()).thenReturn(List.of(b));

        mvc.perform(get("/books"))
           .andExpect(status().isOk())
           .andExpect(view().name("books/list"))
           .andExpect(model().attribute("books", List.of(b)));
    }

    /* ---------- new form ---------- */
    @Test
    void shouldShowNewBookForm() throws Exception {
        mvc.perform(get("/books/new"))
           .andExpect(status().isOk())
           .andExpect(view().name("books/new"))
           .andExpect(model().attributeExists("book"));
    }

    /* ---------- save ---------- */
    @Test
    void shouldSaveBookAndRedirect() throws Exception {
        mvc.perform(post("/books")
                   .param("title", "Refactoring")
                   .param("author", "Martin")
                   .param("isbn", "123"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/books"));

        verify(bookService).saveBook(any(Book.class));
    }

    /* ---------- edit form ---------- */
    @Test
    void shouldShowEditForm() throws Exception {
        Book b = new Book();
        b.setId(5L);
        b.setTitle("Effective Java");
        when(bookService.getBookById(5L)).thenReturn(b);

        mvc.perform(get("/books/5/edit"))
           .andExpect(status().isOk())
           .andExpect(view().name("books/edit"))
           .andExpect(model().attribute("book", b));
    }

    /* ---------- update ---------- */
    @Test
    void shouldUpdateBookAndRedirect() throws Exception {
        mvc.perform(post("/books/7")
                   .param("title", "New Title")
                   .param("author", "A")
                   .param("isbn", "999"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/books"));

        verify(bookService).updateBook(eq(7L), any(Book.class));
    }

    /* ---------- delete ---------- */
    @Test
    void shouldDeleteBookAndRedirect() throws Exception {
        mvc.perform(post("/books/9/delete"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/books"));

        verify(bookService).deleteBook(9L);
    }
}