package com.attsw.bookstore.service;

import java.util.List;
import com.attsw.bookstore.model.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<Category> getAllCategories() {
        throw new UnsupportedOperationException("TODO – RED phase");
    }

    @Override
    public Category getCategoryById(Long id) {
        throw new UnsupportedOperationException("TODO – RED phase");
    }

    @Override
    public Category saveCategory(Category category) {
        throw new UnsupportedOperationException("TODO – RED phase");
    }

    @Override
    public void deleteCategory(Long id) {
        throw new UnsupportedOperationException("TODO – RED phase");
    }
}