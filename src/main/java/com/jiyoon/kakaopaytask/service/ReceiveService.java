package com.jiyoon.kakaopaytask.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiyoon.kakaopaytask.constant.ApiError;
import com.jiyoon.kakaopaytask.exception.ApiException;
import com.jiyoon.kakaopaytask.mapper.ReceiveMapper;
import com.jiyoon.kakaopaytask.mapper.RoomMapper;
import com.jiyoon.kakaopaytask.mapper.SeedMapper;
import com.jiyoon.kakaopaytask.model.Receive;
import com.jiyoon.kakaopaytask.model.SuccessResponse;
import com.jiyoon.kakaopaytask.model.Room;
import com.jiyoon.kakaopaytask.model.Seed;

@Service
public class ReceiveService {
	@Autowired
	SeedMapper seedMapper;
	@Autowired
	ReceiveMapper receiveMapper;
	@Autowired
	RoomMapper roomMappser;
	@Autowired
	ComService comService;
	
	// [카카오페이 뿌리기 기능2] 받기
	@Transactional
	public SuccessResponse receive(String token, int userId) throws ApiException {
		// 해당 뿌리기 정보 가져오기
		Seed seed = seedMapper.selectSeedByToken(token);
		
		// 자신이 뿌린 건일 경우 받기 불가
		if (userId == seed.getUserId()) {			
			throw new ApiException(ApiError.RECEIVE_YOURS);
		}
		
		// 뿌린 후 10분이 지났을 경우 받기 불가
		if (comService.getPeriod(seed.getRegDate(), "m") > 10) {
			throw new ApiException(ApiError.RECEIVE_PERIOD_OVER);
		}
		
		// 뿌리기가 호출된 대화방에 속한 사용자가 아닌 경우 받기 불가
		if (!isSameRoomUser(seed.getRoomId(), userId)) {
			throw new ApiException(ApiError.NOT_SAME_ROOM_USER);
		}	
			
		// 미할당된 분배건 검색 및 할당
		int receivedAmount = -1; // 받은 금액
		for (Receive receive : receiveMapper.selectReceiveListByToken(token)) {
			if (receive.getUserId() == null || receive.getUserId().equals(null)) {
				receive.setUserId(userId);  // 할당 받은 userId 값 세팅
				receiveMapper.updateUserIdBySeq(receive); // 할당 받은 userId 정보 업데이트
				receivedAmount = receive.getAmount(); // 받은 금액 저장
				break;
			} else if (receive.getUserId() == userId) { // 이미  받은 사람일 경우 받기 불가
				throw new ApiException(ApiError.RECEIVE_DUPLICATE);
			}
		}
		
		// 미할당 건을 찾지 못한 경우 받기 불가 (받기 완료된 뿌리기 건)
		if (receivedAmount == -1) {
			throw new ApiException(ApiError.RECEIVE_FINISH);
		}
		
		try {
			// response data
			HashMap<String, Object> data = new HashMap<>();
			data.put("received amount", receivedAmount); // 받은 금액
			
			// response
			SuccessResponse sResponse = new SuccessResponse();					
			sResponse.setMessage("receive success!");
			sResponse.setData(data);
			return sResponse;
			
		} catch (Exception e) {
			throw new ApiException(ApiError.RECEIVE_ERROR, e.getLocalizedMessage());
		}
	}
	
	// 해당 대화방에 사용자가 있는지 확인
	public boolean isSameRoomUser(String roomId, int userId) {		
		for (Room room : roomMappser.selectRoomByRoomId(roomId)) {
			if (room.getUserId().equals(userId)) {
				return true;
			}
		}
		return false;
	}
}
