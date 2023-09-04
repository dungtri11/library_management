package com.example.demo.service;

import com.example.demo.dto.ReaderActionDetailDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.BorrowingDetail;
import com.example.demo.entity.User;

import java.util.List;

public interface BorrowingDetailService {
    public BorrowingDetail createNewBorrowDetail(User user, Book book, long expectedReturn);
    public BorrowingDetail checkOutBorrowDetail(Long userid, Long bookid);
    public BorrowingDetail findBorrowingByUserIdAndBookId(Long userid, Long bookid);
    public BorrowingDetail findFirstInQueue(Long bookId);
    public BorrowingDetail save(BorrowingDetail borrowingDetail);
    public List<ReaderActionDetailDto> readerActionDetails();
}
