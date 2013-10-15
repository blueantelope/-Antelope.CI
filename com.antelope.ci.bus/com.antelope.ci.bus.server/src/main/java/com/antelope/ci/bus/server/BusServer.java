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
import java.util.HashMap;
import java.util.Map;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.EncryptUtil.ASYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.common.EncryptUtil.SYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.service.impl.PasswordAuthServiceImpl;
import com.antelope.ci.bus.server.service.impl.PublickeyAuthServiceImpl;
import com.antelope.ci.bus.server.service.user.User;
import com.antelope.ci.bus.server.service.user.User.AUTH_TYPE;
import com.antelope.ci.bus.server.service.user.UserKey;
import com.antelope.ci.bus.server.service.user.UserPassword;
import com.antelope.ci.bus.server.shell.BusShellFactory;


/**
 * 持续集成的server
 * 使用ssh建立server
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-5		下午10:19:24 
 */
public class BusServer {
	private SshServer sshServer;
	private BusServerConfig config;					// server配置项
	
	public void BusServer() {
		config = new BusServerConfig();
	}
	
	public void setConfig(BusServerConfig config) {
		if (config != null)
			this.config = config;
	}
	
	public void start() throws IOException, CIBusException {
		sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(config.getPort());
		String key_path;
		switch (config.getKt()) {
			case DYNAMIC:
				key_path = getKeyPath(config.getKey_name());
				sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(key_path));
				break;
			case STATIC:
			default:
				URL key_url = config.getKey_url();
				key_path = key_url==null?"":key_url.getFile();
				sshServer.setKeyPairProvider(new FileKeyPairProvider(new String[] {key_path}));
				break;
		}
		sshServer.setShellFactory(new BusShellFactory());
	
		Map<String, User> userMap = initUserMap();
		
		sshServer.setPasswordAuthenticator(new PasswordAuthServiceImpl(userMap));
		sshServer.setPublickeyAuthenticator(new PublickeyAuthServiceImpl(userMap));
		
		sshServer.start();
		System.out.println("ssh server startup");
	}
	
	private Map<String, User> initUserMap() {
		Map<String, User> userMap = new HashMap<String, User>();
		User user = new User();
		user.setAuth_type(AUTH_TYPE.PASSWORD);
		user.setUsername("blueantelope");
		UserPassword userPassword = new UserPassword();
		userPassword.setAlgorithm(SYMMETRIC_ALGORITHM._ORIGIN);
		userPassword.setPassword("blueantelope");
		userPassword.setSeed("seed");
		userPassword.setOriginPwd("blueantelope");
		user.setPassword(userPassword);
		UserKey userKey = new UserKey();
		userKey.setAlgorithm(ASYMMETRIC_ALGORITHM._DSA);
		String privateKey = 
				"-----BEGIN DSA PRIVATE KEY-----\n" +
				"MIIBTAIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdS\n" +
				"PO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVCl\n" +
				"pJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith\n" +
				"1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7L\n" +
				"vKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3\n" +
				"zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImo\n" +
				"g9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFwIVAJNNhJ136mjMnQSLBcRP2cM+sCme\n" +
				"-----END DSA PRIVATE KEY-----	\n"
		;
		String publicKey =
				"ssh-dsa MIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO" +
				"9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6A" +
				"R7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RS" +
				"AHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6O" +
				"uqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4" +
				"KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO" +
				"/7PSSoDgYUAAoGBAIBroh6I3tt2jnmuZ3wXYfF/zapAj/7bKsNZEvivK+XleO/gV61qJhEjvq9" +
				"72q+jJE2Ct6gyQoQ30QgyBcbmwN+aIRyrJCzGM3Q4JA9BezY9DspNQyR7Ezp70qCjsDzka" +
				"1PhU47DqiPxQ38TjV62clUt6CwaCoGi6vyanKIbkuO9 blueantelope@localhost"
		;
		userKey.setPrivateKey(privateKey);
		userKey.setPublicKey(publicKey);
		
		userMap.put("blueantelope", user);
		return userMap;
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
}
