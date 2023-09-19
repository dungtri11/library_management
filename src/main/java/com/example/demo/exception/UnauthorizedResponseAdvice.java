package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UnauthorizedResponseAdvice {

    @ResponseBody
    @ExceptionHandler(UnauthorizedResponseException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> exceptionHandle(UnauthorizedResponseException exception) {
        Map<String, String> error = new HashMap<>();
        String[] params = exception.getMessage().split(":");
        String code = params[1].trim();
        String message = params[0].trim();
        error.put("Code", code);
        error.put("Message", message);
        return error;
    }
}
