package com.example.demo.controller;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import com.example.demo.dto.ReaderActionDetailDto;
import com.example.demo.service.BorrowingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BorrowingController {
    @Autowired
    private BorrowingDetailService borrowingDetailService;

    @RequestMapping(value = "/librarian/readerActionDetails")
    @Authorities(Authority.LIBRARIAN)
    public ResponseEntity<?> readerActionDetails() {
        List<ReaderActionDetailDto> borrowingDetailDtos = borrowingDetailService.readerActionDetails();
        return ResponseEntity.ok(borrowingDetailDtos);
    }
}
