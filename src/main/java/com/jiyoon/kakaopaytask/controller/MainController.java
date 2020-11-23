package com.jiyoon.kakaopaytask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jiyoon.kakaopaytask.exception.ApiException;
import com.jiyoon.kakaopaytask.model.SuccessResponse;
import com.jiyoon.kakaopaytask.service.ReceiveService;
import com.jiyoon.kakaopaytask.service.SearchService;
import com.jiyoon.kakaopaytask.service.SeedService;

@RestController
public class MainController {
	private static final HttpHeaders responseHeaders = new HttpHeaders();

	@Autowired
	SeedService seedService;
	@Autowired
	ReceiveService receiveService;
	@Autowired
	SearchService searchService;
	
	@RequestMapping(value = "/seed/{amount}/{cnt}", method = RequestMethod.POST)
	public ResponseEntity<SuccessResponse> seed(
			@RequestHeader(value = "X-USER-ID") int userId, 
			@RequestHeader(value = "X-ROOM-ID") String roomId, 
			@PathVariable int amount,
			@PathVariable int cnt) throws ApiException {
		
		return new ResponseEntity<SuccessResponse>(seedService.Seed(amount, cnt, userId, roomId), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/receive/{token}", method = RequestMethod.PUT)
	public ResponseEntity<SuccessResponse> receive(
			@RequestHeader(value = "X-USER-ID") int userId, 
			@PathVariable String token) throws ApiException {
		
		return new ResponseEntity<SuccessResponse>(receiveService.receive(token, userId), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/list/{token}", method = RequestMethod.GET)
	public ResponseEntity<SuccessResponse> search(
			@RequestHeader(value = "X-USER-ID") int userId, 
			@PathVariable String token) throws ApiException {
		
		return new ResponseEntity<SuccessResponse>(searchService.search(token, userId), responseHeaders, HttpStatus.OK);		
	}
}
