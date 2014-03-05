// com.antelope.ci.bus.server.shell.BusShellContainerLauncher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.util.HashMap;
import java.util.Map;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 调试并运行shell容器
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午6:15:07 
 */
public class BusShellContainerLauncher extends BusShellLauncher {
	private BusShellContainer container;
	
	public BusShellContainerLauncher() {
		container = new BusShellContainer();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShellLauncher#createShell()
	 */
	@Override
	protected BusShell createShell() throws CIBusException {
		container.addShell(getShellList());
		BusShellSession session = createShellSession();
		BusShell startShell = null;
		Map<String, BusShell> shellMap = new HashMap<String, BusShell>();
		for (String status : container.getShellClassMap().keySet()) {
			BusShell shell = container.createShell(status);
			shell.attatchSession(session);
			shellMap.put(status, shell);
			if (shell.getSort() == -1) {
				if (startShell == null || status.equals(BusShellStatus.ROOT))
					startShell =shell;
			} else {
				if (shell.getSort() < startShell.getSort())
					startShell =shell;
			}
			
		}
		
		if (shellMap.size() > 1) {
			for (String status : shellMap.keySet()) {
				BusShell shell = shellMap.get(status);
				shell.setShellMap(shellMap);
			}
		}
		return startShell;
	}
}

