// com.antelope.ci.bus.server.BusServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.ssh;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.apache.log4j.Logger;
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
import com.antelope.ci.bus.server.BusServer;
import com.antelope.ci.bus.server.common.BusLauncher;
import com.antelope.ci.bus.server.service.auth.AuthService;
import com.antelope.ci.bus.server.service.auth.ssh.SshAuthService;
import com.antelope.ci.bus.server.shell.launcher.BusShellLauncher;


/**
 * 持续集成的server
 * 使用ssh建立server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-5		下午10:19:24 
 */
public abstract class BusSshServer extends BusServer {
	private static final Logger log = Logger.getLogger(BusSshServer.class);
	protected SshServer sshServer;
	protected BusLauncher launcher;
	
	public BusSshServer() throws CIBusException {
		super();
	}
	
	public BusSshServer(BundleContext bundle_context) throws CIBusException {
		super(bundle_context);
	}
	
	public BusSshServer(BundleContext bundle_context, BusShellLauncher launcher) throws CIBusException {
		super(bundle_context);
		this.launcher = launcher;
	}
	
	public void initLauncher(BusLauncher launcher) {
		this.launcher = launcher;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#start()
	 */
	@Override
	protected void start() throws CIBusException {
		initServer();
		configServer();
		runServer();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#shutdown()
	 */
	@Override
	protected void shutdown() {
		if (sshServer != null) {
			try {
				sshServer.stop(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void initServer() {
		sshServer = SshServer.setUpDefaultServer();
		sshServer.setChannelFactories(Arrays.<NamedFactory<Channel>>asList(
                new BusServerChannelSession.Factory(),
                new ChannelDirectTcpip.Factory()));
		sshServer.setPort(config.getPort());
	}
	
	protected void configServer() throws CIBusException {
		String key_path;
		switch (config.getKt()) {
			case DYNAMIC:
				key_path = getKeyPath(config.getKey_name());
				sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(key_path));
				break;
			case FIXED:
			default:
				URL key_url = config.getKey_url();
				key_path = (key_url==null?"":key_url.getFile());
				sshServer.setKeyPairProvider(new FileKeyPairProvider(new String[] {key_path}));
				break;
		}
		
		switch(condition.getServerType()) {
			case API:
			case SHELL:
			default:
				configChannel();
				break;
		}
		
		for (AuthService authService : condition.getAuthServiceList()) {
			switch (authService.getAuthType()) {
				case PASSWORD:
					sshServer.setPasswordAuthenticator((SshAuthService)authService);
					break;
				case PUBLICKEY:
					sshServer.setPublickeyAuthenticator((SshAuthService)authService);
					break;
			}
		}
	}
	
	protected void runServer() throws CIBusException {
		beforeRun();
		if (waitForStart != 0)
			try {
				Thread.sleep(waitForStart * 1000);
			} catch (InterruptedException e) {
			}
		try {
			sshServer.start();
		} catch (IOException e) {
			throw new CIBusException("", "server start failure", e);
		}
		afterRun();
	}
	
	private void configChannel() throws CIBusException {
		BusSshCommand command = new BusSshCommand(launcher);
		BusSshFactory factory = new BusSshFactory(command);;
		sshServer.setShellFactory(factory);
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
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.BusServer#toSummary()
	 */
	@Override
	public String toSummary() {
		return "SSH Server";
	}
	
	/*
	 * 启动前的动作
	 */
	protected abstract void beforeRun() throws CIBusException;
	
	/*
	 * 启动后的动作
	 */
	protected abstract void afterRun() throws CIBusException;
}
