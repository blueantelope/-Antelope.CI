// com.antelope.ci.bus.server.shell.buffer.BusEditBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-10		上午11:41:54 
 */
public class BusEditBuffer extends BusScreenBuffer {
	protected int lines;
	
	public BusEditBuffer(TerminalIO io, ShellCursor cursorStart, ShellScreen screen) {
		super(io, cursorStart, screen, 81920);
		init(cursorStart, screen);
	}
	
	public BusEditBuffer(TerminalIO io, ShellCursor cursorStart, ShellScreen screen, int bufSize) {
		super(io, cursorStart, screen, bufSize);
		init(cursorStart, screen);
	}

	public BusEditBuffer(TerminalIO io, int cursorX, int cursorY, int width, int height) {
		super(io, cursorX, cursorY, width, height, 81920);
		init(cursorStart, screen);
	}
	
	public BusEditBuffer(TerminalIO io, int cursorX, int cursorY, int width, int height, int bufSize) {
		super(io, cursorX, cursorY, width, height, 81920);
		init(cursorStart, screen);
	}
	
	protected void init(ShellCursor cursorStart, ShellScreen screen) {
		super.init(cursorStart, screen);
		lines = 0;
	}
	
	public void reset() {
		buffer.clear();
		this.cursor = cursorStart;
	}
	
	@Override public void put(char c) throws CIBusException {
		try {
			buffer.put(c);
			if (x() < width()) {
				io.moveLeft(1);
			} else {
				io.moveDown(1);
				io.moveRight(width());
				cursor.newLine();
			}
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}

	public boolean left() {
		boolean moved = false;
		if (position() >= 0) {
			moved = super.left();
		}
		
		return moved;
	}

	public boolean right() {
		boolean moved = false;
		if (position() < size()) {
			moved = super.right();
		}
		
		return moved;
	}

	// 向右删除1个字符
	public boolean delete() {
		boolean op = false;
		if (!empty() && position() < size()) {
			deleteFromBuffer(position());
			left(1);
			op = true;
		}
		
		return op;
	}
	
	@Override
	public boolean backspace() {
		boolean op = false;
		if (!empty() && position() > 0) {
			deleteFromBuffer(position());
			right(1);
			op = true;
		}
		
		return op;
	}

	@Override public void tab() {
		if (position() < size()) {
			super.tab();
		}
	}

	@Override public void space() {
		try {
			put((char) NetVTKey.SPACE);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusScreenBuffer#down()
	 */
	@Override
	public boolean down() {
		boolean moved = super.down();
		if (moved && y() == height() -1) {
			try {
				int steps = width() - x();
				io.moveLeft(steps);
				cursor.right(steps);
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return moved;
	}
	
	@Override
	public ShellCommandArg enter() {
		try {
			put((char) NetVTKey.ENTER);
			lines++;
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
			new CIBusException("", e);
		}
		return null;
	}
	
	protected boolean empty() {
		if (buffer.position() == 0)
			return true;
		return false;
	}
	
	protected void deleteFromBuffer(int position) {
		char[] dst = new char[buffer.position() - position - 1];
		buffer.get(dst, position+1, buffer.position());
		buffer.position(position -1);
		buffer.put(dst);
	}
	
	protected int position() {
		return lines * y() + x();
	}
	
	protected int size() {
		return buffer.position();
	}
	
	protected String[] toLine() {
		String s = read();
		List<String> lineList = new ArrayList<String>();
		CharBuffer cb = CharBuffer.allocate(1024);
		for (char c : s.toCharArray()) {
			if (c == NetVTKey.ENTER) {
				lineList.add(read(cb));
				cb.clear();
			}
			cb.put(c);
		}
		return lineList.toArray(new String[lineList.size()]);
	}
	
	private String read(CharBuffer cb) {
		int mark = cb.position();
		cb.flip();
		String s = cb.toString();
		cb.position(mark);
		cb.limit(buffer.capacity());
		return s;
	}

	@Override
	public void tabTip() {
		// nothing to do
	}

	@Override
	public boolean exitSpace() {
		return false;
	}

	@Override
	public void printTips(List<String> cmdList, int width) {
		// nothing to do
	}
}

