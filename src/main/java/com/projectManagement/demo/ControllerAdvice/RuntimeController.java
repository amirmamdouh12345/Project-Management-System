package com.projectManagement.demo.ControllerAdvice;

import com.projectManagement.demo.DTOs.Responses.RuntimeExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestControllerAdvice
public class RuntimeController {

    @ExceptionHandler(RuntimeException.class)
    public RuntimeExceptionResponse ggg(RuntimeException runtimeException){
        RuntimeExceptionResponse r = new RuntimeExceptionResponse();
        r.setDescription(runtimeException.getMessage());
        r.setTimeStamp(LocalDateTime.now().toString());
        r.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return r;
    }
}
