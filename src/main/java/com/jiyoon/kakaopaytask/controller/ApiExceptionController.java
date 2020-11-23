package com.jiyoon.kakaopaytask.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiyoon.kakaopaytask.exception.ApiException;
import com.jiyoon.kakaopaytask.model.ErrorResponse;

@ControllerAdvice
public class ApiExceptionController {
	private static final HttpHeaders responseHeaders = new HttpHeaders();
	
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    protected ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
    	return new ResponseEntity<ErrorResponse>(new ErrorResponse(e.getError()), responseHeaders, HttpStatus.BAD_REQUEST);    	
    }
}