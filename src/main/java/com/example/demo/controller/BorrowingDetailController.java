package com.example.demo.controller;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.dto.BorrowingDetailDto;
import com.example.demo.dto.CheckoutDetailDto;
import com.example.demo.dto.ReaderActionDetailDto;
import com.example.demo.service.BorrowingDetailService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BorrowingDetailController {
    @Autowired
    private BorrowingDetailService borrowingDetailService;
    private UserService userService;
    private final long fiveDay = 5*24*60*60*1000;

    @Operation(summary = "show user action")
    @RequestMapping(value = "/borrow-detail/reader-action-details", method = RequestMethod.GET)
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> readerActionDetails() {
        List<ReaderActionDetailDto> borrowingDetailDtos = borrowingDetailService.readerActionDetails();
        return ResponseEntity.ok(borrowingDetailDtos);
    }

    @Operation(summary = "specific reader borrow specific book")
    @RequestMapping(value = "/borrow-detail/borrow", method = RequestMethod.PUT)
    @Authorities(Authority.READER)
    public ResponseEntity<?> borrowBook(
            @Parameter(description = "id of current user want to borrow book") @RequestParam("userid") Long userid,
            @Parameter(description = "id of book want to borrow") @RequestParam("bookid") Long bookid) {
        BorrowingDetailDto borrowingDetailDto =
                userService.borrowBook(userid, bookid, System.currentTimeMillis() + fiveDay);
        return ResponseEntity.ok(borrowingDetailDto);
    }

    @Operation(summary = "specific reader return specific borrowed book")
    @RequestMapping(value = "/borrow-detail/return", method = RequestMethod.PUT)
    @Authorities(Authority.READER)
    public ResponseEntity<?> returnBook(
            @Parameter(description = "id of current user want to return book") @RequestParam("userid") Long userid,
            @Parameter(description = "id of book want to return") @RequestParam("bookid") Long bookid) {
        CheckoutDetailDto checkoutDetailDto = userService.returnBook(userid, bookid);
        return ResponseEntity.ok(checkoutDetailDto);
    }
}
