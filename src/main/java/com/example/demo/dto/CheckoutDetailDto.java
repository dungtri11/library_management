package com.example.demo.dto;

import com.example.demo.entity.BorrowingDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDetailDto {
    private String book;
    private String user;
    private String name;
    private Date borrowTime;
    private Date expectedReturnTime;
    private Date returnTime;
    private long penaltyDuration;
    private int penalty;

    public CheckoutDetailDto(BorrowingDetail borrowingDetail, long returnTime) {
        this.book = borrowingDetail.getBook().getTitle();
        this.user = borrowingDetail.getUser().getUsername();
        this.name = borrowingDetail.getUser().getFirstName() + " " + borrowingDetail.getUser().getLastName();
        this.borrowTime = new Date(borrowingDetail.getBorrowTime());
        this.expectedReturnTime = new Date(borrowingDetail.getExpectedReturnTime());
        this.returnTime = new Date(returnTime);
        this.penaltyDuration = returnTime - expectedReturnTime.getTime();
        this.penalty = (int)(borrowingDetail.getPenalty() > 0 ? borrowingDetail.getPenalty() : 0)/(24*60*60*1000) * 1000;
    }
}
