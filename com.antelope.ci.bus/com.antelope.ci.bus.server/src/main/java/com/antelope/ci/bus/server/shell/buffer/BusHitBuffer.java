// com.antelope.ci.bus.server.shell.BusFrameBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.shell.buffer;

import java.io.IOException;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.core.TerminalIO;

/**
 * TODO 描述
 * @author blueantelope
 * @version 0.1
 * @Date 2013-12-9 下午5:39:41
 */
public class BusHitBuffer extends BusBuffer {
	
	public BusHitBuffer(TerminalIO io) {
		super(io, 64);
	}
	
	public void reset() {
		super.reset();
	}

	@Override public void put(char c) {
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

	@Override public void space() {
		// nothing to do
	}
	
	@Override public ShellCommandArg enter() {
		try {
			return super.toCommand();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		
		return null;
	}

	@Override public void tabTip() {
		// nothing to do
	}
	
	@Override public boolean exitSpace() {
		return false;
	}
	
	@Override public void printTips(List<String> cmdList, int width) {
		// nothing to do
	}


	@Override public boolean left() {
		return false;
	}


	@Override
	public boolean right() {
		return false;
	}

	@Override public boolean down() {
		return false;
	}

	@Override public boolean up() {
		return false;
	}

	@Override public void tab() {
		
	}
}
