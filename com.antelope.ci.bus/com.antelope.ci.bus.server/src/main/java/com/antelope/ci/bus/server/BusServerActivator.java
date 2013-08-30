// com.antelope.ci.bus.server.MainServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.framework.CommonBusActivator;
import com.antelope.ci.bus.logger.service.BusLogService;
import com.antelope.ci.bus.server.ssh.BusSshServer;

/**
 * 持续bus总线服务
 * 使用ssh方式
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-30		下午11:23:33 
 */
public class BusServerActivator extends CommonBusActivator {
	private static ServiceReference log_ref;
	private static Logger log4j;			// log4j
	private BusSshServer sshServer;
	private static final String LOGSERVICE_CLSNAME = "com.antelope.ci.bus.logger.service.BusLogService";
	
	public static Logger getLog4j() {
		return log4j;
	}
	
	public BusServerActivator() {
		super();
	}
	
	public BusServerActivator(Properties props) {
		super(props);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.framework.CommonBusActivator#run()
	 */
	@Override
	protected void run() throws CIBusException {
		try {
			sshServer = new BusSshServer();
			sshServer.setPort(9426);
			sshServer.start();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.framework.CommonBusActivator#destroy()
	 */
	@Override
	protected void destroy() throws CIBusException {
		if (sshServer != null) {
			sshServer.stop();
			log_ref = null;
			log4j = null;
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.framework.CommonBusActivator#handleLoadService()
	 */
	@Override
	protected void handleLoadService() {
		if (serviceMap.get(LOGSERVICE_CLSNAME) != null) {
			log_ref = serviceMap.get(LOGSERVICE_CLSNAME);
			BusLogService logService = (BusLogService) log_ref;
			log4j = logService.getLog4j(BusServerActivator.class);
			log4j.info("得到Bus Log Service");
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.framework.CommonBusActivator#handleUnloadService()
	 */
	@Override
	protected void handleUnloadService() {
		log_ref = null;
		log4j = null;
	}

}

