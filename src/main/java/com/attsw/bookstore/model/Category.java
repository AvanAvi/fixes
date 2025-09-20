package com.attsw.bookstore.model;

import java.util.ArrayList;
import java.util.List;


public class Category {

    private String name;
    private final java.util.List<Book> books = new java.util.ArrayList<>();
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return books;
    }
}