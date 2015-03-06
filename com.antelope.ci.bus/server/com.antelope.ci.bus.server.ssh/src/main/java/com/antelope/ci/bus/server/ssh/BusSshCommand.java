// com.antelope.ci.bus.server.ssh.BusSshCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.launcher.BusShellLauncher;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月16日		上午11:28:11 
 */
public class BusSshCommand implements Command {
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;
	protected ExitCallback callback;
	protected Environment env;
	protected BusShellLauncher shellLauncher;
	
	public BusSshCommand(BusShellLauncher shellLauncher) {
		super();
		this.shellLauncher = shellLauncher;
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
		this.env = env;
		new Thread() {
			public void run() {
				BusShell shell = null;
				try {
					shell = shellLauncher.createShell(createShellSession());
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
	
	protected BusSshShellSession createShellSession() {
		return new BusSshShellSession(in, out, err, callback, env);
	}
}