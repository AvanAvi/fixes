package com.attsw.bookstore.web;
import org.springframework.web.bind.annotation.PathVariable;

import com.attsw.bookstore.model.Book;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.attsw.bookstore.repository.BookRepository;

@Controller
public class BookstoreWebController {

    @Autowired
    private BookRepository repo;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", repo.findAll());
        return "books/list";
    }
    
    @GetMapping("/books/new")
    public String newBook(Model model) {
    	model.addAttribute("book", Book.withTitle(""));
        return "books/new";
    }
    
    @PostMapping("/books")
    public String saveBook(Book book) {
        repo.save(book);
        return "redirect:/books";
    }
    
    @GetMapping("/books/{id}/edit")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = repo.findById(id).orElseThrow();
        model.addAttribute("book", book);
        return "books/edit";
    }
    
    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id, Book book) {
        Book existing = repo.findById(id).orElseThrow();
        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());
        repo.save(existing);
        return "redirect:/books";
    }
    
}