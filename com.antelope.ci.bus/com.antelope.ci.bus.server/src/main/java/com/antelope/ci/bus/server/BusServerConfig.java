// com.antelope.ci.bus.server.BusServerConfig.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.net.URL;
import java.util.Properties;

import com.antelope.ci.bus.common.PropertiesUtil;


/**
 * bus server 配置项
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-7		下午5:46:51 
 */
public class BusServerConfig {
	/* ssh key类型 */
	public enum KT { 
		STATIC("static"),							// 静态方式，指定已存在的key
		DYNAMIC("dynamic");					// 动态方式，自己生成key
		
		private String name;
		private KT(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public String toString() {
			return name;
		}
		
		public static KT fromName(String name) {
			for (KT type : KT.values() ) {
				if (type.getName().equalsIgnoreCase(name.trim()))
					return type;
			}
			
			return KT.STATIC;
		}
	}
	
	private int port = 9426;
	private KT kt = KT.STATIC;
	private String key_name = "com.antelope.ci.bus.key";
	private URL key_url = null;
	private String welcome_banner = "SSH-2.0-CIBUS";
	
	// getter and setter
	public KT getKt() {
		return kt;
	}
	public void setKt(KT kt) {
		this.kt = kt;
	}
	public String getKey_name() {
		return key_name;
	}
	public void setKey_name(String key_name) {
		this.key_name = key_name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getWelcome_banner() {
		return welcome_banner;
	}
	public void setWelcome_banner(String welcome_banner) {
		this.welcome_banner = welcome_banner;
	}
	public URL getKey_url() {
		return key_url;
	}
	public void setKey_url(URL key_url) {
		this.key_url = key_url;
	}


	/*
	 * 读配置文件
	 * 
	 */
	private static final String SERVER_PORT						= "bus.server.port";
	private static final int DEF_SERVER_PORT 					= 9426;
	private static final String KEY_TYPE							= "bus.key.type";
	private static final String DEF_KEY_TYPE					= "static";
	private static final String KEY_NAME							= "bus.key.name";
	private static final String DEF_KEY_NAME					= "com.antelope.bus.ci.key";
	private static final String WELCOME_BANNER				= "bus.welcome.banner";
	private static final String DEF_WELCOME_BANNER		= "SSH-2.0-CIBUS";
	
	
	/**
	 * 取得属性中的server配置部分
	 * 拼装成server的配置项类
	 * @param  @param props
	 * @param  @return
	 * @return BusServerConfig
	 * @throws
	 */
	public static BusServerConfig fromProps(Properties props) {
		BusServerConfig config = new BusServerConfig();
		config.setPort(
				PropertiesUtil.getInt(props, SERVER_PORT, DEF_SERVER_PORT)
		);
		config.setKt(
				KT.fromName(PropertiesUtil.getString(props, KEY_TYPE, DEF_KEY_TYPE))
		);
		config.setKey_name(
				PropertiesUtil.getString(props, KEY_NAME, DEF_KEY_NAME)
		);
		config.setWelcome_banner(
				PropertiesUtil.getString(props, WELCOME_BANNER, DEF_WELCOME_BANNER)
		);
		
		return config;
	}
						
}

