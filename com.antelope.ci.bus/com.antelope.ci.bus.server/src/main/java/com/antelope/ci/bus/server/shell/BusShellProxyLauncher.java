// com.antelope.ci.bus.server.shell.BusShellProxyLauncher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.util.List;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-4		上午9:42:48 
 */
public class BusShellProxyLauncher extends BusShellLauncher {
	private String shellClass;
	
	public BusShellProxyLauncher() {
		
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShellLauncher#addShell(java.util.List)
	 */
	@Override
	public void addShell(List<String> shellClsList) throws CIBusException {
		this.shellClass = shellClsList.get(0);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShellLauncher#createShell()
	 */
	@Override
	protected BusShell createShell() throws CIBusException {
		BusShell shell = (BusShell) ProxyUtil.newObject(shellClass);
		shell.attatchSession(createShellSession());
		return shell;
	}
}

