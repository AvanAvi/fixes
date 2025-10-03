package com.attsw.bookstore.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.attsw.bookstore.model.Category;
import com.attsw.bookstore.service.CategoryService;

@WebMvcTest(CategoryWebController.class)
class CategoryWebControllerTest {

    @Autowired private MockMvc mvc;
    @MockitoBean   private CategoryService categoryService;

    @Test
    void shouldShowCategoryList() throws Exception {
        Category c = new Category();
        c.setId(1L);
        c.setName("Software");
        when(categoryService.getAllCategories()).thenReturn(List.of(c));

        mvc.perform(get("/categories"))
           .andExpect(status().isOk())
           .andExpect(view().name("categories/list"))
           .andExpect(model().attribute("categories", List.of(c)));
    }
    @Test
    void shouldShowNewCategoryForm() throws Exception {
        mvc.perform(get("/categories/new"))
           .andExpect(status().isOk())
           .andExpect(view().name("categories/new"))
           .andExpect(model().attributeExists("category"));
    }
    
    @Test
    void shouldSaveCategoryAndRedirect() throws Exception {
        mvc.perform(post("/categories")
                   .param("name", "Fiction"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/categories"));

        verify(categoryService).saveCategory(any(Category.class));
    }
    
    @Test
    void shouldShowEditForm() throws Exception {
        Category c = new Category();
        c.setId(5L);
        c.setName("Science");
        when(categoryService.getCategoryById(5L)).thenReturn(c);

        mvc.perform(get("/categories/5/edit"))
           .andExpect(status().isOk())
           .andExpect(view().name("categories/edit"))
           .andExpect(model().attribute("category", c));
    }
    @Test
    void shouldUpdateCategoryAndRedirect() throws Exception {
        mvc.perform(post("/categories/7")
                   .param("name", "Updated Name"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/categories"));

        verify(categoryService).saveCategory(any(Category.class));
    }
    
    @Test
    void shouldDeleteCategoryAndRedirect() throws Exception {
        mvc.perform(post("/categories/9/delete"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/categories"));

        verify(categoryService).deleteCategory(9L);
    }
}