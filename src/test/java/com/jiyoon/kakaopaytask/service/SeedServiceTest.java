package com.jiyoon.kakaopaytask.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.jiyoon.kakaopaytask.model.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class SeedServiceTest {
	@Autowired
	private SeedService seedService;
	
	@Transactional
	@Test
	public void seed() throws Exception {
		SuccessResponse result = seedService.Seed(10000, 3, 202011001, "R0001");
//		log.info("result : {}", result);
	}	
		
	@Test	
	public void getToken() {
		String result = seedService.getToken();
//		log.info("result : {}", result);
	}
	
	@Test
	public void distribution() {
		int[] result = seedService.distribution(10000, 3);
//		log.info("result : {}", result);
	}
}