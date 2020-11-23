package com.jiyoon.kakaopaytask.constant;

import lombok.Getter;

public enum ApiError {
	ERROR(0,"����"),
	SEED_ERROR(10, "�Ѹ��� ����"),
	RECEIVE_ERROR(20, "�ޱ� ����"),
	RECEIVE_YOURS(21, "�ڽ��� �Ѹ� ���� ���� �� �����ϴ�."),
	RECEIVE_PERIOD_OVER(22, "�Ѹ��� 10���� ���� ���� ���� �� �����ϴ�."),
	NOT_SAME_ROOM_USER(23, "�ش� �Ѹ��� ��ȭ�濡 ������ ���� ����� ���� �� �����ϴ�."),
	RECEIVE_DUPLICATE(24, "�̹� ���� ����� ���� �� �����ϴ�."),
	RECEIVE_FINISH(25, "��� �ޱⰡ �Ϸ�� ���Դϴ�."),
	SEARCH_ERROR(30, "��ȸ ����"),
	INVALID_TOKEN(31, "��ȿ���� ���� token �Դϴ�."),
	NOT_YOUR_SEED(32, "�ڽ��� �Ѹ� ���� �ƴ϶� ��ȸ�� �� �����ϴ�."),
	INQUIRY_PERIOD_OVER(33, "�Ѹ��� 7���� ���� ���� ��ȸ�� �� �����ϴ�.");
	
	@Getter
	private final int code;
	@Getter
	private final String desc;	
	
	ApiError(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
