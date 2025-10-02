package com.attsw.bookstore.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.attsw.bookstore.model.Category;
import com.attsw.bookstore.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl service;

    @Test
    void shouldGetAllCategories() {
        Category c = new Category();
        c.setName("Sci-Fi");
        when(repository.findAll()).thenReturn(Arrays.asList(c));

        assertThat(service.getAllCategories()).hasSize(1)
                                              .extracting(Category::getName)
                                              .containsExactly("Sci-Fi");
        verify(repository).findAll();
    }

    @Test
    void shouldGetCategoryById() {
        Category c = new Category();
        c.setName("Sci-Fi");
        when(repository.findById(1L)).thenReturn(Optional.of(c));

        assertThat(service.getCategoryById(1L)).isNotNull()
                                               .extracting(Category::getName)
                                               .isEqualTo("Sci-Fi");
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnNullWhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThat(service.getCategoryById(1L)).isNull();
        verify(repository).findById(1L);
    }

    @Test
    void shouldSaveCategory() {
        Category c = new Category();
        c.setName("Fantasy");

        Category saved = new Category();
        saved.setId(1L);
        saved.setName("Fantasy");
        when(repository.save(c)).thenReturn(saved);

        Category result = service.saveCategory(c);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Fantasy");
        verify(repository).save(c);
    }

    @Test
    void shouldDeleteCategory() {
        service.deleteCategory(1L);
        verify(repository).deleteById(1L);
    }
}