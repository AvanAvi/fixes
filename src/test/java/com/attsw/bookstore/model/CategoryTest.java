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
}