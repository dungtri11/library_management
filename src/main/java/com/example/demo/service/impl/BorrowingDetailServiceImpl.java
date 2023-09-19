package com.example.demo.service.impl;

import com.example.demo.common.BookStatus;
import com.example.demo.common.BorrowingStatus;
import com.example.demo.common.ReaderAction;
import com.example.demo.dto.ReaderActionDetailDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.BorrowingDetail;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestResponseException;
import com.example.demo.repository.BorrowingDetailRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BorrowingDetailServiceImpl implements BorrowingDetailService {
    @Autowired
    private BorrowingDetailRepository borrowingDetailRepository;

    @Autowired
    private BookService bookService;

    @Override
    public BorrowingDetail createNewBorrowDetail(User user, Book book, long expectedReturn) {
        if (borrowingDetailRepository.existsByUserIdAndBookIdAndStatus(user.getId(), book.getId(), BorrowingStatus.IN_QUEUE) ||
        borrowingDetailRepository.existsByUserIdAndBookIdAndStatus(user.getId(), book.getId(), BorrowingStatus.BORROWING)) {
            throw new BadRequestResponseException("User has already borrowed this book: 201");
        }
        BorrowingStatus status = (book.getStatus() == BookStatus.NOT_AVAILABLE
                ? BorrowingStatus.IN_QUEUE : BorrowingStatus.BORROWING);

        BorrowingDetail borrowingDetail = BorrowingDetail.builder()
                .book(book)
                .user(user)
                .borrowTime(System.currentTimeMillis())
                .expectedReturnTime(expectedReturn)
                .status(status)
                .build();
        return borrowingDetailRepository.save(borrowingDetail);
    }

    @Override
    public BorrowingDetail checkOutBorrowDetail(Long userid, Long bookid) {
        BorrowingDetail borrowingDetail = findBorrowingByUserIdAndBookId(userid, bookid);
        if (borrowingDetail == null) {
            throw new BadRequestResponseException("Book has been already returned: 202");
        }

        long current = System.currentTimeMillis();
        long penaltyDuration = current - borrowingDetail.getExpectedReturnTime();
        borrowingDetail.setPenalty(penaltyDuration);
        borrowingDetail.setStatus(BorrowingStatus.RETURNED);
        BorrowingDetail ret = save(borrowingDetail);

        Book book = bookService.findByIdAndStatus(bookid, BookStatus.NOT_AVAILABLE);
        book.setStatus(BookStatus.AVAILABLE);
        bookService.save(book);

        borrowingDetail = findFirstInQueue(bookid);
        if (borrowingDetail != null) {
            book.setStatus(BookStatus.NOT_AVAILABLE);
            bookService.save(book);
            borrowingDetail.setStatus(BorrowingStatus.BORROWING);
            borrowingDetail.setExpectedReturnTime(current + (5*24*60*60*1000));
            save(borrowingDetail);
        }
        return ret;
    }

    @Override
    public BorrowingDetail findBorrowingByUserIdAndBookId(Long userid, Long bookid) {
        return borrowingDetailRepository.findByUserIdAndBookIdAndStatus(userid, bookid, BorrowingStatus.BORROWING)
                .orElse(null);
    }

    @Override
    public BorrowingDetail findFirstInQueue(Long bookId) {
        return borrowingDetailRepository.findFirstByBookIdAndStatusOrderByBorrowTimeDesc(bookId, BorrowingStatus.IN_QUEUE)
                .orElse(null);
    }

    @Override
    public BorrowingDetail save(BorrowingDetail borrowingDetail) {
        return borrowingDetailRepository.save(borrowingDetail);
    }

    @Override
    public List<ReaderActionDetailDto> readerActionDetails() {
        List<BorrowingDetail> borrowingDetails = borrowingDetailRepository.findAll();
        List<ReaderActionDetailDto> readerActionDetailDtos = new ArrayList<>();
        for (BorrowingDetail borrowingDetail : borrowingDetails) {
            if (borrowingDetail.getStatus() == BorrowingStatus.RETURNED) {
                readerActionDetailDtos.add(new ReaderActionDetailDto(borrowingDetail, ReaderAction.RETURN));
            }

            readerActionDetailDtos.add(new ReaderActionDetailDto(borrowingDetail, ReaderAction.BORROW));
        }
        Collections.sort(readerActionDetailDtos, new Comparator<ReaderActionDetailDto>() {
            @Override
            public int compare(ReaderActionDetailDto o1, ReaderActionDetailDto o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });
        return readerActionDetailDtos;
    }
}
