package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class InvalidObjectForActionExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidObjectForActionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> exceptionHandle(InvalidObjectForActionException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("Exception Detail:", exception.getMessage());
        return error;
    }
}
