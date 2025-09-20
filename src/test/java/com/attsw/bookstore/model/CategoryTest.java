package com.attsw.bookstore.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void categoryShouldHaveName() {
        Category category = new Category();
        category.setName("Software Engineering");

        assertEquals("Software Engineering", category.getName());
    }
    
    @Test
    void categoryShouldContainMultipleBooks() {
        Category category = new Category();
        category.setName("Software Engineering");

        Book cleanCode = new Book();
        cleanCode.setTitle("Clean Code");

        Book effectiveJava = new Book();
        effectiveJava.setTitle("Effective Java");

        category.addBook(cleanCode);
        category.addBook(effectiveJava);

        assertEquals(2, category.getBooks().size());
        assertTrue(category.getBooks().contains(cleanCode));
        assertTrue(category.getBooks().contains(effectiveJava));
    }
    
    @Test
    void whenRemovedFromCategory_bookLosesCategoryReference() {
        Category category = new Category();
        category.setName("Software Engineering");

        Book book = new Book();
        book.setTitle("Refactoring");

        category.addBook(book);
        assertEquals(category, book.getCategory());   // precondition

        category.removeBook(book);                    // new behaviour
        assertNull(book.getCategory());               // expectation
    }
    
    @Test
    void addBookShouldRejectNull() {
        Category category = new Category();
        category.setName("Software Engineering");

        assertThrows(IllegalArgumentException.class,
                     () -> category.addBook(null));
    }
    
    @Test
    void removeBookShouldRejectNull() {
        Category category = new Category();
        category.setName("Software Engineering");

        assertThrows(IllegalArgumentException.class,
                     () -> category.removeBook(null));
    }
}