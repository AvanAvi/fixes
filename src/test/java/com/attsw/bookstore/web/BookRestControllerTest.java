package com.attsw.bookstore.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookRestControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookRestController controller;

    @Test
    void shouldReturnAllBooks() {
        Book book1 = Book.withTitle("Clean Code");
        Book book2 = Book.withTitle("Refactoring");
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        List<Book> result = controller.all();

        assertEquals(2, result.size());
        verify(bookService).getAllBooks();
    }

    @Test
    void shouldCreateBook() {
        Book input = Book.withTitle("TDD");
        Book saved = Book.withTitle("TDD");
        saved.setId(1L);
        when(bookService.saveBook(input)).thenReturn(saved);

        Book result = controller.create(input);

        assertNotNull(result.getId());
        assertEquals("TDD", result.getTitle());
        verify(bookService).saveBook(input);
    }

    @Test
    void shouldReturnBookById() {
        Book book = Book.withTitle("Clean Code");
        book.setId(1L);
        when(bookService.getBookById(1L)).thenReturn(book);

        Book result = controller.one(1L);

        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());
        verify(bookService).getBookById(1L);
    }

    @Test
    void shouldUpdateBook() {
        Book updated = Book.withTitle("Updated Title");
        updated.setId(1L);
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updated);

        Book result = controller.update(1L, new Book());

        assertEquals("Updated Title", result.getTitle());
        verify(bookService).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void shouldDeleteBook() {
        doNothing().when(bookService).deleteBook(1L);

        controller.delete(1L);

        verify(bookService).deleteBook(1L);
    }
}
