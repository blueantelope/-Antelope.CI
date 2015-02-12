// com.antelope.ci.bus.server.shell.buffer.CommandArg.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.shell.buffer;

import com.antelope.ci.bus.common.StringUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-10		上午11:23:29 
 */
public class ShellCommandArg {
	private String command;
	private String[] args;
	
	public ShellCommandArg(String command, String[] args) {
		super();
		this.command = command;
		this.args = args;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String[] getArgs() {
		return args;
	}
	
	public boolean exist() {
		return !StringUtil.empty(command);
	}
}

