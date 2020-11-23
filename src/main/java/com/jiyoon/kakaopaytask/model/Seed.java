package com.jiyoon.kakaopaytask.model;

import lombok.Data;

@Data
public class Seed {
	final private String token;
	final private int amount;
	final private int cnt;
	final private Integer userId;
	final private String roomId;	
	private String regDate;	
}