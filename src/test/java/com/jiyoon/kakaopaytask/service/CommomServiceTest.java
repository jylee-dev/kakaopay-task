package com.jiyoon.kakaopaytask.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class CommomServiceTest {
	@Autowired
	private ComService comService;	
			
	@Test
	public void getPeriod() throws Exception {
		Long result = comService.getPeriod("2020-11-22 17:20:00", "d");
//		log.info("result : {}", result);
	}
}