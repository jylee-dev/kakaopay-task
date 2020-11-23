package com.jiyoon.kakaopaytask.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.jiyoon.kakaopaytask.model.Seed;

@Mapper
public interface SeedMapper {	
	Seed selectSeedByToken(String token);
	void insertSeed(Seed seed);
}
