// com.antelope.ci.bus.server.shell.BusFrameBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.shell.buffer;

import java.io.IOException;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.core.TerminalIO;

/**
 * TODO 描述
 * @author blueantelope
 * @version 0.1
 * @Date 2013-12-9 下午5:39:41
 */
public class BusHitBuffer extends BusScreenBuffer {
	private ShellCursor cursor;
	private ShellCursor cursorStart;
	private ShellScreen screen;

	public BusHitBuffer(TerminalIO io, ShellCursor cursorStart, ShellScreen screen) {
		super(io, cursorStart, screen);
	}

	public BusHitBuffer(TerminalIO io, int cursorX, int cursorY, int width, int height) {
		super(io, cursorX, cursorY, width, height);
	}

	public void reset() {
		super.reset();
		int x = cursor.getX() - cursorStart.getX();
		int y = cursor.getY() - cursorStart.getY();
		try {
			if (x > 0) {
				io.moveDown(x);
			} else {
				io.moveUp(-x);
			}
			if (y > 0) {
				io.moveLeft(y);
			} else {
				io.moveRight(-y);
			}
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
		cursor = cursorStart;
	}

	public void put(char c) throws IOException {
		buffer.put(c);
	}

	
	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean backspace() {
		return false;
	}

	@Override
	public void space() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ShellCommandArg enter() throws CIBusException {
		return super.toCommand();
	}
}
