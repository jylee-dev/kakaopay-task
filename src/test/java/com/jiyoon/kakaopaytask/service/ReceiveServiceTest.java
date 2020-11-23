package com.jiyoon.kakaopaytask.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.jiyoon.kakaopaytask.model.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ReceiveServiceTest {
	@Autowired
	private ReceiveService receiveService;
	
	@Transactional
	@Test	
	public void receive() throws Exception {
		SuccessResponse result = receiveService.receive("Aa0", 2011002);		
//		log.info("result : {}", result);
	}

	@Test	
	public void isSameRoomUser() {
		boolean result = receiveService.isSameRoomUser("R0111", 2011001);
//		log.info("result : {}", result);
	}
}