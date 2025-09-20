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
}