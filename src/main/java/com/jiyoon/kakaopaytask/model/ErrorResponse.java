package com.jiyoon.kakaopaytask.model;

import com.jiyoon.kakaopaytask.constant.ApiError;

import lombok.Data;

@Data
public class ErrorResponse {
	final private String status = "ERROR";
	private String error;
	private String message;
	
	public ErrorResponse(ApiError apiError) {
        this.error = apiError.toString();
        this.message = apiError.getDesc();
    }
}
