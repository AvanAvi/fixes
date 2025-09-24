package com.attsw.bookstore.web;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.repository.BookRepository;

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
}
