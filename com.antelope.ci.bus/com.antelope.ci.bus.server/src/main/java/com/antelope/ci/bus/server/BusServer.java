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
	private static ServiceReference log_ref;
	private static Logger log4j;			// log4j
	private BusSshServer sshServer;
	private static final String LOGSERVICE_CLSNAME = "com.antelope.ci.bus.logger.service.BusLogService";
	
	public static Logger getLog4j() {
		return log4j;
	}
	
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		m_context = context;
		context.addServiceListener(this);
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
		uninstallLogService();
	}
	
	private void installLogService() {
		log_ref = m_context.getServiceReference(LOGSERVICE_CLSNAME);
		BusLogService logService = (BusLogService) log_ref;
		log4j = logService.getLog4j(BusServer.class);
		log4j.info("得到Bus Log Service");
	}
	
	private void uninstallLogService() {
		if (log_ref != null)
			m_context.ungetService(log_ref);
		log4j = null;
	}
	
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
	 */
	@Override
	public void serviceChanged(ServiceEvent event)  {
        String[] objectClass = (String[]) event.getServiceReference().getProperty("objectClass");
        String service_clsname = objectClass[0];
        if (event.getType() == ServiceEvent.REGISTERED) {
        	if (service_clsname.equals(LOGSERVICE_CLSNAME)) {
        		installLogService();
        	}
        }  else if (event.getType() == ServiceEvent.UNREGISTERING) {
        	if (service_clsname.equals(LOGSERVICE_CLSNAME)) {
        		uninstallLogService();
        	}
        } else if (event.getType() == ServiceEvent.MODIFIED) {
        	if (service_clsname.equals(LOGSERVICE_CLSNAME)) {
        		uninstallLogService();
        		installLogService();
        	}
        }
    }

}

