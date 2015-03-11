// com.antelope.ci.bus.server.ssh.BusSshContext.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月11日		下午3:42:21 
 */
public class BusSshContext {
	protected ExitCallback callback;
	protected Environment env;
	
	public BusSshContext(ExitCallback callback, Environment env) {
		this.callback = callback;
		this.env = env;
	}
	
	public ExitCallback getCallback() {
		return callback;
	}
	
	public Environment getEnv() {
		return env;
	}
	
	public int getWidth() {
		return Integer.valueOf(env.getEnv().get(Environment.ENV_COLUMNS));
	}

	public int getHeigth() {
		return Integer.valueOf(env.getEnv().get(Environment.ENV_LINES));
	}
}
