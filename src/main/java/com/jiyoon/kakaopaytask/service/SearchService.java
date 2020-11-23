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
			
	public SuccessResponse search (String token, int userId) throws ApiException {
		Seed seed = seedMapper.selectSeedByToken(token);	
		
		if (seed == null) {
			throw new ApiException(ApiError.INVALID_TOKEN);
		}
		
		if (!seed.getUserId().equals(userId)) {
			throw new ApiException(ApiError.NOT_YOUR_SEED);			
		}
		
		if (comService.getPeriod(seed.getRegDate(), "d") > 7) {
			throw new ApiException(ApiError.INQUIRY_PERIOD_OVER);
		}
		
		try {		
			ArrayList<Object> receivedList = new ArrayList<Object>();
			int sum = 0;			
			for (Receive receive : receiveMappser.selectReceiveListByToken(token)) {
				if (receive.getUserId() != null) {
					HashMap<String, Object> m = new HashMap<>();			
					m.put("amount", receive.getAmount());
					m.put("receivedUser", receive.getUserId());
					sum += receive.getAmount();					
					receivedList.add(m);
				}
			}
			
			// response data
			HashMap<String, Object> data = new HashMap<>();
			data.put("time", seed.getRegDate());
			data.put("total amount", seed.getAmount());
			data.put("received amount", sum);
			data.put("receive info", receivedList);
			
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
