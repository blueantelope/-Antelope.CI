// com.antelope.ci.bus.server.shell.BusShellconditionLauncher.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.launcher;

import java.util.HashMap;
import java.util.Map;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.base.BusShellSession;
import com.antelope.ci.bus.server.shell.base.BusShellStatus;


/**
 * shell运行容器
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午6:15:07 
 */
public class BusShellContainerLauncher extends BusShellLauncher {
	public static final String clazz = "com.antelope.ci.bus.shell.launcher.BusShellconditionLauncher";
	
	public BusShellContainerLauncher() {
		super();
	}
	
	public BusShellContainerLauncher(BusShellCondition condition) {
		super(condition);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.launcher.BusShellLauncher#createShell(com.antelope.ci.bus.server.shell.base.BusShellSession)
	 */
	@Override
	public BusShell createShell(BusShellSession session) throws CIBusException {
		BusMultiShellCondition multiCondition = (BusMultiShellCondition) condition;
		multiCondition.addShell(getShellList());
		BusShell startShell = null;
		Map<String, BusShell> shellMap = new HashMap<String, BusShell>();
		Map<String, String> scmap = condition.getShellClassMap();
		for (String status : scmap.keySet()) {
			BusShell shell = multiCondition.createShell(status);
			shell.attatchSession(session);
			shellMap.put(status, shell);
			
			if (startShell == null) {
				startShell =shell;
				continue;
			}
			if (shell.getSort() == -1 && status.equals(BusShellStatus.ROOT)) {
				startShell =shell;
				continue;
			} 
			if (shell.getSort() < startShell.getSort())
				startShell =shell;
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
