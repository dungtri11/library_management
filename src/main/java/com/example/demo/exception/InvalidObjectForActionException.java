package com.example.demo.exception;

public class InvalidObjectForActionException extends RuntimeException {
    public InvalidObjectForActionException(String message) {
        super(message);
    }
}
