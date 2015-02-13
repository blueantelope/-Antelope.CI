// com.antelope.ci.bus.server.shell.BusShellProxyLauncher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh.shell;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusShell;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-4		上午9:42:48 
 */
public class BusSshShellProxyLauncher extends BusSshShellLauncher {
	public BusSshShellProxyLauncher() {
		super();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShellLauncher#createShell()
	 */
	@Override
	public BusShell createShell() throws CIBusException {
		if (getShellList().isEmpty())
			return null;
		String shellClass = getShellList().get(0);
		BusShell shell = (BusShell) ProxyUtil.newObject(shellClass);
		if (shell == null)
			shell = (BusShell) ProxyUtil.newObject(shellClass, condition.getLauncher_classloader());
		shell.attatchSession(createShellSession());
		return shell;
	}
}

