package com.jiyoon.kakaopaytask.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jiyoon.kakaopaytask.model.Room;

@Mapper
public interface RoomMapper {	
	List<Room> selectRoomByRoomId(String roomId);	
}
