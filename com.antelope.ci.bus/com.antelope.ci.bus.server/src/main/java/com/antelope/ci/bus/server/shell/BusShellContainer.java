// com.antelope.ci.bus.server.shell.BusShellContainer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;



/**
 * shell容器，支持多个shell切换
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		上午9:31:43 
 */
public class BusShellContainer {
	protected BusShellSession session;
	protected Map<String, BusShell> shellMap;
	
	public BusShellContainer(BusShellSession session) {
		this.session = session;
		shellMap = new HashMap<String, BusShell>();
	}

	public BusShellContainer(BusShellSession session, List<String> shellClsList) throws CIBusException {
		this.session = session;
		shellMap = new HashMap<String, BusShell>();
		addShell(shellClsList);
	}
	
	public void addShell(List<String> shellClsList) throws CIBusException {
		for (String shellCls : shellClsList) {
			try {
				BusShell shell = (BusShell) ProxyUtil.newObject(shellCls);
				shell.attatchSession(session);
				addShell(shell);
			} catch (Exception e) {
				throw new CIBusException("", e);
			}
		}
	}
	
	public void addShell(BusShell shell) throws CIBusException {
		String status = shell.getStatus();
		if (shellMap.get(status) != null)
			throw new CIBusException("", "");
		shell.attatchContaint(this);
		shellMap.put(shell.getStatus(), shell);
	}

	public BusShell getShell(String status) {
		return shellMap.get(status);
	}

	public Map<String, BusShell> getShellMap() {
		return shellMap;
	}
}

