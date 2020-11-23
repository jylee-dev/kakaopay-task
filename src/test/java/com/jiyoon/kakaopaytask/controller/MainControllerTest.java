package com.jiyoon.kakaopaytask.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@SpringBootTest
public class MainControllerTest {

	@Test
	@DisplayName("seed API")
	public void seed() throws Exception {
//		OkHttpClient client = new OkHttpClient().newBuilder().build();
//		MediaType mediaType = MediaType.parse("application/json");
//		RequestBody body = RequestBody.create(mediaType, "");
//		Request request = new Request.Builder()
//				.url("http://localhost:8080/seed/10000/3")
//				.method("POST", body)
//				.addHeader("Content-Type", "application/json")
//				.addHeader("X-USER-ID", "2011001")
//				.addHeader("X-ROOM-ID", "R0001")
//				.build();
//		Response response = client.newCall(request).execute();
//		log.info("response : {}", response);
//		log.info("body : {}", response.body().string());
	}
	
	@Transactional
	@Test
	@DisplayName("receive API")
	public void receive() throws Exception {
//		OkHttpClient client = new OkHttpClient().newBuilder().build();
//		MediaType mediaType = MediaType.parse("application/json");
//		RequestBody body = RequestBody.create(mediaType, "");
//		Request request = new Request.Builder()
//				.url("http://localhost:8080/receive/Aa0")
//				.method("PUT", body)
//				.addHeader("Content-Type", "application/json")
//				.addHeader("X-USER-ID", "2011002")
//				.build();
//		Response response = client.newCall(request).execute();
//		log.info("response : {}", response);
//		log.info("body : {}", response.body().string());
	}
	
	@Transactional
	@Test
	@DisplayName("search API")
	public void search() throws Exception {
//		OkHttpClient client = new OkHttpClient().newBuilder().build();
//		Request request = new Request.Builder()
//				.url("http://localhost:8080/list/Aa0")
//				.method("GET", null)
//				.addHeader("Content-Type", "application/json")
//				.addHeader("X-USER-ID", "2011001")
//				.build();
//		Response response = client.newCall(request).execute();
//		log.info("response : {}", response);		
//		log.info("body : {}", response.body().string());
	}
}
