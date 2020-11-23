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
		
	// [īī������ �Ѹ��� ���1] �Ѹ���
	@Transactional
	public SuccessResponse Seed(int amount, int cnt, int userId, String roomId) throws ApiException {
		// token ��������
		String token = getToken();
		
		// �Ѹ��� ���� ����
		seedMapper.insertSeed(new Seed(token, amount, cnt, userId, roomId));
		
		// �ο����� �й�� �ݾ� ��������
		int[] amountArry = distribution(amount, cnt);
		
		// �й�� ���� ���� (�й�� ���� = �ޱ� ���� ����)
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
	
	// token ���� (3�ڸ� ���� ���ڿ� = (���빮��) + (���ҹ���) + (����))
	public String getToken() {
		Random rand = new Random();
		char[] chrArry = new char[3];
		chrArry[0] = (char)(rand.nextInt(26) + 'A'); // 'A'~'Z' ���� ����
		chrArry[1] = (char)(rand.nextInt(26) + 'a'); // 'a'~'z' ���� ����
		chrArry[2] = (char)(rand.nextInt(10) + '0'); // '0'~'9' ���� ����
		
		return String.valueOf(chrArry);		
	}
	
	// �ο������� �ݾ� �й�
	public int[] distribution(int amount, int cnt) {
		Random rand = new Random();
		int[] amountArry = new int[cnt]; // �й�� �ݾ�
		int remain = amount; // �ܾ�
		
		for (int i = 0; i < cnt; i ++) {
			if (i != cnt -1) {
				amountArry[i] = (remain) * (rand.nextInt(9) + 1) / 100 * 10; // ���� �ݾ׿��� 10~90% �� �������� �й�, 1�� ���� �ݾ� �ȳ������� ó��
				remain -= amountArry[i]; // �ܾ� ����
			} else {				
				amountArry[i] = remain; // �ܾ��� ���� �ʵ��� ������ ���� ���� �ݾ����� ����
			}			
		}
		return amountArry;
	}
}
