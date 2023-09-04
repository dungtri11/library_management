package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCriteria {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private String key;
    private String order;
}
