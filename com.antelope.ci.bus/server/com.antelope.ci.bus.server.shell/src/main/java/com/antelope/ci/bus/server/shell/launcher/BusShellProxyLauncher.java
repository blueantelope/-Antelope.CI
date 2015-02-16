// com.antelope.ci.bus.server.shell.BusShellProxyLauncher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.launcher;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.base.BusShellSession;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-4		上午9:42:48 
 */
public class BusShellProxyLauncher extends BusShellLauncher {
	public static final String clazz = "com.antelope.ci.bus.shell.launcher.BusShellProxyLauncher";
	
	public BusShellProxyLauncher() {
		super();
	}
	
	public BusShellProxyLauncher(BusShellCondition condition) {
		super(condition);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.launcher.BusShellLauncher#createShell(com.antelope.ci.bus.server.shell.base.BusShellSession)
	 */
	@Override
	public BusShell createShell(BusShellSession session) throws CIBusException {
		if (getShellList().isEmpty())
			return null;
		String shellClass = getShellList().get(0);
		BusShell shell = (BusShell) ProxyUtil.newObject(shellClass);
		if (shell == null)
			shell = (BusShell) ProxyUtil.newObject(shellClass, condition.getClassLoader());
		shell.attatchSession(session);
		return shell;
	}
}

