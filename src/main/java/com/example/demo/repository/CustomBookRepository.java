package com.example.demo.repository;

import com.example.demo.model.BookCriteria;
import com.example.demo.entity.Book;

import java.util.List;

public interface CustomBookRepository {

    public List<Book> findByCriteria(BookCriteria bookCriteria);
}
