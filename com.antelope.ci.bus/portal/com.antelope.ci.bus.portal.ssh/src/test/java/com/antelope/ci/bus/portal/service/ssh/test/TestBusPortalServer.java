// com.antelope.ci.bus.portal.test.TestBusPortalServer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.service.ssh.test;

import com.antelope.ci.bus.common.EncryptUtil.ASYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.common.EncryptUtil.SYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.engine.model.user.User;
import com.antelope.ci.bus.engine.model.user.User.AUTH_TYPE;
import com.antelope.ci.bus.engine.model.user.UserKey;
import com.antelope.ci.bus.engine.model.user.UserPassword;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.ssh.BusPortalSshServer;
import com.antelope.ci.bus.server.BusServerCondition;
import com.antelope.ci.bus.server.BusServerCondition.LAUNCHER_TYPE;
import com.antelope.ci.bus.server.BusServerConfig;
import com.antelope.ci.bus.server.service.auth.ssh.PasswordAuthServiceImpl;
import com.antelope.ci.bus.server.service.auth.ssh.PublickeyAuthServiceImpl;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-8		下午1:48:06 
 */
public class TestBusPortalServer extends BusPortalSshServer {

	public TestBusPortalServer() throws CIBusException {
		super();
	}

	
	@Override
	protected void init() throws CIBusException {
		config = new BusServerConfig();
		condition = new BusServerCondition();
		attatchCondition(condition);
	}
	
	@Override
	protected void customizeInit() throws CIBusException {
		BusPortalConfigurationHelper configurationHelper = BusPortalConfigurationHelper.getHelper();
		configurationHelper.init();
	}
	
	@Override
	protected void customizeConfig(BusServerConfig config) throws CIBusException {
		
	}
	
	@Override
	protected void attatchCondition(BusServerCondition server_condition)
			throws CIBusException {
		server_condition.setLauncherType(LAUNCHER_TYPE.PROXY);
		server_condition.addShellClass(TestBusPortalServer.class.getName());
		server_condition.addUser(createUser());
		server_condition.addAuthService(new PasswordAuthServiceImpl(condition.getUserMap()));
		server_condition.addAuthService(new PublickeyAuthServiceImpl(condition.getUserMap()));
	}
	
	private User createUser() {
		User user = new User();
		user.setAuth_type(AUTH_TYPE.PASSWORD.getCode());
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
				"-----BEGIN RSA PRIVATE KEY-----\n" +
				"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbp1o77lp+fRo4\n" +
				"Dx7Zm1vp0nsRlARFzCIQz87vTgTB0vrJRCdudwjzDwXWl1pI1fVZscOxvfkHLCKt\n" +
				"J1OXkLeSJ6fOMBzLUu31C6azfZZBGJkVLUAjsrreHsZBVSbjflwx0sUpflgokWID\n" +
				"jaCkLODrH4EW5TwkJz42gW8XiEZRO4dz+m9WCE3ZtCNp/st58K1mXCqMhdh7/2iP\n" +
				"C1f15hVduvWEmnjmPhyEvyb9CQv69QLT4or8xRI0g0TAONqzhD+lcBlR7xRp1WiZ\n" +
				"HHg5yzyZYIwsZmYpSz6uJP7EZ+0PdWqYi4wybWQKsvGW9Bxom8w1hzzgX1ytkzQG\n" +
				"DdQeTKytAgMBAAECggEAH0IPW+O05pvm7QUUU+zgG2e6cUBBT2b8HFYvH12SlP3y\n" +
				"gQKfzI45DN6idIK2jdPxzo5lV0doQCl72TwBjHlyc/Zku4Jr9eT0hnZNhtQcnXv8\n" +
				"GgWZUNHutvaFJ8+a3Kqehx7lmzyVJah0f3Qso2ujy3PrL5bfaf87fiSmE5q5TEyO\n" +
				"hKwBep7t98OJqh3eO5aC6cp6ESXh3ROkQCHeIasrEJLA13NfLe67AgsJ1C8CRX8M\n" +
				"+QzqhnOhr12EZdtzASP/K1QcWjC6XzdXnHp4M6cOCkEWtQkGELQSpoZd7mkY20t6\n" +
				"Js+G69VguoT+NGQQGUWvCCckGc+acz6ijew2GR5QQQKBgQD+6d86GhHh2kLWPWmc\n" +
				"LQN2deICoVRV1B4iCHikQxFa5Zj6/hdHnLGCvGhe6AFyTIpNQ1vhKSiOGmj7Yp2Z\n" +
				"SD0jBTWRq9xUcdhKbKKbttvTdnCdI8X3rMJjxYJ2iIbvcRdYeteUX8W8bGWB6oET\n" +
				"mNEyUhBgFcVuHoLgSjbhhg5H/QKBgQCcUS5nTzYnFmKqaVauO0UMI/AhqaSknmW2\n" +
				"uc5Gx5loIvBoieSSz40+Z31ROs63LeUlkFVrsRAJCG/Wp0WdGf2FZ4C8eQ+layqF\n" +
				"U/ryeEBanqVMpqK+jcQO7fvTIEvVBBKIQh9OX+e8eOcgjqn+nNU8MbvBIeeskCu8\n" +
				"74HT7tBecQKBgETglHxyhtpryxtwRJT6ZLEhcQwc41i9k9bGSzK3HH1fBFLtTQUA\n" +
				"hcY5ekoKjjmADO6LgMv5/wdGg49JOiQsrLhFtA/ETChVCogxik5gvk0I96y9+cPO\n" +
				"XkfvGi1E/pC8Ef7KUm244wjikRrlbjMJW50pJ1uPBmWWwO4xn1qYiktRAoGAWjlV\n" +
				"QlXrGMaH7npciLY3FVnM6XEkUcwHQ1+dmybr+bpo7r3gi4h2qvVK1M7Pr9UytOrJ\n" +
				"Ex6g9cXD366utI3i0dQ67w4lJMJlSY1+g+M6qdN173ci6j+jUUa1mlfCzVHLbJBV\n" +
				"x4P9msBiD12o6wgU3+iIyWHLecPBKw1G70+hP1ECgYEAwlXEOlDbnffcHmeB72DT\n" +
				"jf2V2GSjrOC2Y6JvD+ArrRMplDgeAXTUnrRj56x4lv5G3Rmu0xX8bvvl4/+YH8eN\n" +
				"wZ2MdRJZIDw5VvxMxBSfN6MBi2pS6uZTBvV1oQpVniaW4YiGqGfqovSMccwByMAa\n" +
				"fveFzes8Pv6r5TGwI1mSmwo=\n" +
				"-----END RSA PRIVATE KEY-----\n"
		;
		String publicKey =
				"---- BEGIN SSH2 PUBLIC KEY ----\n" +
				"Comment: \"2048-bit RSA, converted from @Antelope.CI.BUS by bluea \\ \n" +
				"ntelope@localhost\"\n" +
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm6daO+5afn0aOA8e2Ztb6dJ7EZ\n" +
				"QERcwiEM/O704EwdL6yUQnbncI8w8F1pdaSNX1WbHDsb35BywirSdTl5C3kienzjAcy1Lt\n" +
				"9Qums32WQRiZFS1AI7K63h7GQVUm435cMdLFKX5YKJFiA42gpCzg6x+BFuU8JCc+NoFvF4\n" +
				"hGUTuHc/pvVghN2bQjaf7LefCtZlwqjIXYe/9ojwtX9eYVXbr1hJp45j4chL8m/QkL+vUC\n" +
				"0+KK/MUSNINEwDjas4Q/pXAZUe8UadVomRx4Ocs8mWCMLGZmKUs+riT+xGftD3VqmIuMMm\n" +
				"1kCrLxlvQcaJvMNYc84F9crZM0Bg3UHkysrQIDAQAB\n" +
				"---- END SSH2 PUBLIC KEY ----\n"
		;
		userKey.setPrivateKey(privateKey);
		userKey.setPublicKey(publicKey);
		
		return user;
	}
}

