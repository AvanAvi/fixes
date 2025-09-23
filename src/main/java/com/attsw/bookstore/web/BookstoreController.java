package com.attsw.bookstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookstoreController {

    @GetMapping("/")
    public String home() {
        return "index";   
    }
}