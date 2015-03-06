// com.antelope.ci.bus.shell.BusShellCreator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.base.BusShellSession;
import com.antelope.ci.bus.server.shell.launcher.BusShellCondition;
import com.antelope.ci.bus.server.shell.launcher.BusShellContainerLauncher;
import com.antelope.ci.bus.server.shell.launcher.BusShellLauncher;
import com.antelope.ci.bus.server.shell.launcher.BusShellLauncherType.BaseShellLauncherType;
import com.antelope.ci.bus.server.shell.launcher.BusShellProxyLauncher;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月15日		下午5:09:26 
 */
public class BusShellManager {
	protected Map<String, BusShellLauncher> launcherMap;
	
	public BusShellManager() {
		launcherMap = new ConcurrentHashMap<String, BusShellLauncher>();
		launcherMap.put(BaseShellLauncherType.CONTAINER.name(), new BusShellContainerLauncher());
		launcherMap.put(BaseShellLauncherType.PROXY.name(), new BusShellProxyLauncher());
	}
	
	public BusShellManager(BusShellCondition condition) {
		launcherMap = new ConcurrentHashMap<String, BusShellLauncher>();
		launcherMap.put(BaseShellLauncherType.CONTAINER.name(), new BusShellContainerLauncher(condition));
		launcherMap.put(BaseShellLauncherType.PROXY.name(), new BusShellProxyLauncher(condition));
	}
	
	
	public BusShellLauncher getContainerLauncher() {
		return getShellLauncher(BaseShellLauncherType.CONTAINER.name());
	}
	
	public BusShellLauncher getProxyLauncher() {
		return getShellLauncher(BaseShellLauncherType.PROXY.name());
	}
	
	public BusShellLauncher getShellLauncher(String name) {
		return launcherMap.get(name);
	}
	
	public BusShell createShell(BusShellLauncher shellLauncher, BusShellSession session) throws CIBusException {
		return (BusShell) shellLauncher.launch(session);
	}
}
