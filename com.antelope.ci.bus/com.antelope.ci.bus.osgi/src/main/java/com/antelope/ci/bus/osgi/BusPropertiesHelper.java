// com.antelope.ci.bus.osgi.BusPropertiesReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;

import java.net.URL;
import java.util.Properties;

import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.configration.URLResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月5日		下午4:05:29 
 */
public class BusPropertiesHelper {
	/* ssh key类型 */
	public enum KT { 
		FIXED("fixed"), // 固定指定方式，指定已存在的key
		DYNAMIC("dynamic"); // 动态生成方式，自己生成key
		
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
			
			return KT.FIXED;
		}
	}
	
	private BundleContext m_context;
	private static final String PROPS_FILE = "/META-INF/bus.properties";
	/* 
	 * bundle configuration
	 */
	private static final String LOAD = "load";
	private static final String LOAD_LEVEL = "load.level";
	private static final String BUNDLE_LEVEL = "bundle.level";
	private static final String BUS_LOAD_SERVICES = "bus.load.services";
	private static final String BUS_BANNER = "bus.banner";
	/* 
	 * server configuration
	 */
	private static final String BUS_SERVER_SWITCHER = "bus.server.switcher";
	private static final String SERVER_HOST = "bus.server.host";
	private static final String SERVER_PORT = "bus.server.port";
	private static final String KEY_TYPE = "bus.key.type";
	private static final String KEY_NAME = "bus.key.name";
	
	private Properties props; // 属性
	public BusPropertiesHelper(BundleContext m_context) throws CIBusException {
		this.m_context = m_context;
		props = load();
	}
	
	public Properties getAll() {
		return props;
	}
	
	public void refresh() throws CIBusException {
		props = load();
	}
	
	/*
	 * 加载bundle默认配置文件bus.properties
	 */
	private Properties load() throws CIBusException {
		URL props_url = m_context.getBundle().getResource(PROPS_FILE);
		if (props_url != null) {
			BasicConfigrationReader reader = new URLResourceReader();
			reader.addConfig(props_url.toString());
			return reader.getProps();
		}
		
		return new Properties();
	}
	
	public String getBanner() {
		return PropertiesUtil.getString(props, BUS_BANNER, BusOsgiConstant.DEF_BANNER);
	}
	
	public boolean switchServer() {
		return PropertiesUtil.getSwitcher(props, BUS_SERVER_SWITCHER, BusOsgiConstant.DEF_SERVER_SWITCHER);
	}
	
	public String getServerHost() {
		return PropertiesUtil.getString(props, SERVER_HOST, BusOsgiConstant.DEF_SERVER_HOST);
	}
	
	public int getServerPort() {
		return PropertiesUtil.getInt(props, SERVER_PORT, BusOsgiConstant.DEF_SERVER_PORT);
	}
	
	public KT getKeyType() {
		return KT.fromName(PropertiesUtil.getString(props, KEY_TYPE, BusOsgiConstant.DEF_KEY_TYPE));
	}
	
	public String getKeyName() {
		return PropertiesUtil.getString(props, KEY_NAME, BusOsgiConstant.DEF_KEY_NAME);
	}
}
