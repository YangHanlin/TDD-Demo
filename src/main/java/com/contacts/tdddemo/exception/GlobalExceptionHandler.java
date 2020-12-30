package com.contacts.tdddemo.exception;

import com.contacts.tdddemo.vo.ErrorResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResult handleException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ErrorResult(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ErrorResult handleNotFoundException(NotFoundException e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new ErrorResult(e.getMessage());
    }
}
