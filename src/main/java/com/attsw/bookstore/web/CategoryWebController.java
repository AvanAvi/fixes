package com.attsw.bookstore.web;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.attsw.bookstore.model.Category;
import com.attsw.bookstore.service.CategoryService;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CategoryWebController {

    private final CategoryService categoryService;

    public CategoryWebController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String list(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories/list";
    }
    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        return "categories/new";
    }
    @PostMapping("/categories")
    public String saveCategory(Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }
    @GetMapping("/categories/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "categories/edit";
    }
    @PostMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id, Category category) {
        category.setId(id);
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }
    
    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}