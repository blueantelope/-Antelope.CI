// com.antelope.ci.bus.server.shell.buffer.SimpleBusInputBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer;

import com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION;
import com.antelope.ci.bus.server.shell.util.TerminalIO;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月21日		下午3:38:41 
 */
public class SimpleBusShellInputBuffer extends BusShellInputBuffer {

	public SimpleBusShellInputBuffer(TerminalIO io, int x, int y, int width, int height) {
		super(io, x, y, width, height);
	}
	
	@Override
	protected void rewriteAhead(int x, int y) {
		
	}

	@Override
	protected void rewriteLatter(int x, int y) {
		
	}

	@Override
	protected void userDown(DIRECTION direction) {
		
	}

	@Override
	protected void userUp(DIRECTION direction) {
		
	}

	@Override
	protected void userLeft(DIRECTION direction) {
		
	}

	@Override
	protected void userRight(DIRECTION direction) {
		
	}
}