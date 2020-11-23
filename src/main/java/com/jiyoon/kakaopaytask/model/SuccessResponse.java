package com.jiyoon.kakaopaytask.model;

import lombok.Data;

@Data
public class SuccessResponse {	
	final private String status = "SUCCESS";
	private String message;
	private Object data;
}