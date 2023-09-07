package com.example.demo.entity;

import com.example.demo.common.BookStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(max = 128, min = 1, message = "Book title should less than 128 characters")
    @NotBlank
    private String title;

    @Size(max = 64, min = 1, message = "Book author should less than 128 characters")
    @NotBlank
    @Column(name = "author", length = 64, nullable = false)
    private String author;

    @Pattern(regexp = "[0-9]{32}", message = "Book ISBN should contains 32 numeric characters")
    @NotBlank
    @Column(name = "isbn", length = 32, nullable = false)
    private String isbn;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16, nullable = false)
    private BookStatus status;
}
