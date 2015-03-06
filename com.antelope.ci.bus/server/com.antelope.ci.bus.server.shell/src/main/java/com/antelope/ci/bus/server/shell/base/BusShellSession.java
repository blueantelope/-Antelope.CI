// com.antelope.ci.bus.server.portal.BusPortalSession.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.base;

import java.io.InputStream;
import java.io.OutputStream;

import com.antelope.ci.bus.server.common.BusSession;
import com.antelope.ci.bus.server.shell.util.ConnectionData;
import com.antelope.ci.bus.server.shell.util.TerminalIO;


/**
 * 
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午2:41:05 
 */
public abstract class BusShellSession extends BusSession {
	protected TerminalIO io;
	protected ConnectionData setting;
	
	public BusShellSession() {
		super();
	}
	
	public BusShellSession(InputStream in, OutputStream out, OutputStream err) {
		super(in, out, err);
		init();
	}
	
	private void init() {
		setting = new ConnectionData();
		setting.setNegotiatedTerminalType("vt100");
		if (in != null && out != null)
			io = new TerminalIO(in, out, setting);
	}
	
	// getter and setter
	public TerminalIO getIo() {
		return io;
	}
	public void setIo(TerminalIO io) {
		this.io = io;
	}
	public ConnectionData getSetting() {
		return setting;
	}
	public void setSetting(ConnectionData setting) {
		this.setting = setting;
	}

	public abstract int getWidth();

	public abstract int getHeigth();
}
