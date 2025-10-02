package com.attsw.bookstore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookServiceImpl service;

    @Test
    void shouldGetAllBooks() {
        Book book1 = Book.withTitle("Clean Code");
        Book book2 = Book.withTitle("Refactoring");
        when(repository.findAll()).thenReturn(Arrays.asList(book1, book2));

        assertEquals(2, service.getAllBooks().size());
        verify(repository).findAll();
    }

    @Test
    void shouldGetBookById() {
        Book book = Book.withTitle("Clean Code");
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        Book found = service.getBookById(1L);

        assertNotNull(found);
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnNullWhenBookNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertNull(service.getBookById(1L));
    }

    @Test
    void shouldSaveBook() {
        Book book = Book.withTitle("Clean Code");
        Book saved = Book.withTitle("Clean Code");
        saved.setId(1L);
        
        when(repository.save(book)).thenReturn(saved);

        Book result = service.saveBook(book);

        assertNotNull(result.getId());
        verify(repository).save(book);
    }

    @Test
    void shouldUpdateBook() {
        Book existing = Book.withTitle("Old Title");
        existing.setId(1L);
        
        Book updates = Book.withTitle("New Title");
        
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Book result = service.updateBook(1L, updates);

        assertEquals("New Title", result.getTitle());
        verify(repository).findById(1L);
        verify(repository).save(existing);
    }

    @Test
    void shouldDeleteBook() {
        service.deleteBook(1L);
        verify(repository).deleteById(1L);
    }
}