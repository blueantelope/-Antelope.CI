// com.antelope.ci.bus.server.shell.command.CommandType.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.shell.command;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 命令类型
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		上午9:44:47 
 */
public enum CommandType {
	ECHO("echo", "echo"),
	HIT("hit", "frame");
	
	private String name;
	private String shell;
	private CommandType(String name, String shell) {
		this.name = name;
		this.shell = shell;
	}
	public String getName() {
		return name;
	}
	public String getShell() {
		return shell;
	}
	
	public static CommandType fromName(String name) throws CIBusException {
		for (CommandType ct : CommandType.values()) {
			if (name.equalsIgnoreCase(ct.getName()))
				return ct;
		}
		
		throw new CIBusException("", "");
	}
	
	public static CommandType fromShell(String shell) throws CIBusException {
		for (CommandType ct : CommandType.values()) {
			if (shell.equalsIgnoreCase(ct.getShell()))
				return ct;
		}
		
		throw new CIBusException("", "");
	}
}

