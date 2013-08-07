// com.antelope.ci.bus.server.MainServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

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
public class BusServer implements BundleActivator, ServiceListener {
	private BundleContext m_context;
	private static BusLogService logService;
	private ServiceReference log_ref;
	private static Logger log4j;			// log4j
	private BusSshServer sshServer;
	
	public static BusLogService getLogService() {
		return logService;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		addLogService();
		if (log4j != null)
			log4j.info("Bus Server Start...");
		sshServer = new BusSshServer();
		sshServer.start();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		sshServer.stop();
	}
	
	private void addLogService() {
		log_ref = m_context.getServiceReference(BusLogService.class.getName() );
        if  (log_ref != null) {
        	logService = m_context.getService(log_ref);
        	log4j = logService.getLot4j(BusServer.class);
        }
	}
	
	public void removeLogService() {
		if (log_ref != null)
			m_context.ungetService(log_ref);
		log_ref = null;
		logService =  null;
		log4j = null;
	}
	

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
	 */
	@Override
	public void serviceChanged(ServiceEvent event) {
		if (event.getType() == ServiceEvent.REGISTERED) {
			addLogService();
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
        	removeLogService();
        } else if (event.getType() == ServiceEvent.MODIFIED) {
        	removeLogService();
        	addLogService();
        }
	}

}

