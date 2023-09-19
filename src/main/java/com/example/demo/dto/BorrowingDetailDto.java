package com.example.demo.dto;

import com.example.demo.entity.BorrowingDetail;
import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingDetailDto {
    private String book;
    private String user;
    private Date borrowTime;
    private Date expectedReturnTime;
    private long penalty;

    public BorrowingDetailDto(BorrowingDetail borrowingDetail) {
        this.book = borrowingDetail.getBook().getTitle();
        this.user = borrowingDetail.getUser().getFirstName() + " " + borrowingDetail.getUser().getLastName();
        this.borrowTime = new Date(borrowingDetail.getBorrowTime());
        this.expectedReturnTime = new Date(borrowingDetail.getExpectedReturnTime());
        this.penalty = borrowingDetail.getPenalty();
    }
}