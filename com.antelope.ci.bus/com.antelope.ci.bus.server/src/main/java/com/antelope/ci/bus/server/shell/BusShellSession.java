// com.antelope.ci.bus.server.portal.BusPortalSession.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午2:41:05 
 */
public class BusShellSession {
	private InputStream in;
	private OutputStream out;
	private OutputStream err;
	private ExitCallback callback;
	private Environment env;
	
	public BusShellSession() {
		
	}
	
	public BusShellSession(InputStream in, OutputStream out, OutputStream err) {
		super();
		this.in = in;
		this.out = out;
		this.err = err;
	}
	
	public BusShellSession(InputStream in, OutputStream out, OutputStream err, ExitCallback callback, Environment env) {
		super();
		this.in = in;
		this.out = out;
		this.err = err;
		this.callback = callback;
		this.env = env;
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
	public ExitCallback getCallback() {
		return callback;
	}
	public void setCallback(ExitCallback callback) {
		this.callback = callback;
	}
	public Environment getEnv() {
		return env;
	}
	public void setEnv(Environment env) {
		this.env = env;
	}
	
	public int getWidth() {
		return Integer.valueOf(env.getEnv().get(Environment.ENV_COLUMNS));
	}

	public int getHeigth() {
		return Integer.valueOf(env.getEnv().get(Environment.ENV_LINES));
	}
}

