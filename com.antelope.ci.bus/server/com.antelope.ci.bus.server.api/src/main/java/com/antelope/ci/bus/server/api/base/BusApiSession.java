// com.antelope.ci.bus.server.api.base.BusAPISession.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.base;

import java.io.InputStream;
import java.io.OutputStream;

import com.antelope.ci.bus.server.common.BusSession;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月11日		上午11:26:37 
 */
public abstract class BusApiSession extends BusSession {
	protected ApiIO io;
	
	public BusApiSession() {
		super();
	}
	
	public BusApiSession(InputStream in, OutputStream out, OutputStream err) {
		super(in, out, err);
		initIo();
	}
	
	public void initIo() {
		if (in != null && out != null)
			io = new ApiIO(in, out);
	}
	
	public ApiIO getIo() {
		return io;
	}
}
