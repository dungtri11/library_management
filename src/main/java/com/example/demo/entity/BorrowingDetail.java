package com.example.demo.entity;

import com.example.demo.common.BorrowingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "borrowing_detail")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "borrow_time", nullable = false)
    private long borrowTime;

    @Column(name = "expected_return_time", nullable = false)
    private long expectedReturnTime;

    @Column(name = "penalty", nullable = false)
    private long penalty;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16, nullable = false)
    private BorrowingStatus status;
}
