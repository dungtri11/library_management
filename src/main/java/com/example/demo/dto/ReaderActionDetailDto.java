package com.example.demo.dto;

import com.example.demo.common.ReaderAction;
import com.example.demo.entity.BorrowingDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ReaderActionDetailDto {
    private ReaderAction readerAction;
    private Date time;
    private String user;
    private String name;

    public ReaderActionDetailDto(BorrowingDetail detail, ReaderAction readerAction) {
        this.readerAction = readerAction;
        this.time = new Date((readerAction == ReaderAction.BORROW
                ? detail.getBorrowTime() : detail.getExpectedReturnTime() + detail.getPenalty()));
        this.user = detail.getUser().getUsername();
        this.name = detail.getUser().getFirstName() + " " + detail.getUser().getLastName();
    }
}
