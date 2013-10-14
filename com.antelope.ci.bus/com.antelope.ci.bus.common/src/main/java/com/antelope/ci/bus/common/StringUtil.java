// com.antelope.ci.bus.common.StringUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

/**
 * 字符中通用工具类
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-9-26 下午9:03:45
 */
public class StringUtil {
	private static final char[] letters = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 将url列表转换为字符串表示，url间的分割符由delim定义
	 * 
	 * @param @param urlList
	 * @param @param delim
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String convertUrlList(List<URL> urlList, String delim) {
		String us = "";
		for (URL url : urlList) {
			us += url.toString() + delim;
		}
		if (us.length() > 0)
			us = us.substring(0, us.length() - 1);

		return us;
	}

	public static String bytes2hex(byte[] data) {
		StringBuilder builder = new StringBuilder(data.length * 2);
		for (byte b : data) {
			builder.append(letters[(b >> 4) & 0xf]).append(letters[b & 0xf]);
		}
		return builder.toString();
	}

	public static String getFixLengthString(String str, int length, char token,
			int direction) {
		int origLength = getStringLength(str);
		if (origLength > length) {
			int width = str.length();
			if (width > length)
				return str.substring(0, length);
			return str;
		} else {
			int delta = length - origLength;
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < delta; i++)
				builder.append(token);

			if (direction > 0)
				return str + builder;
			return builder + str;
		}
	}
	
	private static int getStringLength (String str) {
        try {
            return new String (str.getBytes (), "8859_1").length ();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException ("UnsupportedEncodingException Exception encountered", e);
        }
    }
	
	/**
	 * 截取第一个大写字母之后的字符串
	 * @param  @param str
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String trimStrUpper(String str) {
		int n = 0;
		for (byte b : str.getBytes()) {
			int i = (int) b;
			if (i >= 65 && i<= 90)
				break;
			n++;
		}
		
		return str.substring(n);
	}
}
