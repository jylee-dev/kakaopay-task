package com.jiyoon.kakaopaytask.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiyoon.kakaopaytask.constant.ApiError;
import com.jiyoon.kakaopaytask.mapper.ReceiveMapper;
import com.jiyoon.kakaopaytask.mapper.RoomMapper;
import com.jiyoon.kakaopaytask.mapper.SeedMapper;
import com.jiyoon.kakaopaytask.exception.ApiException;
import com.jiyoon.kakaopaytask.model.Receive;
import com.jiyoon.kakaopaytask.model.SuccessResponse;
import com.jiyoon.kakaopaytask.model.Seed;

@Service
public class SearchService {
	@Autowired
	SeedMapper seedMapper;
	@Autowired
	ReceiveMapper receiveMappser;
	@Autowired
	RoomMapper roomMappser;
	@Autowired
	ComService comService;	
			
	// [카카오페이 뿌리기 기능3] 조회
	public SuccessResponse search (String token, int userId) throws ApiException {		
		// 해당 뿌리기 정보 가져오기	
		Seed seed = seedMapper.selectSeedByToken(token);	
		
		// 유효하지 않은 token일 경우 조회 불가
		if (seed == null) {
			throw new ApiException(ApiError.INVALID_TOKEN);
		}
			
		// 뿌린 사람이 자신이 아닌 경우 조회 불가
		if (!seed.getUserId().equals(userId)) {
			throw new ApiException(ApiError.NOT_YOUR_SEED);			
		}
		
		// 뿌린 후 7일이 지난 경우 조회 불가
		if (comService.getPeriod(seed.getRegDate(), "d") > 7) {
			throw new ApiException(ApiError.INQUIRY_PERIOD_OVER);
		}
		
		try {
			// 받기 완료된 정보 가져오기
			ArrayList<Object> receivedList = new ArrayList<Object>();
			int sum = 0;			
			for (Receive receive : receiveMappser.selectReceiveListByToken(token)) {
				if (receive.getUserId() != null) { // 받기 완료된 분배건
					HashMap<String, Object> m = new HashMap<>();			
					m.put("amount", receive.getAmount());
					m.put("receivedUser", receive.getUserId());
					sum += receive.getAmount();					
					receivedList.add(m);
				}
			}
			
			// response data
			HashMap<String, Object> data = new HashMap<>();
			data.put("time", seed.getRegDate()); // 뿌린 시각
			data.put("total amount", seed.getAmount()); // 뿌린 금액
			data.put("received amount", sum); // 받기 완료된 금액
			data.put("receive info", receivedList); // 받기 완료된 정보List
			
			// response
			SuccessResponse sResponse = new SuccessResponse();					
			sResponse.setMessage("receive success!");
			sResponse.setData(data);
			return sResponse;
			
		} catch (Exception e) {
			throw new ApiException(ApiError.RECEIVE_ERROR, e.getLocalizedMessage());
		}
	}	
}
