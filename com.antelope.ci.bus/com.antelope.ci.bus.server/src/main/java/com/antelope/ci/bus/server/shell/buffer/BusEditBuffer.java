// com.antelope.ci.bus.server.shell.buffer.BusEditBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer;

import com.antelope.ci.bus.server.shell.util.TerminalIO;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月15日		上午10:12:01 
 */
public abstract class BusEditBuffer extends BusAreaBuffer {
	private static final int BUFFER_SIZE = 10240;

	public BusEditBuffer(TerminalIO io, int x, int y, int width, int height) {
		super(io, x, y, width, height, BUFFER_SIZE);
	}
}

