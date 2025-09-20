package com.attsw.bookstore.model;

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
    	if (book == null) {
            throw new IllegalArgumentException("Book must not be null");
    	}
    	if (books.contains(book)) {
            throw new IllegalArgumentException("Book already present in category");
    	}
        books.add(book);
        book.setCategory(this); 
    }

    public List<Book> getBooks() {
        return books;
    }
    
    public void removeBook(Book book) {
    	if (book == null) {
            throw new IllegalArgumentException("Book must not be null");
        }
        books.remove(book);
        book.setCategory(null);
    }
}