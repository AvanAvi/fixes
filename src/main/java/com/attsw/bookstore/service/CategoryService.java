package com.attsw.bookstore.service;

import java.util.List;
import com.attsw.bookstore.model.Category;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category saveCategory(Category category);
    void deleteCategory(Long id);
    boolean hasBooks(Long id);
}