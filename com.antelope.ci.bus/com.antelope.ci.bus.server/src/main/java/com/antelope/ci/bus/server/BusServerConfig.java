// com.antelope.ci.bus.server.BusServerConfig.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server;

import java.net.URL;

import com.antelope.ci.bus.common.PROTOCOL;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusOsgiConstant;
import com.antelope.ci.bus.osgi.BusPropertiesHelper;
import com.antelope.ci.bus.osgi.BusPropertiesHelper.KT;
import com.antelope.ci.bus.osgi.CommonBusActivator;


/**
 * bus server 配置项
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-7		下午5:46:51 
 */
public class BusServerConfig {
	private PROTOCOL proto;
	private boolean switcher;
	private String host;
	private int port;
	private KT kt;
	private String key_name;
	private URL key_url;
	private String welcome_banner;
	
	public BusServerConfig() {
		super();
		init();
	}
	
	public BusServerConfig(PROTOCOL proto) {
		this();
		this.proto = proto;
	}
	
	private void init() {
		String host = BusOsgiConstant.DEF_SERVER_HOST;
		int port = BusOsgiConstant.DEF_SERVER_PORT;
		KT kt = KT.DYNAMIC;
		String key_name = BusOsgiConstant.DEF_KEY_NAME;
		String welcome_banner = BusOsgiConstant.DEF_WELCOME_BANNER;
		proto = PROTOCOL.TCP;
	}
	
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
	
	public PROTOCOL getProto() {
		return proto;
	}
	
	public boolean isSwitcher() {
		return switcher;
	}
	public void setSwitcher(boolean switcher) {
		this.switcher = switcher;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public boolean anyLocalAddress() {
		if (StringUtil.empty(host) || "0.0.0.0".equals(host.trim()))
			return true;
		
		return false;
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

	/**
	 * 取得属性中的server配置部分
	 * 拼装成server的配置项类
	 * @param  @param props
	 * @param  @return
	 * @return BusServerConfig
	 * @throws CIBusException 
	 */
	public static BusServerConfig load() throws CIBusException {
		BusServerConfig config = new BusServerConfig();
		BusPropertiesHelper helper = new BusPropertiesHelper(CommonBusActivator.getContext());
		config.setSwitcher(helper.switchServer());
		config.setHost(helper.getServerHost());
		config.setPort(helper.getServerPort());
		config.setKt(helper.getKeyType());
		config.setKey_name(helper.getKeyName());
		config.setWelcome_banner(helper.getWelcomeBanner());
		
		return config;
	}
						
}