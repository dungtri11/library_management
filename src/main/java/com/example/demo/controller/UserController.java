package com.example.demo.controller;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.dto.AuthenDto;
import com.example.demo.dto.BorrowingDetailDto;
import com.example.demo.dto.CheckoutDetailDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.User;
import com.example.demo.jwt.Header;
import com.example.demo.jwt.Jwt;
import com.example.demo.jwt.Payload;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "librarian switch user status")
    @RequestMapping(value = "/user/switch-authority", method = RequestMethod.PUT)
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> switchUserStatus(
            @Parameter(description = "id of updating user") @RequestParam Long userid) {
        userService.switchUserStatus(userid);
        return ResponseEntity.ok("Successfully Switched Status");
    }

}
