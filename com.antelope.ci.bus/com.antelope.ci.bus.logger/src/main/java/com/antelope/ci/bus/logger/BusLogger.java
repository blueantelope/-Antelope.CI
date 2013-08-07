// com.antelope.ci.bus.logger.Logger.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.Constants;
import com.antelope.ci.bus.common.FileUtil;


/**
 * 启动日志服务
 * 使用log4j
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		上午10:56:57 
 */
public class BusLogger implements BundleActivator {
	private static boolean isStart = false;					// 日志系统是否启动
	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Start Logger for @Antelope CI Bus");
		String log_cnf = System.getProperty(Constants.LOG_CNF);
		if (FileUtil.existFile(log_cnf)) {
			System.out.println("use define log");
			PropertyConfigurator.configure(log_cnf);
		} else {
			PropertyConfigurator.configure(BusLogger.class.getResource("/log4j.properties"));
		}
		Logger log = Logger.getLogger(BusLogger.class);
		log.info("Welcome to Logger World!");
		isStart = true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		LogManager.resetConfiguration();			// 关闭log4j日志服务
	}
	

}

