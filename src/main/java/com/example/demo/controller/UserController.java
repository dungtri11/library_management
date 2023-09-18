package com.example.demo.controller;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.dto.AuthenDto;
import com.example.demo.dto.BorrowingDetailDto;
import com.example.demo.dto.CheckoutDetailDto;
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

    @Operation(summary = "register as new reader")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Authorities(Authority.PUBLIC)
    public ResponseEntity<?> doRegister(@RequestBody User user) {
        User newUser = userService.userRegister(user);
        return ResponseEntity.ok(newUser);
    }

    @Operation(summary = "access into login page")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Authorities(Authority.PUBLIC)
    public ResponseEntity<?> getLogin() {
        return ResponseEntity.ok("This is login page");
    }

    @Operation(summary = "authenticate login information")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Authorities(Authority.PUBLIC)
    public ResponseEntity<?> doLogin(@RequestBody AuthenDto auth) {
        HttpHeaders headers = new HttpHeaders();
        String username = auth.getUsername();
        String password = auth.getPassword();
        User user = userService.userLogin(username, password);
        if (user != null) {
            String credential = new Jwt(new Header(), new Payload(username, user.getAuthority()), "MySecret").getSpecification();
            headers.add("Set-Cookie", "credential=" + credential + "; Max-Age=604800; Path=/; Secure; HttpOnly; SameSite=strict");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body("Successfully login");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find user");
    }

    @Operation(summary = "logout from current credential")
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
