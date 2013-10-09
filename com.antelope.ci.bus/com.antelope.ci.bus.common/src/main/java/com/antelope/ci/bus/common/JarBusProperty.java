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
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-8-2 下午6:20:01
 */
public class JarBusProperty {
	private static final String BUS_PROPS = "META-INF/bus.properties";
	private JarLoadMethod load; // 装载方式: install, start
	private int startLevel; // 启动级别
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

	public int getStartLevel() {
		return startLevel;
	}

	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
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
		reader.addResource(BUS_PROPS);
		JarBusProperty busProperty = new JarBusProperty();
		busProperty.setLoad(reader.getString(BusConstants.JAR_LOAD));
		busProperty.setStartLevel(reader.getInt(BusConstants.JAR_START_LEVEL));
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
					DebugUtil.assert_exception(e);
				}
			}
		}

		return urlList;
	}
}
