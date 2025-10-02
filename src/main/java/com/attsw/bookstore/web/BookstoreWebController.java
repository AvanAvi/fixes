package com.attsw.bookstore.web;

import org.springframework.web.bind.annotation.PathVariable;
import com.attsw.bookstore.model.Book;
import com.attsw.bookstore.service.BookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookstoreWebController {

    private final BookService bookService;

    public BookstoreWebController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/list";
    }
    
    @GetMapping("/books/new")
    public String newBook(Model model) {
        model.addAttribute("book", Book.withTitle(""));
        return "books/new";
    }
    
    @PostMapping("/books")
    public String saveBook(Book book) {
        bookService.saveBook(book);
        return "redirect:/books";
    }
    
    @GetMapping("/books/{id}/edit")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "books/edit";
    }
    
    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id, Book book) {
        bookService.updateBook(id, book);
        return "redirect:/books";
    }
    
    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}