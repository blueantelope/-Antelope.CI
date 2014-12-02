// com.antelope.ci.bus.common.JarBusProperty.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.configration.JarResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * bus.properties中的定义
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * load.level:
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		1-100:	preserve
		
		101-200: com.antelope.ci.bus.engine
			101-120: preserve
			121-140: com.antelope.ci.bus.engine.model
				121-129: part
				130: com.antelope.ci.bus.engine.model
				131-140: service
			141-160: com.antelope.ci.bus.engine.access
				141-149: part
				150: com.antelope.ci.bus.engine.access
				151-160: service
			161-180: com.antelope.ci.bus.engine.manager
				161-169: part
				170: com.antelope.ci.bus.engine.manager
				171-180: service
			181-200: preserve
				
		201-300: com.antelope.ci.bus.ext
			201-220: preserve
			221-250: com.antelope.ci.bus.server
				221-234: part
				235: com.antelope.ci.bus.server
				236-250: service
			251-280: com.antelope.ci.bus.portal
				251-264: part
				265: com.antelope.ci.bus.portal
				266-280: service
			281-300: preserve
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * bundle.level:
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 		1-5:	preserve

		6-25: com.antelope.ci.bus.engine
			6-7: preserve
			8-12: com.antelope.ci.bus.engine.model
				8-9: part
				10: com.antelope.ci.bus.engine.model
				11-12: service
			13-17: com.antelope.ci.bus.engine.access
				13-14: part
				15: com.antelope.ci.bus.engine.access
				16-17: service
			18-22: com.antelope.ci.bus.engine.manager
				18-19: part
				20: com.antelope.ci.bus.engine.manager
				21-22: service
			23-25: preserve
				
		26-45: com.antelope.ci.bus.ext
			26-28: preserve
			29-35: com.antelope.ci.bus.server
				29-31: part
				32: com.antelope.ci.bus.server
				33-35: service
			36-42: com.antelope.ci.bus.portal
				36-38: part
				39: com.antelope.ci.bus.portal
				40-42: service
			43-45: preserve
			
		46-50: preserve
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @author blueantelope
 * @version 0.1
 * @Date 2013-8-2 下午6:20:01
 */
public class JarBusProperty {
	private static final String BUS_PROPS = "META-INF/bus.properties";
	private JarLoadMethod load; // 装载方式: install, start
	private int bundleLevel; // sogi bundle start level
	private int loadLevel; // 加载级别
	private String services; // 需要加载的osgi serivce列表
	private List<URL> loaderUrlList; // 加载的url列表

	// getter and setter
	public JarLoadMethod getLoad() {
		return load;
	}
	public void setLoad(JarLoadMethod load) {
		this.load = load;
	}
	/**
	 * 由装载方式的字符转换为enum表示， 无匹配时，使用IGNORE，即没有装载动作
	 * 
	 * @param @param loadValue
	 * @return void
	 * @throws
	 */
	public void setLoad(String loadValue) {
		this.load = JarLoadMethod.toLoadMethod(loadValue);
		if (null == this.load)
			this.load = JarLoadMethod.IGNORE;
	}

	public int getLoadLevel() {
		return loadLevel;
	}
	public void setLoadLevel(int loadLevel) {
		this.loadLevel = loadLevel;
	}
	

	public int getBundleLevel() {
		return bundleLevel;
	}
	public void setBundleLevel(int bundleLevel) {
		this.bundleLevel = bundleLevel;
	}

	public String getServices() {
		return services;
	}
	public void setServices(String services) {
		this.services = services;
	}

	public List<URL> getLoaderUrlList() {
		return loaderUrlList;
	}
	public void setLoaderUrlList(List<URL> loaderUrlList) {
		this.loaderUrlList = loaderUrlList;
	}

	/**
	 * 读取jar中配置文件 jar文件路径
	 * 
	 * @param @param path
	 * @param @return
	 * @return Properties
	 * @throws CIBusException
	 */
	public static JarBusProperty readJarBus(String path) throws CIBusException {
		return readJarBus(new JarResourceReader(path));
	}

	/**
	 * 读取jar中配置文件 jar文件实体
	 * 
	 * @param @param jarFile
	 * @param @return
	 * @param @throws CIBusException
	 * @return JarBusProperty
	 * @throws
	 */
	public static JarBusProperty readJarBus(File jarFile) throws CIBusException {
		return readJarBus(new JarResourceReader(jarFile));
	}

	/*
	 * 由jar资源reader读取资源文件
	 */
	private static JarBusProperty readJarBus(JarResourceReader reader)
			throws CIBusException {
		reader.addConfig(BUS_PROPS);
		JarBusProperty busProperty = new JarBusProperty();
		busProperty.setLoad(reader.getString(BusConstants.JAR_LOAD));
		busProperty.setLoadLevel(reader.getInt(BusConstants.JAR_LOAD_LEVEL));
		busProperty.setBundleLevel(reader.getInt(BusConstants.JAR_BUNDLE_LEVEL));

		busProperty.setLoaderUrlList(parseLoaderUrl(reader.getString(BusConstants.JAR_LOADER_URL, null)));
		busProperty.setServices(reader.getString(BusConstants.JAR_SERVICES));

		return busProperty;
	}

	/*
	 * 解析ur列表字符串，分割符为逗号
	 */
	private static List<URL> parseLoaderUrl(String urls) {
		List<URL> urlList = new ArrayList<URL>();
		if (urls != null && urls.length() > 0) {
			for (String url : urls.split(",")) {
				try {
					urlList.add(new URL(url.toString()));
				} catch (MalformedURLException e) {
					DevAssistant.assert_exception(e);
				}
			}
		}

		return urlList;
	}
}
