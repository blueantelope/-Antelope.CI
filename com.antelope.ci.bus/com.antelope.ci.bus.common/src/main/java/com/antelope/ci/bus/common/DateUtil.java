// com.antelope.ci.bus.common.DateUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-23		下午4:37:11 
 */
public class DateUtil {
	private static final String defaultDatePattern = "yyyy-MM-dd";						// 默认的日期样式
	private static final String defaultTimePattern = "yyyy-MM-dd HH:mm:ss";		// 默认的时间样式
	private static final String minutePattern = "yyyy-MM-dd HH:mm";				// 分钟时间样式
	private static final String hourPattern = "yyyy-MM-dd HH";							// 小时时间样式
	
	
	public static Date toDate(String date) throws CIBusException  { 
		try {
			return new SimpleDateFormat(defaultDatePattern).parse(date);
		} catch (ParseException e) {
			throw new CIBusException("", e);
		} 
	}
	
	public static String formatDay(Date date) {
		return new SimpleDateFormat(defaultDatePattern).format(date);
	}
	
	public static String formatTime(Date date) {
		return new SimpleDateFormat(defaultTimePattern).format(date);
	}
	
	public static int differDay(Date d1, Date d2) throws CIBusException {
		String hourPattern = "yyyy-MM-dd";
		long hours = 24L * 60L * 60L * 1000L;
		return differTime(d1, d2, hourPattern, hours);
	}
	
	private static int differTime(Date d1, Date d2, String timePattern, long differ) throws CIBusException {
		SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);
		String s1 = timeFormat.format(d1);
		String s2 = timeFormat.format(d2);
		try {
			return (int) ((timeFormat.parse(s1).getTime() - timeFormat.parse(s2).getTime()) / differ);
		} catch (ParseException e) {
			throw new CIBusException("", e);
		}
	}
	
	public static Date parseDate(String date) throws CIBusException {
		return parse(date, defaultDatePattern);
	}
	
	public static Date parseTime(String time) throws CIBusException {
		return parse(time, defaultTimePattern);
	}
	
	private static Date parse(String time, String format) throws CIBusException {
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(time);
		} catch (ParseException e) {
			throw new CIBusException("", e);
		}
	}
}

