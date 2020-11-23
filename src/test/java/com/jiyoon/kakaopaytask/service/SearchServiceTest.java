package com.jiyoon.kakaopaytask.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jiyoon.kakaopaytask.model.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class SearchServiceTest {
	@Autowired
	private SearchService searchService;
	
	@Test
	public void search() throws Exception {
		SuccessResponse result = searchService.search("Aa0", 2011001);
//		log.info("result : {}", result);
	}	
}