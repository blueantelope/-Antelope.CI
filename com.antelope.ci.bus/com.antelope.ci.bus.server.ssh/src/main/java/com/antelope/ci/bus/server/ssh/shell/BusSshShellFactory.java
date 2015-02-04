// com.antelope.ci.bus.server.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh.shell;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午12:43:31 
 */
public class BusSshShellFactory implements Factory<Command> {
	private Class command_class;
	private BusSshShellLauncher shellLauncher;
	
	public BusSshShellFactory(String classname) throws CIBusException {
		this();
		try {
			Class clazz = Class.forName(classname);
			setCommandClass(command_class);
		} catch (ClassNotFoundException e) {
			throw new CIBusException("",  e);
		}
	
	}
	
	public BusSshShellFactory(Class command_class) throws CIBusException {
		this();
		setCommandClass(command_class);
	}
	
	public BusSshShellFactory(BusSshShellLauncher shellLauncher) {
		this();
		this.shellLauncher = shellLauncher;
	}
	
	public BusSshShellFactory() {
		this.command_class = null;
		this.shellLauncher = null;
	}
	
	private void setCommandClass(Class command_class) throws CIBusException {
		if (!BusSshShellLauncher.class.isAssignableFrom(command_class))
			throw new CIBusException("", "Shell Command Define Error");
		this.command_class = command_class;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.apache.sshd.common.Factory#create()
	 */
	@Override
	public Command create() {
		if (shellLauncher != null)
			return shellLauncher;
		return createShellLauncher();
    }
	
	private Command createShellLauncher() {
		Command command = null;
		if (command_class != null) {
			try {
				command = (Command) command_class.newInstance();
			} catch (Exception e) {
				DevAssistant.errorln(e);
			} finally {
				return command;
			}
		}
		
		return command;
	}
}

