package com.example.demo.exception;

public class UnauthorizedResponseException extends RuntimeException{
    public UnauthorizedResponseException(String message) {
        super(message);
    }
}
