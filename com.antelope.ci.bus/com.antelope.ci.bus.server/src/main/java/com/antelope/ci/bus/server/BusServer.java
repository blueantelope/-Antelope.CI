// com.antelope.ci.bus.server.BusServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.exception.CIBusException;
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
	private static final Logger log = Logger.getLogger(BusServer.class);	// log4j
	protected BusServerConfig config; // server配置项
	protected BusServerCondition condition;
	protected static final long WAIT_INIT = 10 * 1000; // 10 seconds
	protected static final long SLEEP_WAIT_INIT = 500; // 500 millisecond 
	protected long waitForStart;
	protected boolean running;
	private ReadWriteLock locker = new ReentrantReadWriteLock();
	private Lock readLocker = locker.readLock();
	private Lock writeLocker = locker.writeLock();
	
	public BusServer() throws CIBusException {
		init();
		customizeInit();
	}
	
	public void setWaitForStart(long waitForStart) {
		this.waitForStart = waitForStart;
	}
	
	public BusServerConfig getConfig() {
		return config;
	}

	public BusServerCondition getCondition() {
		return condition;
	}
	
	public void open() throws CIBusException {
		writeLocker.lock();
		try {
			if (config.isSwitcher() && !running) {
				log.info(toSummary() + " starting");
				start();
				running= true;
			}
		} finally {
			writeLocker.unlock();
		}
	}
	
	public void close() throws CIBusException {
		writeLocker.lock();
		try {
			if (running) {
				log.info(toSummary() + " shutdowning");
				shutdown();
			}
		} finally {
			writeLocker.unlock();
		}
	}
	
	public boolean running() {
		readLocker.lock();
		try {
			return running;
		} finally {
			readLocker.unlock();
		}
	}
	
	public BusServerIdentity refreshIdentity() {
		BusServerIdentity identity = new BusServerIdentity();
		identity.setHost(config.getHost());
		identity.setPort(config.getPort());
		identity.setProto(config.getProto());
		identity.setSummary(toSummary());
		return identity;
	}

	protected void init() throws CIBusException {
		running = false;
		waitForStart = 0;
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
		while ((System.currentTimeMillis()-start_tm) < WAIT_INIT) {
			try {
				Thread.sleep(SLEEP_WAIT_INIT);
			} catch (InterruptedException e) {}
			if (userstore_added) {
				List<AuthService> authServices = BusServerTemplateActivator.getAuthServices();
				for (AuthService authService : authServices) {
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
				UserStoreServerService userStoreService = BusServerTemplateActivator.getUserStoreService();
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
		BusServerConfig config = BusServerConfig.load();
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
	protected abstract void start() throws CIBusException;
	
	/*
	 * 关闭服务
	 */
	protected abstract void shutdown() throws CIBusException;
	
	/*
	 * 取得服务描述
	 */
	public abstract String toSummary();
}
