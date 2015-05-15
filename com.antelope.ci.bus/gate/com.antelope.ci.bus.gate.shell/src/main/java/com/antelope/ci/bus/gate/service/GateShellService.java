// com.antelope.ci.bus.gate.service.ShellService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.service;

import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.gate.shell.BusGateShell;
import com.antelope.ci.bus.osgi.BusContext;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.osgi.IService;
import com.antelope.ci.bus.osgi.Service;
import com.antelope.ci.bus.server.shell.BusShellManager;
import com.antelope.ci.bus.server.shell.launcher.BusShellCondition;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		上午10:12:14 
 */
@Service(name=GateShellService.NAME)
public class GateShellService implements IService {
	public static final String NAME = "com.antelope.ci.bus.gate.service.GateShellService";
	protected BusShellManager manager;
	
	public GateShellService() {
		super();
	}
	
	
	public BusShellManager getManager() {
		return manager;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.IService#publish(org.osgi.framework.BundleContext)
	 * @Deprecated replace by {@link #publish(BusContext context)}
	 */
	@Deprecated
	@Override
	public boolean publish(BundleContext m_context) throws CIBusException {
		BusShellCondition condition = new BusShellCondition(BusOsgiUtil.getBundleClassLoader(m_context));
		condition.addDefaultShellClass(BusGateShell.class.getName());
		manager = new BusShellManager(condition);
		return false;
	}


	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.IService#publish(com.antelope.ci.bus.osgi.BusContext)
	 */
	@Override
	public boolean publish(BusContext context) throws CIBusException {
		BusShellCondition condition = new BusShellCondition(new Object[]{context}, context.getClassLoader());
		condition.addDefaultShellClass(BusGateShell.class.getName());
		manager = new BusShellManager(condition);
		return false;
	}
}
