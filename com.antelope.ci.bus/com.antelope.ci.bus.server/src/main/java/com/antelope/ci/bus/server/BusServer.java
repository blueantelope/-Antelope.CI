// com.antelope.ci.bus.server.BusServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.Channel;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.channel.ChannelDirectTcpip;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.server.BusServerCondition.LAUNCHER_TYPE;
import com.antelope.ci.bus.server.service.AuthService;
import com.antelope.ci.bus.server.service.UserStoreServerService;
import com.antelope.ci.bus.server.shell.BusShellContainerLauncher;
import com.antelope.ci.bus.server.shell.BusShellFactory;
import com.antelope.ci.bus.server.shell.BusShellLauncher;
import com.antelope.ci.bus.server.shell.BusShellProxyLauncher;


/**
 * 持续集成的server
 * 使用ssh建立server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-5		下午10:19:24 
 */
public abstract class BusServer {
	protected SshServer sshServer;
	protected BusServerConfig config; // server配置项
	protected BusServerCondition condition;
	private static final long waitForInit = 3 * 1000; // 3 seconds
	protected long waitForStart = 0;
	protected BundleContext m_context;
	
	public BusServer() throws CIBusException {
		init();
		customInit();
	}
	
	public void setWaitForStart(long waitForStart) {
		this.waitForStart = waitForStart;
	}

	public BusServer(BundleContext m_context) throws CIBusException {
		this.m_context = m_context;
		init();
		customInit();
	}
	
	public BusServerConfig getConfig() {
		return config;
	}

	public BusServerCondition getCondition() {
		return condition;
	}

	protected void init() throws CIBusException {
		config = readConfig();
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
				UserStoreServerService userStoreService = (UserStoreServerService) CommonBusActivator.getUsingService(UserStoreServerService.SERVICE_NAME);
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

	
	public void start() throws CIBusException {
		sshServer = SshServer.setUpDefaultServer();
		sshServer.setChannelFactories(Arrays.<NamedFactory<Channel>>asList(
                new BusServerChannelSession.Factory(),
                new ChannelDirectTcpip.Factory()));
		sshServer.setPort(config.getPort());
		String key_path;
		switch (config.getKt()) {
			case DYNAMIC:
				key_path = getKeyPath(config.getKey_name());
				sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(key_path));
				break;
			case FIXED:
			default:
				URL key_url = config.getKey_url();
				key_path = key_url==null?"":key_url.getFile();
				sshServer.setKeyPairProvider(new FileKeyPairProvider(new String[] {key_path}));
				break;
		}
		
		BusShellLauncher shellLauncher = null;
		switch (condition.getLauncherType()) {
			case PROXY:
				shellLauncher = new BusShellProxyLauncher();
				break;
			case CONTAINER:
				shellLauncher = new BusShellContainerLauncher();
				break;
			default:
				break;
		}
		BusShellFactory shellFactory;
		if (shellLauncher == null) {
			if (condition.getLauncher_class() != null) {
				shellFactory = new BusShellFactory(condition.getLauncher_class());
			} else if (condition.getLauncher_className() != null
					&& condition.getLauncher_className().length() > 0) {
				shellFactory = new BusShellFactory(condition.getLauncher_className());
			} else {
				throw new CIBusException("", "create shell factory error");
			}
		} else {
			shellLauncher.setCondition(condition);
			shellFactory = new BusShellFactory(shellLauncher);
		}
		sshServer.setShellFactory(shellFactory);
		for (AuthService authService : condition.getAuthServiceList()) {
			switch (authService.getAuthType()) {
				case PASSWORD:
					sshServer.setPasswordAuthenticator(authService);
					break;
				case PUBLICKEY:
					sshServer.setPublickeyAuthenticator(authService);
					break;
			}
		}
		if (waitForStart != 0)
			try {
				Thread.sleep(waitForStart * 1000);
			} catch (InterruptedException e) {
			}
		try {
			sshServer.start();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
		customRun();
	}
	
	public void stop() {
		if (sshServer != null) {
			try {
				sshServer.stop(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 得到密钥所在路径
	 * 先使用ci bus的缓存目录
	 * 如果不存在，使用系统的缓存目录
	 * 如果都不存在，放在与类同一级目录下
	 */
	private String getKeyPath(String key_name) {
		String key_path = key_name;
		if (System.getProperty(BusConstants.CACHE_DIR) != null) {
			String cache_dir = System.getProperty(BusConstants.CACHE_DIR);
			if (FileUtil.existDir(cache_dir)) {
				key_path = cache_dir + File.separator + key_name;
			} else {
				cache_dir = System.getProperty("java.io.tmpdir");
				if (FileUtil.existDir(cache_dir)) {
					key_path = cache_dir + File.separator + key_name;
				}
			}
		}
		
		return key_path;
	}
	
	protected static class BusServerChannelSession extends ChannelSession {
		public int getWidth() {
			return Integer.valueOf(super.getEnvironment().getEnv().get(Environment.ENV_COLUMNS));
		}
		
		public int getHeight() {
			return Integer.valueOf(super.getEnvironment().getEnv().get(Environment.ENV_LINES));
		}
	}

	
	/*
	 * 读取服务配置
	 */
	protected abstract BusServerConfig readConfig() throws CIBusException;
	
	/*
	 * 加入条件变量
	 */
	protected abstract void attatchCondition(BusServerCondition server_condition) throws CIBusException;
	
	/*
	 * 自定义初始化时的动作
	 */
	protected abstract void customInit()  throws CIBusException;
	
	/*
	 * 自定义需要在启动时的动作
	 */
	protected abstract void customRun() throws CIBusException;
}
