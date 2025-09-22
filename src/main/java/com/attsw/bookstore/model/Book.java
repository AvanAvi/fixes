package com.attsw.bookstore.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private LocalDate publishedDate;
    private boolean available;

    @ManyToOne
    private Category category;

    Book() {}                       // package-private for JPA

    /* ---- factory helper for tests (book §3.5) ---- */
    public static Book withTitle(String title) {
        Book b = new Book();
        b.setTitle(title);
        return b;
    }

    public Long getId() { return id; }

    /* ----------  setters  ---------- */
    public void setTitle(String title)       { this.title = title; }
    public void setAuthor(String author)     { this.author = author; }
    public void setIsbn(String isbn)         { this.isbn = isbn; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setCategory(Category category) { this.category = category; }

    /* ----------  getters  ---------- */
    public String getTitle()       { return title; }
    public String getAuthor()      { return author; }
    public String getIsbn()        { return isbn; }
    public LocalDate getPublishedDate() { return publishedDate; }
    public boolean isAvailable()   { return available; }
    public Category getCategory()  { return category; }
}