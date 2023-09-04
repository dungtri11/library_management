package com.example.demo.jwt;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class Header {
    private String typ;
    private String alg;

    public Header() {
        this.typ = "JWT";
        this.alg = "HS256";
    }
}
