package com.jiyoon.kakaopaytask.service;

import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiyoon.kakaopaytask.mapper.SeedMapper;
import com.jiyoon.kakaopaytask.model.SuccessResponse;
import com.jiyoon.kakaopaytask.model.Seed;
import com.jiyoon.kakaopaytask.constant.ApiError;
import com.jiyoon.kakaopaytask.exception.ApiException;
import com.jiyoon.kakaopaytask.mapper.ReceiveMapper;
import com.jiyoon.kakaopaytask.mapper.RoomMapper;
import com.jiyoon.kakaopaytask.model.Receive;

@Service
public class SeedService {
	@Autowired
	SeedMapper seedMapper;
	@Autowired
	ReceiveMapper receiveMapper;
	@Autowired
	RoomMapper roomMappser;
		
	// [카카오페이 뿌리기 기능1] 뿌리기
	@Transactional
	public SuccessResponse Seed(int amount, int cnt, int userId, String roomId) throws ApiException {
		// token 가져오기
		String token = getToken();
		
		// 뿌리기 정보 저장
		seedMapper.insertSeed(new Seed(token, amount, cnt, userId, roomId));
		
		// 인원수로 분배된 금액 가져오기
		int[] amountArry = distribution(amount, cnt);
		
		// 분배된 정보 저장 (분배된 정보 = 받기 위한 정보)
		for (int i = 0; i < cnt; i ++) {
			receiveMapper.insertReceive(new Receive(token, i+1, amountArry[i]));
		}

		try {
			// response data
			HashMap<String, Object> data = new HashMap<>();
			data.put("token", token); // token
		
			// response
			SuccessResponse sResponse = new SuccessResponse();					
			sResponse.setMessage("seed success!");
			sResponse.setData(data);
			return sResponse;
			
		} catch (Exception e) {
			throw new ApiException(ApiError.SEED_ERROR, e.getLocalizedMessage());
		}
	}
	
	// token 생성 (3자리 랜덤 문자열 = (영대문자) + (영소문자) + (숫자))
	public String getToken() {
		Random rand = new Random();
		char[] chrArry = new char[3];
		chrArry[0] = (char)(rand.nextInt(26) + 'A'); // 'A'~'Z' 랜덤 문자
		chrArry[1] = (char)(rand.nextInt(26) + 'a'); // 'a'~'z' 랜덤 문자
		chrArry[2] = (char)(rand.nextInt(10) + '0'); // '0'~'9' 랜덤 문자
		
		return String.valueOf(chrArry);		
	}
	
	// 인원수별로 금액 분배
	public int[] distribution(int amount, int cnt) {
		Random rand = new Random();
		int[] amountArry = new int[cnt]; // 분배된 금액
		int remain = amount; // 잔액
		
		for (int i = 0; i < cnt; i ++) {
			if (i != cnt -1) {
				amountArry[i] = (remain) * (rand.nextInt(9) + 1) / 100 * 10; // 남은 금액에서 10~90% 중 랜덤으로 분배, 1원 단위 금액 안나오도록 처리
				remain -= amountArry[i]; // 잔액 차감
			} else {				
				amountArry[i] = remain; // 잔액이 남지 않도록 마지막 값은 남은 금액으로 저장
			}			
		}
		return amountArry;
	}
}
