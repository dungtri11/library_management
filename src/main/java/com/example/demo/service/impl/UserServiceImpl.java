package com.example.demo.service.impl;

import com.example.demo.common.Authority;
import com.example.demo.common.BookStatus;
import com.example.demo.dto.BorrowingDetailDto;
import com.example.demo.dto.CheckoutDetailDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.BorrowingDetail;
import com.example.demo.entity.User;
import com.example.demo.exception.MyCustomException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowingDetailService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BorrowingDetailService borrowingDetailService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserRepository userRepository;
    @Override
    public BorrowingDetailDto borrowBook(Long userid, Long bookid, long expectedReturn) {
        User user = userRepository.findById(userid).orElseThrow(() -> new MyCustomException("Unavailable user"));
        Book book = bookService.findById(bookid);
        BorrowingDetail borrowingDetail = borrowingDetailService.createNewBorrowDetail(user, book, expectedReturn);
        if (book.getStatus() == BookStatus.AVAILABLE) {
            book.setStatus(BookStatus.NOT_AVAILABLE);
            bookService.save(book);
        }
        return new BorrowingDetailDto(borrowingDetail);
    }

    @Override
    public CheckoutDetailDto returnBook(Long userid, Long bookid) {
        BorrowingDetail borrowingDetail = borrowingDetailService.checkOutBorrowDetail(userid, bookid);
        return new CheckoutDetailDto(borrowingDetail, System.currentTimeMillis());
    }

    @Override
    public void switchUserStatus(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new MyCustomException("User is not exist");
        }
        if (user.getAuthority() == Authority.NON_USER) {
            user.setAuthority(Authority.READER);
        } else {
            user.setAuthority(Authority.NON_USER);
        }
        userRepository.save(user);
    }

    @Override
    public User userRegister(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new MyCustomException("User already existed");
        }
        user.setAuthority(Authority.READER);
        return userRepository.save(user);
    }

    @Override
    public User userLogin(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new MyCustomException("User not found"));
        if (!user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }
}
