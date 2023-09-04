package com.example.demo.service;

import com.example.demo.common.BookStatus;
import com.example.demo.model.BookCriteria;
import com.example.demo.entity.Book;

import java.util.List;

public interface BookService {
    public List<Book> findBookByCriteria(BookCriteria bookCriteria);
    public Book save(Book book);
    public Book findByIdAndStatus(Long id, BookStatus status);
    public void deleteBook(Book book);
    public Book addNewBook(Book book);
    public Book editBook(Book book);

    public Book findById(Long id);

}
