package com.attsw.bookstore.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookstoreWebControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private Model model;

    @InjectMocks
    private BookstoreWebController controller;

    @Test
    void shouldReturnHomeView() {
        String view = controller.home();
        assertEquals("index", view);
    }

    @Test
    void shouldReturnListBooksView() {
        Book book = Book.withTitle("Clean Code");
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book));

        String view = controller.listBooks(model);

        assertEquals("books/list", view);
        verify(model).addAttribute(eq("books"), anyList());
        verify(bookService).getAllBooks();
    }

    @Test
    void shouldReturnNewBookView() {
        String view = controller.newBook(model);

        assertEquals("books/new", view);
        verify(model).addAttribute(eq("book"), any(Book.class));
    }

    @Test
    void shouldSaveBookAndRedirect() {
        Book book = Book.withTitle("TDD");

        String redirect = controller.saveBook(book);

        assertEquals("redirect:/books", redirect);
        verify(bookService).saveBook(book);
    }

    @Test
    void shouldReturnEditBookView() {
        Book book = Book.withTitle("Clean Code");
        book.setId(1L);
        when(bookService.getBookById(1L)).thenReturn(book);

        String view = controller.editBook(1L, model);

        assertEquals("books/edit", view);
        verify(model).addAttribute("book", book);
        verify(bookService).getBookById(1L);
    }

    @Test
    void shouldUpdateBookAndRedirect() {
        Book book = Book.withTitle("Updated");

        String redirect = controller.updateBook(1L, book);

        assertEquals("redirect:/books", redirect);
        verify(bookService).updateBook(1L, book);
    }

    @Test
    void shouldDeleteBookAndRedirect() {
        String redirect = controller.deleteBook(1L);

        assertEquals("redirect:/books", redirect);
        verify(bookService).deleteBook(1L);
    }
}
