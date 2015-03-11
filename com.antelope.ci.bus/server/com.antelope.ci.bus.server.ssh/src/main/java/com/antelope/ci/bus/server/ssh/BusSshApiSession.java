// com.antelope.ci.bus.server.ssh.BusSshApiSession.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

import com.antelope.ci.bus.server.api.base.BusApiSession;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月11日		下午3:47:13 
 */
public class BusSshApiSession extends BusApiSession {
	protected BusSshContext context;
	
	public BusSshApiSession() {
		super();
	}
	
	public BusSshApiSession(InputStream in, OutputStream out, OutputStream err) {
		super(in, out, err);
	}
	
	public BusSshApiSession(InputStream in, OutputStream out, OutputStream err, ExitCallback callback, Environment env) {
		super(in, out, err);
		context = new BusSshContext(callback, env);
	}
	
	public BusSshContext getContext() {
		return context;
	}
	
	@Override
	public void exit() {
		context.getCallback().onExit(0);
	}
}

