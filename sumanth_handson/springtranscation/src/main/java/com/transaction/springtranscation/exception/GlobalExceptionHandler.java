package com.transaction.springtranscation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handle(Exception exception)
    {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("exception", exception.getClass().getSimpleName());

        body.put("message", exception.getMessage());

        body.put("note", "Some endpoints fail intentionally " + "so you can observe transaction behavior.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
