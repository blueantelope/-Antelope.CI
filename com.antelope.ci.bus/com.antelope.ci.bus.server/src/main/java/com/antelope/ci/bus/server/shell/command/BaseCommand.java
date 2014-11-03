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
 * s
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-13		下午6:33:41 
 */
public abstract class BaseCommand implements ICommand {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.ICommand#getIdentity()
	 */
	@Override public String getIdentity() {
		if (this.getClass().isAnnotationPresent(Command.class)) {
			Command cmd = this.getClass().getAnnotation(Command.class);
			return cmd.status() + "." + cmd.mode() + "." + cmd.name();
		}
		return "";
	}
	
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
	 * @see com.antelope.ci.bus.server.shell.command.ICommand#execute(boolean, com.antelope.ci.bus.server.shell.BusShell, java.lang.Object[])
	 */
	@Override
	public String execute(boolean refresh, BusShell shell, Object... args) {
		if (this.getClass().isAnnotationPresent(Command.class)) {
			Command command = this.getClass().getAnnotation(Command.class);
			if (refresh && command.beforeClear()) {
				try {
					ShellUtil.clear(shell.getIO());
				} catch (CIBusException e) {
					DevAssistant.errorln(e);
				}
			}
		}
		
		return execute(shell, args);
	}
	
	protected abstract String execute(BusShell shell, Object... args);
}

