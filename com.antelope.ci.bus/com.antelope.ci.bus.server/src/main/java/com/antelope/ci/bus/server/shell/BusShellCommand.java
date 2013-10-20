// com.antelope.ci.bus.server.shell.BusShellCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-17		下午10:22:10 
 */
public abstract class BusShellCommand implements Command {
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;
	protected ExitCallback callback;
	
	public BusShellCommand() {
		
	}

	@Override
	public void setInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	@Override
	public void setErrorStream(OutputStream err) {
		this.err = err;
	}

	@Override
	public void setExitCallback(ExitCallback callback) {
		this.callback = callback;
	}

	@Override
	public void start(Environment env) throws IOException {
		new Thread() {
			public void run() {
				BusShell shell = null;
				try {
					shell = initShell();
					shell.open();
				} catch (CIBusException e) {
					if (shell != null) {
						try {
							shell.close();
						} catch (CIBusException ce) {
						
						}
					} 
				}
			}
		}.start();
	}

	@Override
	public void destroy() {
		callback.onExit(0);
	}
	
	protected BusShellSession createShellSession() {
		BusShellSession shellSession = new BusShellSession(in, out, err);
		return shellSession;
	}
	
	protected abstract BusShell initShell() throws CIBusException;
}

