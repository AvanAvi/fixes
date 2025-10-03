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
import org.springframework.http.MediaType;

@WebMvcTest(CategoryRestController.class)
class CategoryRestControllerTest {

    @Autowired private MockMvc mvc;
    @MockitoBean private CategoryService categoryService;

    @Test
    void shouldReturnAllCategories() throws Exception {
        Category c = new Category();
        c.setId(1L);
        c.setName("Fiction");
        when(categoryService.getAllCategories()).thenReturn(List.of(c));

        mvc.perform(get("/api/categories"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].name").value("Fiction"));
    }
    @Test
    void shouldCreateCategory() throws Exception {
        Category saved = new Category();
        saved.setId(1L);
        saved.setName("Science");

        when(categoryService.saveCategory(any(Category.class))).thenReturn(saved);

        mvc.perform(post("/api/categories")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("{\"name\":\"Science\"}"))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id").value(1L));
    }
    
    @Test
    void shouldReturnSingleCategory() throws Exception {
        Category c = new Category();
        c.setId(2L);
        c.setName("History");
        when(categoryService.getCategoryById(2L)).thenReturn(c);

        mvc.perform(get("/api/categories/2"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("History"));
    }
}