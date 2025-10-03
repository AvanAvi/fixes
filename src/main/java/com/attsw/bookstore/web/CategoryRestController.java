package com.attsw.bookstore.web;

import org.springframework.web.bind.annotation.*;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

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
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }
    
    @GetMapping("/{id}")
    public Category one(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return categoryService.saveCategory(category);
    }
}
