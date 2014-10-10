// com.antelope.ci.bus.server.shell.buffer.BusScreenBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer;

import java.io.IOException;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-10		下午3:34:01 
 */
public abstract class BusScreenBuffer extends BusBuffer {
	protected ShellCursor cursor;
	protected ShellCursor cursorStart;
	protected ShellScreen screen;

	public BusScreenBuffer(TerminalIO io, ShellCursor cursorStart, ShellScreen screen) {
		super(io);
		init(cursorStart, screen);
	}

	public BusScreenBuffer(TerminalIO io, int cursorX, int cursorY, int width, int height) {
		super(io);
		this.cursorStart = new ShellCursor(cursorX, cursorY);
		this.screen = new ShellScreen(width, height);
		init(cursorStart, screen);
	}
	
	public BusScreenBuffer(TerminalIO io, ShellCursor cursorStart, ShellScreen screen, int bufSize) {
		super(io, bufSize);
		init(cursorStart, screen);
	}

	public BusScreenBuffer(TerminalIO io, int cursorX, int cursorY, int width, int height, int bufSize) {
		super(io, bufSize);
		this.cursorStart = new ShellCursor(cursorX, cursorY);
		this.screen = new ShellScreen(width, height);
		init(cursorStart, screen);
	}
	
	protected void init(ShellCursor cursorStart, ShellScreen screen) {
		this.cursorStart = cursorStart;
		this.screen = screen;
		this.cursor = new ShellCursor(cursorStart.getX(), cursorStart.getY());
	}
	
	public void setTabSize(int tabSize) {
		this.tabSize = tabSize;
	}
	
	public boolean left() {
		boolean moved = false;
		try {
			if (x() > cursorStart.getX()) {
				io.moveLeft(1);
				cursor.left(1);
				moved = true;
			} else {
				if (y() > cursorStart.getY()) {
					io.moveUp(1);
					io.moveRight(width());
					cursor.lastEndline(width());
					moved = true;
				}
			}
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
		
		return moved;
	}

	public boolean right() {
		boolean moved = false;
		try {
			if (x() < width()) {
				io.moveRight(1);
				cursor.right(1);
				moved = true;
			} else {
				io.moveDown(1);
				io.moveLeft(width());
				cursor.newLine();
				moved = true;
			}
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
		
		return moved;
	}

	public boolean up() {
		boolean moved = false;
		if (y() > cursorStart.getY()) {
			try {
				io.moveUp(1);
				cursor.up(1);
				moved = true;
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return moved;
	}

	public boolean down() {
		boolean moved = false;
		if (y() < height()) {
			try {
				io.moveDown(1);
				cursor.down(1);
				moved = true;
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return moved;
	}
	
	@Override public void tab() {
		try {
			int steps = width() - x() > tabSize ? tabSize : width() - x();
			io.moveRight(steps);
			cursor.right(steps);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected int x() {
		return cursor.getX();
	}
	
	protected int y() {
		return cursor.getY();
	}
	
	protected int width() {
		return screen.getWidth();
	}
	
	protected int height() {
		return screen.getHeight();
	}
}

