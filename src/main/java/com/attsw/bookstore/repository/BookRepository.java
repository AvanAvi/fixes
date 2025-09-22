package com.attsw.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.attsw.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}