package com.jiyoon.kakaopaytask.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.jiyoon.kakaopaytask.constant.ApiError;
import com.jiyoon.kakaopaytask.exception.ApiException;

@Service
public class ComService {	
	public Long getPeriod(String strDate, String st) throws ApiException {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = fm.parse(strDate);			
			Date now = new Date(); // 현재 시각			
			int div;
			switch (st) {
			case "m":
				div = 1000*60; // 1000밀리초*60초
				break;
			case "h":
				div = 1000*60*60; // 1000밀리초*60초*60분
				break;
			case "d":
				div = 1000*60*60*24; // 1000밀리초*60초*60분*24시간 
				break;
			case "y":
				div = 1000*60*60*24*365; // 1000밀리초*60초*60분*24시간*365일
				break;
			default:
				div = 1;
				break;
			}
			return (now.getTime() - date.getTime()) / div;
		
		} catch (ParseException e) {
			throw new ApiException(ApiError.ERROR, e.getLocalizedMessage());			
		}
	}
}