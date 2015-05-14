// com.antelope.ci.bus.logger.BusLoggerComplexActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger;

import org.apache.log4j.LogManager;
import org.osgi.framework.ServiceReference;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusComplexActivator;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月14日		下午5:34:38 
 */
public class BusLoggerComplexActivator extends BusComplexActivator {

	@Override
	protected void customInit() throws CIBusException {
		bus_context.undesiredLoadLogService();
	}

	@Override
	protected void run() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroy() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleLoadService(String clsName, ServiceReference ref,
			Object service) throws CIBusException {
		
		// TODO Auto-generated method stub
		
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

	@Override
	protected void addServices() throws CIBusException {
		bus_context.addLogService();
	}

	@Override
	protected void removeServices() throws CIBusException {
		LogManager.shutdown(); // 关闭log4j日志服务
	}
}

