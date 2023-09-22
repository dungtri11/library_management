package com.example.demo.service;

import com.example.demo.dto.AuthenDto;
import com.example.demo.dto.BorrowingDetailDto;
import com.example.demo.dto.CheckoutDetailDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.BorrowingDetail;
import com.example.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    public BorrowingDetailDto borrowBook(Long userid, Long bookid, long expectedReturn);
    public CheckoutDetailDto returnBook(Long userid, Long bookid);
    public void switchUserStatus(Long id);
    public UserInfoDto userRegister(User user);
    public String userLogin(AuthenDto authentication);
    public List<User> findAllUser();
}
