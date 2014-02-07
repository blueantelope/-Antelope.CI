// com.antelope.ci.bus.server.shell.ShellUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.IOException;

import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-9		下午5:23:32 
 */
public class ShellUtil {
	public enum VER {STATIC, LEFT, RIGHT};
	public enum HOR{STATIC, UP, DOWN};

	
	public static void clear(TerminalIO io) throws CIBusException {
		try {
			io.eraseScreen ();
			io.homeCursor ();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	public static void resetIo(TerminalIO io) throws IOException {
		io.setUnderlined(false);
		io.setBold(false);
		io.setReverse(false);
	}
	
	public static void backspace(TerminalIO io) throws IOException {
		io.moveLeft(1); // 光标左移一位
		io.eraseToEndOfLine(); // 删除光标到行尾部分的内容
	}
	
	public static void shiftTop(TerminalIO io) throws IOException {
		io.homeCursor();
	}
	
	public static void shiftBottom(TerminalIO io, int height) throws IOException {
		io.homeCursor();
		io.moveDown(height - 1);
	}
	
	public static void shift(TerminalIO io, int x, int y, int width, int height) throws IOException {
		io.homeCursor();
		int cx = x > width ? width : x;
		int cy = y > height ? height : y;
		io.moveLeft(cx);
		io.moveDown(cy);
	}
	
	public static void writeHeader(TerminalIO io, String str) throws IOException {
		shiftTop(io);
		String[] ss = StringUtil.toLines(str);
		for (String s : ss) {
			io.println(s);
		}
	}
	
	public static void writeTail(TerminalIO io, String str, int width, int height) throws IOException {
		shiftBottom(io, height);
		String[] ss = StringUtil.toLines(str);
		int distance = ss.length;
		for (String s : ss) {
			distance += s.length() / width;
		}
		io.moveUp(distance);
		for (String s : ss) {
			io.println(s);
		}
	}
}

