package com.attsw.bookstore.model;

import java.time.LocalDate;

public class Book {

    private String title;
    private String author;
    private String isbn;
    private LocalDate publishedDate;
    private boolean available;
    private Category category;

    /* ---- setters ---- */
    public void setTitle(String title)       { this.title = title; }
    public void setAuthor(String author)     { this.author = author; }
    public void setIsbn(String isbn)         { this.isbn = isbn; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setCategory(Category category) { this.category = category; }

    /* ---- getters ---- */
    public String getTitle()       { return title; }
    public String getAuthor()      { return author; }
    public String getIsbn()        { return isbn; }
    public LocalDate getPublishedDate() { return publishedDate; }
    public boolean isAvailable()   { return available; }
    public Category getCategory()  { return category; }
}