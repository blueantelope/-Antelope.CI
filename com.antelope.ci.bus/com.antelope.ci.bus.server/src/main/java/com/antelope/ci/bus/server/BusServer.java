// com.antelope.ci.bus.server.BusServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.server.BusServerCondition.LAUNCHER_TYPE;
import com.antelope.ci.bus.server.service.UserStoreServerService;
import com.antelope.ci.bus.server.service.auth.AuthService;


/**
 * 持续集成的server
 * 使用ssh建立server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-5		下午10:19:24 
 */
public abstract class BusServer {
	private static final Logger log = Logger.getLogger(BusServer.class);
	protected BusServerConfig config; // server配置项
	protected BusServerCondition condition;
	private static final long waitForInit = 3 * 1000; // 3 seconds
	protected long waitForStart = 0;
	protected BundleContext m_context;
	
	public BusServer() throws CIBusException {
		init();
		customizeInit();
	}
	
	public void setWaitForStart(long waitForStart) {
		this.waitForStart = waitForStart;
	}

	public BusServer(BundleContext m_context) throws CIBusException {
		this.m_context = m_context;
		init();
		customizeInit();
	}
	
	public BusServerConfig getConfig() {
		return config;
	}

	public BusServerCondition getCondition() {
		return condition;
	}

	protected void init() throws CIBusException {
		try {
			config = parseConfig();
		} catch (CIBusException e) {
			log.warn("parse error bus.properties, using with default");
		}
		if (config == null)
			config = new BusServerConfig();
		customizeConfig(config);
		
		condition = new BusServerCondition();
		condition.setLauncherType(LAUNCHER_TYPE.CONTAINER);
		long start_tm = System.currentTimeMillis();
		boolean pwd_added = false;
		boolean key_added = false;
		boolean userstore_added = false;
		while ((System.currentTimeMillis()-start_tm) < waitForInit) {
			if (userstore_added) {
				List authServices = CommonBusActivator.getServices(AuthService.SERVICE_NAME);
				if (authServices == null)
					continue;
				for (Object service : authServices) {
					AuthService authService = (AuthService) service;
					switch (authService.getAuthType()) {
						case PASSWORD:
							if (!pwd_added) {
								authService.initUserStore(condition.getUserMap());
								condition.addAuthService(authService);
								pwd_added = true;
							}
							break;
						case PUBLICKEY:
							if (!key_added) {
								authService.initUserStore(condition.getUserMap());
								condition.addAuthService(authService);
								key_added = true;
							}
							break;
					}
				}
				if (pwd_added && key_added)
					break;
			} else {
				UserStoreServerService userStoreService = 
						(UserStoreServerService) CommonBusActivator.getUsingService(UserStoreServerService.SERVICE_NAME);
				if (userStoreService == null)
					continue;
				condition.setUserMap(userStoreService.getUserMap());
				userstore_added = true;
			}
		}
		if (!userstore_added)
			throw new CIBusException("", "server could not get service of user store");
		if (!pwd_added || !key_added)
			throw new CIBusException("", "server could not get services of auth");
		attatchCondition(condition);
	}

	/*
	 * 解析bus.properties配置
	 */
	protected BusServerConfig parseConfig() throws CIBusException {
		Properties props = BusCommonServerActivator.getProperties();
		BusServerConfig config = BusServerConfig.fromProps(props);
		return config;
	}
	
	/*
	 * 定制服务端配置
	 */
	protected abstract void customizeConfig(BusServerConfig config) throws CIBusException;
	
	/*
	 * 加入条件变量
	 */
	protected abstract void attatchCondition(BusServerCondition server_condition) throws CIBusException;
	
	/*
	 * 自定义初始化时的动作
	 */
	protected abstract void customizeInit()  throws CIBusException;
	
	/*
	 * 启动服务
	 */
	public abstract void start() throws CIBusException;
	
	/*
	 * 关闭服务
	 */
	public abstract void shutdown() throws CIBusException;
}
