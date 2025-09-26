package com.attsw.bookstore.web;

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
}