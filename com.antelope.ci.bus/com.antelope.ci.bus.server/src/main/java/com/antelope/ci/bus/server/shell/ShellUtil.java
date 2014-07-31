// com.antelope.ci.bus.server.shell.ShellUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-9		下午5:23:32 
 */
public class ShellUtil {
	private static final Logger log = Logger.getLogger(ShellUtil.class);
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
	
	public static void backspace(TerminalIO io) throws CIBusException {
		try {
			io.moveLeft(1); // 光标左移一位
			io.eraseToEndOfLine(); // 删除光标到行尾部分的内容
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	public static void shiftTop(TerminalIO io) throws CIBusException {
		try {
			io.homeCursor();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	public static void shiftBottom(TerminalIO io, int height) throws CIBusException {
		try {
			io.homeCursor();
			io.moveDown(height - 1);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	public static void shift(TerminalIO io, int x, int y, int width, int height) throws CIBusException {
		try {
			io.homeCursor();
			int cx = x > width ? width : x;
			int cy = y > height ? height : y;
			if (cx != 0) {
				if (cx > 0)
					io.moveLeft(cx);
				else
					io.moveRight(-cx);
			}
			if (cy != 0) {
				if (cy > 0)
					io.moveDown(cy);
				else
					io.moveUp(-cy);
			}
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	public static void writeHeader(TerminalIO io, String str) throws CIBusException {
		shiftTop(io);
		String[] ss = StringUtil.toLines(str);
		for (String s : ss) {
			println(io, s);
		}
	}
	
	public static void writeTail(TerminalIO io, String str, int width, int height) throws CIBusException {
		shiftBottom(io, height);
		String[] ss = StringUtil.toLines(str);
		int distance = ss.length;
		for (String s : ss) {
			distance += s.length() / width;
		}
	
		shiftBottom(io, height);
		try {
			io.moveUp(distance);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
		for (String s : ss) {
			println(io, s);
		}
	}
	
	public static void move(TerminalIO io, int x, int y) throws IOException {
		if (x != 0) {
			if (x > 0) 		io.moveRight(x);
			else				io.moveLeft(-x);
		}
		if (y != 0) {
			if (y > 0)		io.moveDown(y);
			else				io.moveUp(-y);
		}
	}
	
	public static void printFormat(TerminalIO io, String str) throws IOException {
		print(io, ShellText.toShellText(str), false);
	}
	
	public static void printlnFormat(TerminalIO io, String str) throws IOException {
		print(io, ShellText.toShellText(str), true);
	}
	
	public static void print(TerminalIO io, ShellText text) throws IOException {
		print(io, text, false);
	}
	
	public static void println(TerminalIO io, ShellText text) throws IOException {
		print(io, text, true);
	}
	
	public static void shiftNext(TerminalIO io, String str) throws CIBusException {
		try {
			io.moveLeft(StringUtil.getWordCount(str));
			io.moveDown(1);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	private static void print(TerminalIO io, ShellText text, boolean br) throws IOException {
		if (text.getText() == null)
			text.setText("");
		
		if (text.getIndent() > 0)
			io.moveRight(text.getIndent());
		
		int style = text.getFont_style();
		switch (style) {
			case 1:
			default:
				break;
			case 2:
				io.setBold(true);
				break;
			case 3:
				io.setItalic(true);
				break;
		}
		
		int mark = text.getFont_mark();
		switch (mark) {
			case 1:
			default:
				break;
			case 2:
				io.setUnderlined(true);
				break;
			case 3:
				io.setReverse(true);
				break;
		}
		
		if (br)
			io.println(text.getText());
		else
			io.write(text.getText());
		
		switch (style) {
			case 1:
			default:
				break;
			case 2:
				io.setBold(false);
				break;
			case 3:
				io.setItalic(false);
				break;
		}
		
		switch (mark) {
			case 1:
			default:
				break;
			case 2:
				io.setUnderlined(false);
				break;
			case 3:
				io.setReverse(false);
				break;
		}
	}
	
	public static Class getShellClass(String shellClassName) throws CIBusException {
		Class shellClass;
		try {
			shellClass = ProxyUtil.loadClass(shellClassName);
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
			shellClass = ProxyUtil.loadClass(shellClassName, CommonBusActivator.getClassLoader());
		}
		return shellClass;
	}
	
	public static String getStatus(String shellClassName) throws CIBusException {
		Class shellClass = getShellClass(shellClassName);
		if (shellClass.isAnnotationPresent(Shell.class))
			return ((Shell) shellClass.getAnnotation(Shell.class)).status();
		return null;
	}
	
	private static void println(TerminalIO io, String s) {
		try {
			io.println(s);
		} catch (IOException e) {
			log.error(e);
		}
	}
}

