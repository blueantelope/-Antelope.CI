// com.antelope.ci.bus.logger.Logger.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.log4j.LogManager;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.DebugUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.logger.service.BusLogServiceImpl;
import com.antelope.ci.bus.osgi.CommonBusActivator;


/**
 * 启动日志服务
 * 使用log4j
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		上午10:56:57 
 */
public class BusLoggerActivator extends CommonBusActivator {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#customInit()
	 */
	@Override
	protected void customInit() throws CIBusException {
		super.logServiceProvider = true;			// logService的提供者
	}
	

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#run()
	 */
	@Override
	protected void run() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroy() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleLoadService(String clsName, ServiceReference ref, Object service) throws CIBusException {
		
	}

	@Override
	protected void handleUnloadService(ServiceReference ref)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleStopAllService() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#addServices()
	 */
	@Override
	protected void addServices() throws CIBusException {
		if (logService == null) {
			logService = new BusLogServiceImpl();
			Dictionary<String, ?> properties = new Hashtable<String, Object>();
			m_context.registerService(LOGSERVICE_CLSNAME, logService, properties);
			DebugUtil.assert_out("注册log4j服务");
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#removeServices()
	 */
	@Override
	protected void removeServices() throws CIBusException {
		LogManager.shutdown();			// 关闭log4j日志服务
	}
}

