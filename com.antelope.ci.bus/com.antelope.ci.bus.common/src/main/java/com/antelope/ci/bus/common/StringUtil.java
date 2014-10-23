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
			int len = lengthVT(line);
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
	
	public static String subStringVT(String str, int start) throws CIBusException {
		return subStringVT(str, start, lengthVT(str));
	}

	public static String subStringVT(String str, int start, int end) throws CIBusException {
		if (start < 0 || end < 0 || end < start || end > lengthVT(str))
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

	public static int lengthVT(String s) {
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
	
	public static String deleteFirst(String str, String header, String tail) {
		int start = str.indexOf(header);
		if (start  != -1) {
			int end = str.indexOf(tail, start+header.length());
			if (end != -1)
				return str.substring(end+tail.length());
		}
		
		return str;
	}
	
	public static String find(String str, String header, String tail) {
		int start = str.indexOf(header);
		if (start  != -1) {
			start += header.length();
			int end = str.indexOf(tail, start);
			if (end != -1)
				return str.substring(start, end);
		}
		return null;
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
	
	public static String loopString(String str, String prefix, String suffix) {
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
	
	/**
	 * 将byte数组转换为16进制显示的字符串
	 * @param  @param buf
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String toHexString(byte[] buf) {
		// 16进制包体内容
		StringBuffer bufInfo = new StringBuffer();
		int bufIndex = 0;
		int count = buf.length / 16;
		if (buf.length % 16 != 0) {
			count += 1;
		}
		int n = 0;
		while (n < 35) {
			bufInfo.append("--");
			n++;
		}
		bufInfo.append("\n");
		while (count > 0) {
			bufIndex = appendHexLine(bufInfo, buf, bufIndex);	// 每16位取出一行数据
			count--;
		}
		n = 0;
		while (n < 35) {
			bufInfo.append("--");
			n++;
		}
		return  bufInfo.toString();
	}
	
	/*
	 * 取出byte数组中每行数据，16个字节为一行，
	 * 并将这一行放入buffer中，返回byte数组读
	 * 取到的下标
	 */
	private static int appendHexLine(StringBuffer bufInfo, byte[] buf, int index) {
		int lineCount = 16;
		byte[] lineBs = new byte[lineCount];
		int lineIndex = 0;
		
		while (index < buf.length && lineIndex < lineCount) {
			lineBs[lineIndex] = buf[index];
			bufInfo.append(toHexString(lineBs[lineIndex])).append((char)32);
			lineIndex++;
			index++;
		}
		while (lineIndex < lineCount) {
			bufInfo.append((char)32).append((char)32).append((char)32);
			lineIndex++;
		}
		String lineInfo = new String();
		for (byte b : lineBs) {
			if (b >= 32 && b <= 126) {
				lineInfo += (char) b;
			} else {
				lineInfo += '.';
			}
			
		}
		bufInfo.append("; " + lineInfo + "\n");
		
		return index;
	}
	
	/**
	 * byte转换成16进制字符串
	 * @param  @param b
	 * @param  @return
	 * @return String
	 * @throws
	 */
	private static String toHexString(byte b) {
		char[] buffer = new char[2];
        buffer[0] = Character.forDigit((b >>> 4) & 0x0F, 16);
        buffer[1] = Character.forDigit(b & 0x0F, 16);
        return new String(buffer);
    }
	
	public static String deleteFirst(String str, String delStr) {
		String str_lower = str.toLowerCase();
		String delStr_lower = delStr.toLowerCase();
		int index = str_lower.indexOf(delStr_lower);
		if (index != -1)
			return str.substring(index+delStr_lower.length());
		return str;
	}
	
	public static String deleteLast(String str, String delStr) {
		String str_lower = str.toLowerCase();
		String delStr_lower = delStr.toLowerCase();
		int index = str_lower.lastIndexOf(delStr_lower);
		if (index != -1)
			return str.substring(0, index);
		return str;
	}
	
	public static String peel(String str, String prefix, String suffix) {
		String ret = str;
		if (containEndsite(str, prefix, suffix)) {
			ret = StringUtil.deleteFirst(str, prefix);
			ret = StringUtil.deleteLast(ret, suffix); 
		}
		
		return ret;
	}
	
	public static boolean containEndsite(String str, String prefix, String suffix) {
		if (StringUtil.empty(str))
			return false;
		str = str.trim();
		if (StringUtil.startsWithIgnoreCase(str, prefix) && str.endsWith(suffix))
			return true;
		return false;
	}
	
	public static int compare(String s1, String s2) {
		if (s1 == null && s2 == null)
			return 0;
		if (s1 != null && s2 == null)
			return 1;
		if (s1 == null && s2 != null)
			return -1;
		return s1.trim().toLowerCase().compareTo(s2.trim().toLowerCase());
	}
	
	public static char[] insert(char[] src, char c) {
		char[] dst = new char[src.length + 1];
		dst[0] = c;
		System.arraycopy(src, 0, dst, 1, src.length);
		return dst;
	}
	
	public static char[] concat(char[] src1, char[] src2) {
		if (src1 == null && src2 == null)
			return null;
		if (src1 == null)
			return src2;
		if (src2 == null)
			return src1;
		
		char[] dst = new char[src1.length + src2.length];
		System.arraycopy(src1, 0, dst, 0, src1.length);
		System.arraycopy(src2, 0, dst, src1.length, src2.length);
		return dst;
	}
	
	public static int placeholder(char c) {
		if (((byte) c) >= 0)
			return 0;
		return 2;
	}
	
    public static final boolean isChinese(char c) {   
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }
    
    public static String subString(String str, String start_str, String end_str) {
    	int start_index = str.indexOf(start_str);
    	if (start_index != -1) {
    		int end_index = str.indexOf(end_str, start_index+start_str.length());
    		if (end_index != -1)
    			return str.substring(start_index, end_index+end_str.length());
    	}
    	
    	return "";
    }
}