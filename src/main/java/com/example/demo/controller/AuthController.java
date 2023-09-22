package com.example.demo.controller;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.dto.AuthenDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @Operation(summary = "register as new reader")
    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    @Authorities(Authority.PUBLIC)
    public ResponseEntity<?> doRegister(@RequestBody User user) {
        UserInfoDto newUser = userService.userRegister(user);
        return ResponseEntity.ok(newUser);
    }

    @Operation(summary = "authenticate user")
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    @Authorities(Authority.PUBLIC)
    public ResponseEntity<?> doLogin(@RequestBody AuthenDto authentication) {
        return ResponseEntity.ok(userService.userLogin(authentication));
    }
}
