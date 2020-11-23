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
			
	// [īī������ �Ѹ��� ���3] ��ȸ
	public SuccessResponse search (String token, int userId) throws ApiException {		
		// �ش� �Ѹ��� ���� ��������	
		Seed seed = seedMapper.selectSeedByToken(token);	
		
		// ��ȿ���� ���� token�� ��� ��ȸ �Ұ�
		if (seed == null) {
			throw new ApiException(ApiError.INVALID_TOKEN);
		}
			
		// �Ѹ� ����� �ڽ��� �ƴ� ��� ��ȸ �Ұ�
		if (!seed.getUserId().equals(userId)) {
			throw new ApiException(ApiError.NOT_YOUR_SEED);			
		}
		
		// �Ѹ� �� 7���� ���� ��� ��ȸ �Ұ�
		if (comService.getPeriod(seed.getRegDate(), "d") > 7) {
			throw new ApiException(ApiError.INQUIRY_PERIOD_OVER);
		}
		
		try {
			// �ޱ� �Ϸ�� ���� ��������
			ArrayList<Object> receivedList = new ArrayList<Object>();
			int sum = 0;			
			for (Receive receive : receiveMappser.selectReceiveListByToken(token)) {
				if (receive.getUserId() != null) { // �ޱ� �Ϸ�� �й��
					HashMap<String, Object> m = new HashMap<>();			
					m.put("amount", receive.getAmount());
					m.put("receivedUser", receive.getUserId());
					sum += receive.getAmount();					
					receivedList.add(m);
				}
			}
			
			// response data
			HashMap<String, Object> data = new HashMap<>();
			data.put("time", seed.getRegDate()); // �Ѹ� �ð�
			data.put("total amount", seed.getAmount()); // �Ѹ� �ݾ�
			data.put("received amount", sum); // �ޱ� �Ϸ�� �ݾ�
			data.put("receive info", receivedList); // �ޱ� �Ϸ�� ����List
			
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
