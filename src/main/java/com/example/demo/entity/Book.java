package com.example.demo.entity;

import com.example.demo.common.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", length = 128, nullable = false)
    private String title;

    @Column(name = "author", length = 64, nullable = false)
    private String author;

    @Column(name = "isbn", length = 32, nullable = false)
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16, nullable = false)
    private BookStatus status;
}
