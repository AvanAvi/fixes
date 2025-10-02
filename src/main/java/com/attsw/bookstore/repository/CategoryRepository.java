package com.attsw.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.attsw.bookstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}