package com.example.demo.repository;

import com.example.demo.common.BookStatus;
import com.example.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository {
    public Optional<Book> findByIdAndStatus(Long id, BookStatus status);
    public boolean existsByIsbn(String isbn);
}
