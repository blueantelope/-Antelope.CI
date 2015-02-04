// com.antelope.ci.bus.common.NetUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 * stream utilities
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月4日		上午11:13:01 
 */
public class StreamUtil {
	/*
	 * hex and stream
	 */
	// 将字节流转换为16进制显示的字符串
	public static String toHex(byte[] buf) {
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
			bufIndex = toHexLine(bufInfo, buf, bufIndex);	// 每16位取出一行数据
			count--;
		}
		n = 0;
		while (n < 35) {
			bufInfo.append("--");
			n++;
		}
		return  bufInfo.toString();
	}
	
	private static int toHexLine(StringBuffer bufInfo, byte[] buf, int index) {
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
	
	// byte转换成16进制字符串
	public static String toHexString(byte b) {
		char[] buffer = new char[2];
        buffer[0] = Character.forDigit((b >>> 4) & 0x0F, 16);
        buffer[1] = Character.forDigit(b & 0x0F, 16);
        return new String(buffer);
    }
	
	/*
	 * short and stream
	 */
	// byte数组转换成short类型
	public static short toShort(byte[] bs, int index) {
		return toShort(new byte[]{bs[index], bs[index+1]});
	}
	
	// 无符号byte数组转换成short类型
	public static int toUnsignedShort(byte[] bs, int index) {
		return toUnsignedShort(new byte[]{bs[index], bs[index+1]});
	}
	
	// byte数组转换成short类型
	public static final short toShort(byte[] b) {
		return (short) ( (b[0] << 8) + (b[1] & 0xFF));
	}
	
	// 无符号byte数组转换成short类型
	public static final int toUnsignedShort(byte[] b) {
		int s1 = (toInt(b[0]) & 0xFF) << 8;
		int s2 =  toInt(b[1]) & 0xFF;
		return s1 + s2;
	}
	
	/*
	 * int and stream
	 */
	public static final long toUnsignedInt(byte[] b) {
		long i1 = (toInt(b[0]) & 0x0FFFFL) << 24;
		long i2 = (toInt(b[1]) & 0x0FFFFL) << 16;
		long i3 = (toInt(b[2]) & 0x0FFFFL) << 8;
		long i4 = toInt(b[3]) & 0x0FFFFL;
		return i1 + i2 + i3 + i4;
	}
	
	// int类型转换成byte数组
	public static byte[] toStream(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}
	
	// 无符号int类型转换成byte数组
	public static byte[] toStream(long value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}
	
	// 2个字节数据转换为int型
	public static final int toInt(byte[] bs) {
		return (toInt(bs[0]) << 8) + toInt(bs[1]);
	}
	
	// byte转换成无符号int
	public static final int toInt(byte b) {
		return b & 0xFF;
	}
}
