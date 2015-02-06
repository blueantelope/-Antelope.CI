// com.antelope.ci.bus.portal.core.shell.form.CommonFormAction.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.form;

import java.util.HashMap;
import java.util.Map;

import com.antelope.ci.bus.engine.manager.project.EngineProjectManager;
import com.antelope.ci.bus.engine.manager.project.EngineProjectManagerConstants;
import com.antelope.ci.bus.osgi.BusActivator;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.server.service.UserStoreServerService;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月18日		下午5:07:59 
 */
public abstract class CommonFormAction {
	protected BusPortalShell shell;
	protected Object[] args;
	protected Map<String, Object> arguments = new HashMap<String, Object>();
	
	public void setShell(BusPortalShell shell) {
		this.shell = shell;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	public void putArgument(String name, Object argument) {
		arguments.put(name, argument);
	}
	
	protected Object getArgument(String name) {
		if (arguments.containsKey(name))
			return (BusPortalShell) arguments.get(name);
		return null;
	}
	
	protected EngineProjectManager getProjectManager() {
		return (EngineProjectManager) BusActivator.getUsingService(EngineProjectManagerConstants.SERVICE_NAME);
	}
}