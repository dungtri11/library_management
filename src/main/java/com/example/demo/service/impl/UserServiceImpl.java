package com.example.demo.service.impl;

import com.example.demo.common.Authority;
import com.example.demo.common.BookStatus;
import com.example.demo.dto.AuthenDto;
import com.example.demo.dto.BorrowingDetailDto;
import com.example.demo.dto.CheckoutDetailDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.BorrowingDetail;
import com.example.demo.entity.User;

import com.example.demo.exception.BadRequestResponseException;
import com.example.demo.exception.UnauthorizedResponseException;
import com.example.demo.jwt.JwtUtils;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowingDetailService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        User user = userRepository.findById(userid).orElseThrow(() ->
                new BadRequestResponseException("Couldn't find suitable user: 301"));
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
    public void switchUserStatus(Long userid) {

        User user = userRepository.findById(userid).
                orElseThrow(() -> new BadRequestResponseException("Couldn't find suitable user: 301"));
        if (user.getAuthority() == Authority.NON_USER) {
            user.setAuthority(Authority.READER);
        } else if (user.getAuthority() == Authority.READER) {
            user.setAuthority(Authority.NON_USER);
        } else {
            throw new BadRequestResponseException("User's authority must be non-user or reader to do this action: 601");
        }
        userRepository.save(user);
    }

    @Override
    public UserInfoDto userRegister(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestResponseException("User already existed: 302");
        }
        if (user.getUsername().length() > 64 || user.getPassword().length() > 64 ||
        user.getFirstName().length() > 32 || user.getLastName().length() > 32) {
            throw new BadRequestResponseException("Invalid user information: 303");
        }
        user.setAuthority(Authority.READER);
        return new UserInfoDto(userRepository.save(user));
    }

    @Override
    public String userLogin(AuthenDto authenDto) {
        User user = (User) loadUserByUsername(authenDto.getUsername());
        if (!user.getPassword().equals(authenDto.getPassword())) {
            throw new UnauthorizedResponseException("Wrong password: 401");
        }
        String token = new JwtUtils().generateAccessToken(user);
        return token;
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new BadRequestResponseException("Couldn't find suitable username: 301"));
    }
}
