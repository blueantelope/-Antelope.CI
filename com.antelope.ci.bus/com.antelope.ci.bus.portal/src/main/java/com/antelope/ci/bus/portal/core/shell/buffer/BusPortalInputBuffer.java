// com.antelope.ci.bus.portal.core.shell.BusPortalInputBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.buffer;

import com.antelope.ci.bus.server.shell.buffer.BusInputBuffer;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月10日		下午4:20:59 
 */
public class BusPortalInputBuffer extends BusInputBuffer {
	private String name;

	public BusPortalInputBuffer(TerminalIO io, int x, int y, int width, int height, String name) {
		super(io, x, y, width, height);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

