package com.example.demo.service;

import com.example.demo.dto.BorrowingDetailDto;
import com.example.demo.dto.CheckoutDetailDto;
import com.example.demo.entity.BorrowingDetail;
import com.example.demo.entity.User;

public interface UserService {
    public BorrowingDetailDto borrowBook(Long userid, Long bookid, long expectedReturn);
    public CheckoutDetailDto returnBook(Long userid, Long bookid);
    public void switchUserStatus(Long id);
    public User userRegister(User user);
    public User userLogin(String username, String password);

}
