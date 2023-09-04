package com.example.demo.controller;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.dto.BorrowingDetailDto;
import com.example.demo.dto.CheckoutDetailDto;
import com.example.demo.entity.User;
import com.example.demo.jwt.Header;
import com.example.demo.jwt.Jwt;
import com.example.demo.jwt.Payload;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
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

    @RequestMapping(value = "/reader/borrow", method = RequestMethod.POST)
    @Authorities(Authority.READER)
    public ResponseEntity<?> borrowBook(@RequestParam("userid") Long userid,
                                        @RequestParam("bookid") Long bookid) {
        BorrowingDetailDto borrowingDetailDto =
                userService.borrowBook(userid, bookid, System.currentTimeMillis() + (5*24*60*60*1000));
        return ResponseEntity.ok(borrowingDetailDto);
    }

    @RequestMapping(value = "/reader/return", method = RequestMethod.POST)
    @Authorities(Authority.READER)
    public ResponseEntity<?> returnBook(@RequestParam("userid") Long userid,
                                        @RequestParam("bookid") Long bookid) {
        CheckoutDetailDto checkoutDetailDto = userService.returnBook(userid, bookid);
        return ResponseEntity.ok(checkoutDetailDto);
    }

    @RequestMapping(value = "/librarian/switchStatus", method = RequestMethod.POST)
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> switchUserStatus(@RequestBody User user) {
        userService.switchUserStatus(user);
        return ResponseEntity.ok("Successfully Switched Status");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Authorities(Authority.PUBLIC)
    public ResponseEntity<?> userRegister(@RequestBody User user) {
        User newUser = userService.userRegister(user);
        return ResponseEntity.ok(newUser);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Authorities(Authority.PUBLIC)
    public ResponseEntity<?> getLogin() {
        return ResponseEntity.ok("This is login page");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Authorities(Authority.PUBLIC)
    public ResponseEntity<?> doLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        HttpHeaders headers = new HttpHeaders();
        User user = userService.userLogin(username, password);
        if (user != null) {
            String credential = new Jwt(new Header(), new Payload(username, user.getAuthority()), "MySecret").getSpecification();
            headers.add("Set-Cookie", "credential=" + credential + "; Max-Age=604800; Path=/; Secure; HttpOnly; SameSite=strict");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body("Successfully login");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find user");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Authorities({Authority.READER, Authority.LIBRARIAN})
    public ResponseEntity<?> userLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie c = null;
        if (cookies != null) {
            List<Cookie> cookieList = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("credential")).collect(Collectors.toList());
            c = (cookieList.size() > 0 ? cookieList.get(0) : null);
        }
        if (c != null) {
            c.setMaxAge(0);
            response.addCookie(c);
            return ResponseEntity.ok("Successfully logout");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credential not found");
        }
    }
}
