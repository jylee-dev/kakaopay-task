package com.jiyoon.kakaopaytask.constant;

import lombok.Getter;

public enum ApiError {
	ERROR(0,"오류"),
	SEED_ERROR(10, "뿌리기 오류"),
	RECEIVE_ERROR(20, "받기 오류"),
	RECEIVE_YOURS(21, "자신이 뿌린 건은 받을 수 없습니다."),
	RECEIVE_PERIOD_OVER(22, "뿌린지 10분이 지난 건은 받을 수 없습니다."),
	NOT_SAME_ROOM_USER(23, "해당 뿌리기 대화방에 속하지 않은 사람은 받을 수 없습니다."),
	RECEIVE_DUPLICATE(24, "이미 받은 사람은 받을 수 없습니다."),
	RECEIVE_FINISH(25, "모든 받기가 완료된 건입니다."),
	SEARCH_ERROR(30, "조회 오류"),
	INVALID_TOKEN(31, "유효하지 않은 token 입니다."),
	NOT_YOUR_SEED(32, "자신이 뿌린 건이 아니라서 조회할 수 없습니다."),
	INQUIRY_PERIOD_OVER(33, "뿌린지 7일이 지난 건은 조회할 수 없습니다.");
	
	@Getter
	private final int code;
	@Getter
	private final String desc;	
	
	ApiError(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}