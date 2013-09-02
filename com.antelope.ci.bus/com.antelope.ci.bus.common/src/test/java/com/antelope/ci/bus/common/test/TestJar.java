// com.antelope.ci.bus.common.test.TestJar.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-2		下午9:40:20 
 */
public class TestJar extends TestCase {
	private URL test_url;
	private static final int BUFFER_SIZE = 2048;
	
	@Test
	public void testClassPath() throws Exception {
		String class_name = "javax.crypto.Cipher";
		Class c = Class.forName(class_name);
		String[] cns = class_name.split("\\.");
		System.out.println(cns.length);
		System.out.println(cns[cns.length-1]);
		
//		URL u =  c.getResource("");
//		u = new URL(u.toString() + "Cipher.class");
//		InputStream in = u.openStream();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		byte[] bs = new byte[BUFFER_SIZE];
//		while (in.read(bs) != -1) {
//			for (byte b : bs) {
//				System.out.print(b);
//			}
//			System.out.println(toHexString(bs));
//			out.write(bs);
//			out.flush();
//		}
//		System.out.println(toHexString(out.toByteArray()));
	}
	
	@Test
	public void testJarUrl() {
		 try {
//			test_url = new URL("jar:file:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/jce.jar!/javax/crypto/");
//			InputStream in = new FileInputStream(new File(test_url.getFile()));
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			byte[] bs = new byte[BUFFER_SIZE];
//			while (in.read(bs) != -1) {
//				for (byte b : bs) {
//					System.out.print(b);
//				}
//				System.out.println(toHexString(bs));
//				out.write(bs);
//				out.flush();
//			}
//			System.out.println(toHexString(out.toByteArray()));
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	/*
	 * 将byte数组转换为16进制显示的字符串
	 */
	private String toHexString(byte[] buf) {
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
	private int appendHexLine(StringBuffer bufInfo, byte[] buf, int index) {
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

	/*
	 * byte转换成16进制字符串
	 */
	private String toHexString(byte b) {
		char[] buffer = new char[2];
        buffer[0] = Character.forDigit((b >>> 4) & 0x0F, 16);
        buffer[1] = Character.forDigit(b & 0x0F, 16);
        return new String(buffer);
    }

	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestJar.class);
	}
}

