package com.contacts.tdddemo.exception;

import com.contacts.tdddemo.vo.ErrorResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResult handleException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Map<String, Object> extrasMap = new HashMap<>(1);
        extrasMap.put("originalException", e.getClass().getName());
        return new ErrorResult(e.getMessage(), extrasMap);
    }

    @ExceptionHandler(NotFoundException.class)
    public ErrorResult handleNotFoundException(NotFoundException e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new ErrorResult(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ErrorResult handleValidationException(BindException e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, Object> extrasMap = new HashMap<>(1);
        extrasMap.put("diagnosisInformation", e.getMessage());
        return new ErrorResult("Argument validation failed", extrasMap);
    }
}
