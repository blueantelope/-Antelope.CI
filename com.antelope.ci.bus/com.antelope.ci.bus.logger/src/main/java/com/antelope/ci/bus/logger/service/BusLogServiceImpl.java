// com.antelope.ci.bus.logger.service.BusLogServiceImpl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.logger.service;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.DebugUtil;
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
	
	/**
	 * 初始化log4j
	 */
	public BusLogServiceImpl() {
		String log_cnf = System.getProperty(BusConstants.LOG_CNF);
		LogManager.resetConfiguration();			// 重置log4j日志服务
		if (FileUtil.existFile(log_cnf)) {
			try {
				Properties log_props = new Properties(); 
				log_props.load(new FileInputStream(log_cnf));
				log_props.setProperty("log_dir", System.getProperty(BusConstants.LOG_DIR));
				DebugUtil.assert_out("logger 日志配置：" + log_cnf);
				PropertyConfigurator.configure(log_props);
			} catch (Exception e) {
				DebugUtil.assert_exception(e);
				PropertyConfigurator.configure(BusLogServiceImpl.class.getResource("/log4j.properties"));
			}
		} else {
			PropertyConfigurator.configure(BusLogServiceImpl.class.getResource("/log4j.properties"));
		}
		log4j = Logger.getLogger(BusLogServiceImpl.class);
		log4j.info("Welcome to Logger World!");
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.logger.service.BusLogService#getLog4j(java.lang.Class)
	 */
	@Override
	public Logger getLog4j(Class clazz) {
		return Logger.getLogger(clazz);
		
	}
}

