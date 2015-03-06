// com.antelope.ci.bus.server.common.BusSession.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.common;

import java.io.InputStream;
import java.io.OutputStream;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午4:40:16 
 */
public abstract class BusSession {
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;
	
	public BusSession() {
		super();
	}
	
	public BusSession(InputStream in, OutputStream out, OutputStream err) {
		super();
		this.in = in;
		this.out = out;
		this.err = err;
	}
	
	// getter and setter
	public InputStream getIn() {
		return in;
	}
	public void setIn(InputStream in) {
		this.in = in;
	}
	public OutputStream getOut() {
		return out;
	}
	public void setOut(OutputStream out) {
		this.out = out;
	}
	public OutputStream getErr() {
		return err;
	}
	public void setErr(OutputStream err) {
		this.err = err;
	}
	
	public abstract void exit();
}
