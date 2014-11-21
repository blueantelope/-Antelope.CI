// com.antelope.ci.bus.server.service.userstore.PaswordHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service.userstore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.EncryptUtil.ASYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.common.EncryptUtil.SYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.engine.model.user.User;
import com.antelope.ci.bus.engine.model.user.UserKey;
import com.antelope.ci.bus.engine.model.user.UserPassword;
import com.antelope.ci.bus.engine.model.user.User.AUTH_TYPE;


/**
 * file operation for password
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-8		下午2:05:50 
 */
class PasswordHelper {
	private static final PasswordHelper helper = new PasswordHelper();
	private static final Logger log = Logger.getLogger(PasswordHelper.class);
	
	public static final PasswordHelper getHelper() {
		return helper;
	}
	
	Map<String, User> userMap = new HashMap<String, User>();
	private PasswordHelper() {
	}
	void init() {
		parse(this.getClass().getResource("/com/antelope/ci/bus/server/service/userstore/password"));
	}
	
	private void parse(URL url) {
		BufferedReader reader = null;
		try {
			File dir = new File(url.getFile()).getParentFile();
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line=reader.readLine()) != null) {
				if (line.trim().startsWith("#"))
					continue;
				try {
					User user = new User();
					UserPassword uPasswd = new UserPassword();
					UserKey uKey = new UserKey();
					String[] es = line.split(":");
					int n = 0;
					for (final String e : es) {
						switch (n) {
							case 0:			// username
								user.setUsername(e);
								break;
							case 1:			// auth_type
								String[] as =  e.split("\\|");
								int auth_type = 0;
								for (String a : as) {
									auth_type |= AUTH_TYPE.toAuthType(a).getCode();
								}
								user.setAuth_type(auth_type);
								break;
							case 2:			// passwd_alg
								if (e.length() > 0) {
									uPasswd.setAlgorithm(SYMMETRIC_ALGORITHM.toAlgorithm(e));
								}
								break;
							case 3:			// seed
								if (e.length() > 0) {
									uPasswd.setSeed(e);
								}
								break;
							case 4:			// orgin_passwd
								if (e.length() > 0) {
									uPasswd.setOriginPwd(e);
								}
								break;
							case 5:			// passwd
								if (e.length() > 0) {
									uPasswd.setPassword(e);
								}
								break;
							case 6:			// key_alg
								if (e.length() > 0) {
									uKey.setAlgorithm(ASYMMETRIC_ALGORITHM.toCipher(e));
								}
								break;
							case 7:			// prikey
								if (e.length() > 0) {
									String ku = getSubUrl(dir, e);
									if (ku != null) {
										uKey.setPrivateKey(ku);
									}
								}
								break;
							case 8:			// pubkey
								if (e.length() > 0) {
									String ku = getSubUrl(dir, e);
									if (ku != null) {
										uKey.setPublicKey(ku);
									}
								}
								break;
							case 9:			// passphase
								if (e.length() > 0) {
									uKey.setPassphase(e);
								}
								break;
						}
						n++;
					}
					user.setPassword(uPasswd);
					user.setKey(uKey);
					userMap.put(user.getUsername(), user);
				} catch (Exception e) {
					e.printStackTrace();
					log.warn(e);
				}
			}
			reader.close();
		} catch (IOException e) {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
			log.error(e);
		}
	}
	
	
	private String getSubUrl(File dir, String name) throws MalformedURLException {
		File[] files = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.equalsIgnoreCase(name);
		    }
		});
		if (files != null && files.length > 0)
			return files[0].toURI().toURL().toString();
		return null;
	}
}

