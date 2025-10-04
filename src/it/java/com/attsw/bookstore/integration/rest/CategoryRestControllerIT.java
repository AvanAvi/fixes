package com.attsw.bookstore.integration.rest;

import com.attsw.bookstore.web.CategoryRestController;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.attsw.bookstore.model.Category;
import com.attsw.bookstore.service.CategoryService;

@WebMvcTest(CategoryRestController.class)
class CategoryRestControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void shouldReturnJsonListOfCategories() throws Exception {
        Category c = new Category();
        c.setName("Fiction");
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(c));

        mvc.perform(get("/api/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Fiction"));
    }

    @Test
    void shouldCreateCategoryViaPost() throws Exception {
        Category saved = new Category();
        saved.setId(1L);
        saved.setName("Science");

        when(categoryService.saveCategory(org.mockito.ArgumentMatchers.any(Category.class))).thenReturn(saved);

        mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Science\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Science"));
    }

    @Test
    void shouldReturnSingleCategoryById() throws Exception {
        Category saved = new Category();
        saved.setId(1L);
        saved.setName("History");

        when(categoryService.getCategoryById(1L)).thenReturn(saved);

        mvc.perform(get("/api/categories/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("History"));
    }

    @Test
    void shouldUpdateExistingCategoryViaPut() throws Exception {
        Category updated = new Category();
        updated.setId(1L);
        updated.setName("Updated Name");

        when(categoryService.saveCategory(org.mockito.ArgumentMatchers.any(Category.class))).thenReturn(updated);

        mvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Name\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void shouldDeleteCategoryViaDelete() throws Exception {
        mvc.perform(delete("/api/categories/1"))
            .andExpect(status().isNoContent());
    }
}