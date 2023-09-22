package com.example.demo.controller;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.entity.Book;
import com.example.demo.model.BookCriteria;
import com.example.demo.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @Operation(summary = "get books and filter by criteria")
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    @Authorities({Authority.PUBLIC, Authority.LIBRARIAN, Authority.READER})
    public ResponseEntity<?> findBookByCriteria_Short(
            @Parameter(description = "criteria for filtering") BookCriteria bookCriteria) {
        List<Book> books = bookService.findBookByCriteria(bookCriteria);
        System.out.println(books.size());
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "get books and filter by criteria")
    @RequestMapping(value = {"/books"}, method = RequestMethod.GET)
    @Authorities({Authority.PUBLIC, Authority.LIBRARIAN, Authority.READER})
    public ResponseEntity<?> findBookByCriteria(
            @Parameter(description = "criteria for filtering") BookCriteria bookCriteria) {
        List<Book> books = bookService.findBookByCriteria(bookCriteria);
        System.out.println(books.size());
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "add new book")
    @RequestMapping(value = "/books/add", method = RequestMethod.POST)
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> addNewBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addNewBook(book));
    }

    @Operation(summary = "get book by id")
    @RequestMapping(value = "/books/detail/{id}", method = RequestMethod.GET)
    @Authorities({Authority.PUBLIC, Authority.LIBRARIAN, Authority.READER})
    public ResponseEntity<?> bookDetails(
            @Parameter(description = "book id for searching") @PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @Operation(summary = "edit book information")
    @RequestMapping(value = "/books/edit", method = RequestMethod.PUT)
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> editBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.editBook(book));
    }

    @Operation(summary = "delete book")
    @RequestMapping(value = "/books/delete/{id}", method = RequestMethod.DELETE)
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> deleteBook(
            @Parameter(description = "id of deleting user") @PathVariable("id") Long bookid) {
        bookService.deleteBook(bookid);
        return ResponseEntity.ok("Successfully Deleted");
    }
}
