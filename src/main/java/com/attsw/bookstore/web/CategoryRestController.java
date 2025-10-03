package com.attsw.bookstore.web;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.attsw.bookstore.model.Category;
import com.attsw.bookstore.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> all() {
        return categoryService.getAllCategories();
    }
}