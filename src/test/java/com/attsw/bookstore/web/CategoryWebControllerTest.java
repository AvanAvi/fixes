package com.attsw.bookstore.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.attsw.bookstore.model.Category;
import com.attsw.bookstore.service.CategoryService;

@ExtendWith(MockitoExtension.class)
class CategoryWebControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private Model model;

    @InjectMocks
    private CategoryWebController controller;

    @Test
    void shouldReturnListView() {
        Category category = new Category();
        category.setName("Fiction");
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category));

        String view = controller.list(model);

        assertEquals("categories/list", view);
        verify(model).addAttribute(eq("categories"), anyList());
        verify(categoryService).getAllCategories();
    }

    @Test
    void shouldReturnNewCategoryView() {
        String view = controller.newCategory(model);

        assertEquals("categories/new", view);
        verify(model).addAttribute(eq("category"), any(Category.class));
    }

    @Test
    void shouldSaveCategoryAndRedirect() {
        Category category = new Category();

        String redirect = controller.saveCategory(category);

        assertEquals("redirect:/categories", redirect);
        verify(categoryService).saveCategory(category);
    }

    @Test
    void shouldReturnEditCategoryView() {
        Category category = new Category();
        category.setId(1L);
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        String view = controller.editCategory(1L, model);

        assertEquals("categories/edit", view);
        verify(model).addAttribute("category", category);
        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void shouldUpdateCategoryAndRedirect() {
        Category category = new Category();
        category.setName("Updated Name");

        String redirect = controller.updateCategory(1L, category);

        assertEquals("redirect:/categories", redirect);
        assertEquals(1L, category.getId());
        verify(categoryService).saveCategory(category);
    }

    @Test
    void shouldDeleteCategoryAndRedirect() {
        String redirect = controller.deleteCategory(1L);

        assertEquals("redirect:/categories", redirect);
        verify(categoryService).deleteCategory(1L);
    }
}
