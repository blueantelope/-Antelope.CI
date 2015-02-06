// com.antelope.ci.bus.portal.BusPortalActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal;

import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.ServicePublisher;
import com.antelope.ci.bus.server.BusServerActivator;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-21		下午12:10:23 
 */
public class BusPortalActivator extends BusServerActivator {
	protected static final String BANNER = "bus.portal.banner";
	private final static String PARSER = "bus.portal.parser";	
	private final static String DEF_PARSER = "static";
	protected final static String START_WAIT = "bus.portal.start.wait";
	private final static long DEF_START_WAIT = 0;
	protected final static String FORM_COMMAND_WAIT = "bus.portal.form.command.wait";
	private final static long DEF_FORM_COMMAND_WAIT = 30 * 1000;
	
	public static String getPortalBanner() {
		return PropertiesUtil.getString(properties, BANNER, getBanner());
	}
	
	public static String getParser() {
		return PropertiesUtil.getString(properties, PARSER, DEF_PARSER);
	}
	
	public static long getStartWait() {
		return PropertiesUtil.getLong(properties, START_WAIT, DEF_START_WAIT);
	}
	
	public static long getFormCommandWait() {
		return PropertiesUtil.getLong(properties, FORM_COMMAND_WAIT, DEF_FORM_COMMAND_WAIT);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServerActivator#run()
	 */
	@Override
	protected void run() throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServerActivator#destroy()
	 */
	@Override
	protected void destroy() throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#handleLoadService(java.lang.String, org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	@Override
	protected void handleLoadService(String clsName, ServiceReference ref,
			Object service) throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#handleUnloadService(org.osgi.framework.ServiceReference)
	 */
	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#handleStopAllService()
	 */
	@Override
	protected void handleStopAllService() throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServerActivator#addServices()
	 */
	@Override
	protected void addServices() throws CIBusException {
		ServicePublisher.publish(m_context, "com.antelope.ci.bus.portal.service");
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.BusActivator#removeServices()
	 */
	@Override
	protected void removeServices() throws CIBusException {
		
	}
}
