// com.antelope.ci.bus.server.shell.BusShellContainerLauncher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午6:15:07 
 */
public class BusShellContainerLauncher extends BusShellLauncher {
	private BusShellContainer container;
	
	public BusShellContainerLauncher() {
		container = new BusShellContainer(createShellSession());
	}
	
	public void addShell(List<String> shellClsList) throws CIBusException {
		container.addShell(shellClsList);
	}
	
	public void addShell(BusShell shell) throws CIBusException {
		container.addShell(shell);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShellLauncher#createShell()
	 */
	@Override
	protected BusShell createShell() throws CIBusException {
		return container.getShell(BusShellStatus.ROOT);
	}
}

