package com.jiyoon.kakaopaytask.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jiyoon.kakaopaytask.model.Receive;

@Mapper
public interface ReceiveMapper {	
	List<Receive> selectReceiveListByToken(String token);
	void insertReceive(Receive receive);
	void updateUserIdBySeq(Receive receive);
}
