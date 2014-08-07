// com.antelope.ci.bus.common.StringUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.antelope.ci.bus.common.exception.CIBusException;

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

	private static int getStringLength(String str) {
		try {
			return new String(str.getBytes(), "8859_1").length();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"UnsupportedEncodingException Exception encountered", e);
		}
	}

	/**
	 * 截取第一个大写字母之后的字符串
	 * 
	 * @param @param str
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String trimStrUpper(String str) {
		int n = 0;
		for (byte b : str.getBytes()) {
			int i = (int) b;
			if (i >= 65 && i <= 90)
				break;
			n++;
		}

		return str.substring(n);
	}

	public static boolean empty(String str) {
		if (str != null && str.length() > 0)
			return false;
		return true;
	}

	public static String[] toLines(String str) throws CIBusException {
		try {
			BufferedReader reader = new BufferedReader(new StringReader(str));
			List<String> lineList = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null)
				lineList.add(line);
			return lineList.toArray(new String[lineList.size()]);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}

	public static String[] toStringLines(String str, int width) throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(str));
		List<String> lineList = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			int len = getWordCount(line);
			if (len < width) {
				lineList.add(line);
			} else {
				int start = 0;
				while (start < len) {
					int end = (start + width) > len ? len : start + width;
					lineList.add(line.substring(start, end));
					start += width;
				}
			}
		}
		return lineList.toArray(new String[lineList.size()]);
	}
	
	public static String[] toLines(String str, int width) throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(str));
		List<String> lineList = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			int len = 0;
			int last_size = 0;
			int s_start = 0;
			int index = 0;
			for (index = 0; index < line.length(); index++) {
				if (len == width) {
					lineList.add(line.substring(s_start, index));
					s_start = index;
					len  = 0;
				} else if (len > width) {
					lineList.add(line.substring(s_start, index-1));
					s_start = index - 1;
					len  = last_size;
				}
				
				int codePoint = Character.codePointAt(line, index);
				if (codePoint >= 0 && codePoint <= 255) {
					len += 1;
					last_size = 1;
				} else {
					len += 2;
					last_size = 2;
				}
			}
			
			if (index > s_start)
				lineList.add(line.substring(s_start, index));
		}
		return lineList.toArray(new String[lineList.size()]);
	}
	
	public static String subString(String str, int start) throws CIBusException {
		return subString(str, start, getWordCount(str));
	}
	
	public static String subString(String str, int start, int end) throws CIBusException {
		if (start < 0 || end < 0 || end < start || end > getWordCount(str))
			throw new CIBusException("", "subString exception occur, index not fit rule");
		
		boolean started = false;
		boolean ended = false;
		int str_start = 0;
		int str_end = 0;
		int length = 0;
		int str_len = str.length();
		for (int i = 0; i <= str_len; i++) {
			if (length >= end && started) {
				str_end = i;
				if (length > end)
					str_end -= 1;
				ended = true;
				break;
			}
			
			int codePoint = Character.codePointAt(str, i);
			if (codePoint >= 0 && codePoint <= 255)
				length++;
			else
				length+= 2;
			
			if (length >= start && !started) {
				str_start = i;
				if (length > start && str_start > 0)
					str_start -= 1;
				started = true;
			}
		}
		if (!ended)
			str_end = length;
		
		return str.substring(str_start, str_end);
	}

	public static int maxLine(String str) throws CIBusException {
		String[] lines = toLines(str);
		int maxWidth = 0;
		for (String line : lines) {
			maxWidth = maxWidth > line.length() ? maxWidth : line.length();
		}

		return maxWidth;
	}

	public static int getWordCount(String s) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int codePoint = Character.codePointAt(s, i);
			if (codePoint >= 0 && codePoint <= 255)
				length++;
			else
				length += 2;
		}

		return length;
	}

	public static String getLastName(String value, String  deco) {
		String[] ss = value.split(deco);
		return ss[ss.length-1];
	}
	
	public static boolean startsWithIgnoreCase(String value, String prefix) {
		value = value.trim();
		if (value.length() < prefix.length())
				return false;
		return value.substring(0, prefix.length()).equalsIgnoreCase(prefix);
	}
	
	public static boolean endsWithIgnoreCase(String value, String suffix) {
		value = value.trim();
		if (value.length() < suffix.length())
				return false;
		return value.substring(value.length()-suffix.length()).equalsIgnoreCase(suffix);
	}
	
	public static String repeatString(String repeat, int times) {
		StringBuffer result = new StringBuffer();
		while (times > 0) {
			result.append(repeat);
			times--;
		}
		
		return result.toString();
	}
	
	public static boolean contain(String str, String prefix, String suffix) {
		String reg = "(" + prefix + ".+" + suffix + "){1,}";
		Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find())
        	return true;
        return false;
	}
	
	public static boolean equalsIgnoreCase(String vtStr, String baseStr) {
		if (vtStr.length() > 1 || baseStr.length() > 1) 		return vtStr.equalsIgnoreCase(baseStr);
		else																return equalsIgnoreCaseWithChar(vtStr, baseStr);
	}
	
	public static boolean equalsIgnoreCaseWithChar(String vtStr, String baseStr) {
		if (vtStr.length() == 1 && baseStr.length() == 1) {
			char vs = vtStr.toLowerCase().charAt(0);
			char bc = baseStr.toLowerCase().charAt(0);
			EU_NetVTKey nv = EU_NetVTKey.fromVtkey((int) vs);
			if (nv != null)		vs = nv.getC();
			return vs == bc ? true : false;
		}
		
		return false;
	}
	
	public static boolean isNumeric(String str) {
		if (str.matches("\\d*")) return true;
		return false;
	}
	
	public static String formatString(String str, String prefix, String suffix) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		while (true) {
			int start = str.indexOf(prefix, index);
			if (start != -1) {
				int end = str.indexOf(suffix, start);
				if (end != -1) {
					String loop = str.substring(start+1, end);
					if (isNumeric(loop)) {
						if (start > index) {
							String loopStr = str.substring(index, start);
							int l = Integer.parseInt(loop);
							while (l > 0) {
								sb.append(loopStr);
								l--;
							}
						}
						index = end + 1;
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}
		
		if (index < str.length())
			sb.append(str.substring(index));
		
		return sb.toString();
	}
}
