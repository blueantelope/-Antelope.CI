// com.antelope.ci.bus.portal.service.ssh.BusPortalSshService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.service.ssh;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.IService;
import com.antelope.ci.bus.osgi.Service;
import com.antelope.ci.bus.portal.core.entrance.EntranceManager;
import com.antelope.ci.bus.server.ssh.BusSshServer;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月6日		下午3:03:34 
 */
@Service(name="com.antelope.ci.bus.portal.service.ssh.BusPortalSshService")
public class BusPortalSshService implements IService {
	private static final Logger log = Logger.getLogger(BusPortalSshService.class);
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.IService#register(org.osgi.framework.BundleContext)
	 */
	@Override
	public void register(BundleContext m_context) throws CIBusException {
		log.info("portal on ssh start...");
		BusSshServer server = new BusPortalSshServer(m_context);
		server.start();
	}
}

