// com.antelope.ci.bus.server.shell.BusBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.util.TerminalIO;


/**
 * 
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-10		上午9:52:25 
 */
public abstract class BusBuffer {
	private static final int BUF_SIZE = 512;
	protected int bufSize;
	protected int tabSize;
	protected CharBuffer buffer;
	protected TerminalIO io;
	protected boolean inTip;
	
	public BusBuffer(TerminalIO io) {
		this(io, BUF_SIZE);
	}
	
	public BusBuffer(TerminalIO io, int bufSize) {
		this.io = io;
		this.bufSize = bufSize;
		buffer = CharBuffer.allocate(bufSize);
		tabSize = 4;
	}
	
	public void setTabSize(int tabSize) {
		this.tabSize = tabSize;
	}
	
	public void reset() {
		buffer.clear();
	}
	
	public String read() {
		int mark = buffer.position();
		buffer.flip();
		String s = buffer.toString();
		buffer.position(mark);
		buffer.limit(buffer.capacity());
		return s;
	}
	
	public String read(int start, int end) {
		int position = buffer.position();
		int limit = buffer.limit();
		buffer.position(start);
		buffer.limit(end);
		String s = buffer.toString();
		buffer.position(position);
		buffer.limit(limit);
		return s;
	}
	
	public void remove(int index, int length) {
		char[] puts =  read(index+length);
		buffer.position(index);
		buffer.put(puts);
	}
	
	public void put(char c) throws CIBusException {
		buffer.put(c);
		try {
			io.write((char) c);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}

	public void left(int times) {
		int n = 0;
		while (n < times) {
			if (!left())
				break;
			n++;
		}
	}

	public void right(int times) {
		int n = 0;
		while (n < times) {
			if (!right())
				break;
			n++;
		}
	}

	public void up(int times) {
		int n = 0;
		while (n < times) {
			if (!up())
				break;
			n++;
		}
	}

	public void down(int times) {
		int n = 0;
		while (n < times) {
			if (!down())
				break;
			n++;
		}
	}

	// 向右删除多个字符
	public void delete(int times) throws CIBusException {
		int n = 0;
		while (n < times) {
			if (!delete())
				break;
			n++;
		}
	}
		
	// 向左删除多个字符
	public void backspace(int times) throws CIBusException {
		int n = 0;
		while (n < times) {
			if (!backspace())
				break;
			n++;
		}
	}
	
	public ShellCommandArg enter() {
		try {
			io.write((char) NetVTKey.LF);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
		try {
			return toCommand();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		
		return null;
	}

	public ShellCommandArg toCommand() throws CIBusException {
		buffer.flip();
		String line = buffer.toString();
		String[] strs = line.split(" ");
		String command = "";
		String[] args = new String[0];
		if (strs.length > 0) {
			command = strs[0];
			args = new String[strs.length-1];
			int n = 0;
			while (n < args.length) {
				args[n] = strs[++n];
			}
		}
		return new ShellCommandArg(command, args);
	}
	
	public boolean inCmdTab() {
		return inTip;
	}
	
	protected boolean empty() {
		if (buffer.position() == 0)
			return true;
		return false;
	}
	
	protected void putTab(int index) {
		int n = 0;
		while (n < tabSize) {
			buffer.put(index, ' ');
			n++;
			index++;
		}
	}
	
	protected void putTab() {
		char[] spaces = new char[tabSize];
		int n = 0;
		while (n < tabSize)
			spaces[n++] = ' ';
		buffer.put(spaces);
	}
	
	protected void read(int index, char[] dst) {
		int current = buffer.position();
		buffer.position(index);
		buffer.get(dst);
		buffer.position(current);
	}
	
	protected char[] read(int index) {
		int current = buffer.position();
		char[] chs = new char[current - index];
		buffer.position(index);
		buffer.get(chs);
		buffer.position(current);
		return chs;
	}
	
	@Override public String toString() {
		return read();
	}
	
	// 向左删除一个字符
	public abstract boolean backspace() throws CIBusException;
	
	// 向右删除1个字符
	public abstract boolean delete() throws CIBusException;
	
	public abstract boolean left();
	
	public abstract boolean right();
	
	public abstract boolean down();
	
	public abstract boolean up();
	
	public abstract void tab();
	
	public abstract void space();
	
	public abstract void tabTip();
	
	public abstract void printTips(List<String> cmdList, int width);
	
	public abstract boolean exitSpace();
}

