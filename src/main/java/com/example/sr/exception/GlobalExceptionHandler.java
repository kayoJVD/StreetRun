package com.example.sr.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> businessRuleExceptionHandler(BusinessRuleException e, HttpServletRequest request){
        StandardError standardError = new StandardError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }

}
