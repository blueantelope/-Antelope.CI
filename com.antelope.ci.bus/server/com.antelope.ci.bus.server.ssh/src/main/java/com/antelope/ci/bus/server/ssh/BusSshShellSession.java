// com.antelope.ci.bus.server.portal.BusPortalSession.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

import com.antelope.ci.bus.server.shell.base.BusShellSession;

/**
 * 
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午2:41:05 
 */
public class BusSshShellSession extends BusShellSession {
	protected BusSshContext context;
	
	public BusSshShellSession() {
		super();
	}
	
	public BusSshShellSession(InputStream in, OutputStream out, OutputStream err) {
		super(in, out, err);
	}
	
	public BusSshShellSession(InputStream in, OutputStream out, OutputStream err, ExitCallback callback, Environment env) {
		super(in, out, err);
		context = new BusSshContext(callback, env);
	}
	
	public BusSshContext getContext() {
		return context;
	}

	public int getWidth() {
		return context.getWidth();
	}

	public int getHeigth() {
		return context.getHeigth();
	}

	@Override
	public void exit() {
		context.getCallback().onExit(0);
	}
}
