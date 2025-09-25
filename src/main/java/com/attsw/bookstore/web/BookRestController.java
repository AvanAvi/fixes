package com.attsw.bookstore.web;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PutMapping;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.repository.BookRepository;
import org.springframework.web.bind.annotation.PathVariable;
@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookRepository repo;

    public BookRestController(BookRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Book> all() {
        return repo.findAll();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return repo.save(book);
    }
    
    @GetMapping("/{id}")
    public Book one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
    
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book newBook) {
        Book existing = repo.findById(id).orElseThrow();
        existing.setTitle(newBook.getTitle());
        existing.setAuthor(newBook.getAuthor());
        existing.setIsbn(newBook.getIsbn());
        return repo.save(existing);
    }
    
}
