// com.antelope.ci.bus.logger.service.BusLogServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger.service;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.antelope.ci.bus.common.Constants;
import com.antelope.ci.bus.common.FileUtil;


/**
 * 日志对外服务实现
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-7		下午4:39:44 
 */
public class BusLogServiceImpl implements BusLogService {
	private static Logger log4j;
	private static boolean isStart = false;					// 日志系统是否启动
	
	/**
	 * 初始化log4j
	 */
	public BusLogServiceImpl() {
		String log_cnf = System.getProperty(Constants.LOG_CNF);
		if (FileUtil.existFile(log_cnf)) {
			System.out.println("use define log");
			PropertyConfigurator.configure(log_cnf);
		} else {
			PropertyConfigurator.configure(BusLogServiceImpl.class.getResource("/log4j.properties"));
		}
		log4j = Logger.getLogger(BusLogServiceImpl.class);
		log4j.info("Welcome to Logger World!");
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.logger.service.BusLogService#getLot4j(java.lang.Class)
	 */
	@Override
	public Logger getLot4j(Class clazz) {
		if (isStart)
			return Logger.getLogger(clazz);
		
		return null;
	}
}

