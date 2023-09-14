package com.example.demo.service.impl;

import com.example.demo.common.BookStatus;
import com.example.demo.entity.Book;
import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.exception.InvalidObjectForActionException;
import com.example.demo.model.BookCriteria;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Override
    public List<Book> findBookByCriteria(BookCriteria bookCriteria) {
        return bookRepository.findByCriteria(bookCriteria);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book findByIdAndStatus(Long id, BookStatus status) {
        return bookRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Couldn't find suitable books"));
    }

    @Override
    public void deleteBook(Long bookid) {
        Book book = bookRepository.findById(bookid)
                .orElseThrow(() -> new ObjectNotFoundException("Couldn't find suitable books"));
        if (book.getStatus() == BookStatus.NOT_AVAILABLE) {
            throw new InvalidObjectForActionException("Book need to be available for deletion");
        }
        bookRepository.delete(book);
    }

    @Override
    public Book addNewBook(Book book) {
        return save(book);
    }

    @Override
    public Book editBook(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            throw new ObjectNotFoundException("Couldn't find suitable books");
        }
        return save(book);
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Couldn't find suitable books"));
    }


}
