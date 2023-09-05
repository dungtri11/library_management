package com.example.demo.controller;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.entity.Book;
import com.example.demo.model.BookCriteria;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    @Authorities({Authority.PUBLIC, Authority.LIBRARIAN, Authority.READER})
    public ResponseEntity<?> findBookByCriteria_Short(BookCriteria bookCriteria) {
        List<Book> books = bookService.findBookByCriteria(bookCriteria);
        return ResponseEntity.ok(books);
    }

    @RequestMapping(value = {"/books"}, method = RequestMethod.GET)
    @Authorities({Authority.PUBLIC, Authority.LIBRARIAN, Authority.READER})
    public ResponseEntity<?> findBookByCriteria(BookCriteria bookCriteria) {
        List<Book> books = bookService.findBookByCriteria(bookCriteria);
        return ResponseEntity.ok(books);
    }

    @RequestMapping(value = "librarian/book/add", method = RequestMethod.POST)
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> addNewBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addNewBook(book));
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    @Authorities({Authority.PUBLIC, Authority.LIBRARIAN, Authority.READER})
    public ResponseEntity<?> bookDetails(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @RequestMapping(value = "librarian/book/edit")
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> editBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.editBook(book));
    }

    @RequestMapping(value = "librarian/book/delete")
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> deleteBook(@RequestBody Book book) {
        bookService.deleteBook(book);
        return ResponseEntity.ok("Successfully Deleted");
    }
}
