// com.antelope.ci.bus.server.shell.buffer.test.TestCharBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer.test;

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.StringUtil;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月15日		下午3:13:55 
 */
public class TestCharBuffer extends TestCase {
	private static final int BUFFER_SIZE = 64;
	
	@Test public void test() {
		byte[] chs = new byte[]{(byte)228, (byte)184, (byte)128};
		try {
			System.out.println(new String(chs, "utf8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		CharBuffer buffer = CharBuffer.allocate(BUFFER_SIZE);
		buffer.put('a');
		buffer.put('b');
		buffer.put('c');
		buffer.put('d');
		int length = buffer.position() - 2;
		char[] append = new char[length];
		buffer.position(2);
		buffer.mark();
		buffer.get(append);
		buffer.reset();
		buffer.put(StringUtil.insert(append, 'e'));
		System.out.println(buffer.position());
		buffer.flip();
		System.out.println(buffer.toString());
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestCharBuffer.class);
	}
}

