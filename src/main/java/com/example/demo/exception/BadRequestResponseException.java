package com.example.demo.exception;

public class BadRequestResponseException extends RuntimeException{
    public BadRequestResponseException(String code) {
        super(code);
    }
}
