// com.antelope.ci.bus.server.shell.command.BaseCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.ShellUtil;

/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-13		下午6:33:41 
 */
public abstract class BaseCommand implements ICommand {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.ICommand#getContent()
	 */
	@Override public Command getContent() {
		if (this.getClass().isAnnotationPresent(Command.class))
			return this.getClass().getAnnotation(Command.class);
		
		return null;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.ICommand#execute(boolean, com.antelope.ci.bus.server.shell.BusShell, com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.String, java.lang.Object[])
	 */
	@Override
	public String execute(boolean refresh, BusShell shell, String status, Object... args) {
		if (this.getClass().isAnnotationPresent(Command.class)) {
			Command command = this.getClass().getAnnotation(Command.class);
			if (refresh && command.beforeClear()) {
				try {
					ShellUtil.clear(shell.getIo());
				} catch (CIBusException e) {
					DevAssistant.errorln(e);
				}
			}
		}
		
		return execute(shell, status, args);
	}
	
	protected abstract String execute(BusShell shell, String status, Object... args);
}

