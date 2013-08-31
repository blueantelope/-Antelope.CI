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

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.logger.service.BusLogService;
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
	private BusLogService logService;				// 日志对外服务
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#run()
	 */
	@Override
	protected void run() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#destroy()
	 */
	@Override
	protected void destroy() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#handleLoadService()
	 */
	@Override
	protected void handleLoadService() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#handleUnloadService()
	 */
	@Override
	protected void handleUnloadService() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addServices() throws CIBusException {
		if (logService == null) {
			String clazz = BusLogService.class.getName();
			logService = new BusLogServiceImpl();
			Dictionary<String, ?> properties = new Hashtable();
			m_context.registerService(clazz, logService, properties);
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.osgi.CommonBusActivator#removeServices()
	 */
	@Override
	protected  void removeServices() throws CIBusException {
		LogManager.resetConfiguration();			// 关闭log4j日志服务
	}
}

