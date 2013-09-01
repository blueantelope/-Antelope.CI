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
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-2		下午6:20:01 
 */
public class JarBusProperty {
	private static final String BUS_PROPS = "META-INF/bus.properties";
	private JarLoadMethod load;						// 装载方式: install, start
	private int startLevel;								// 启动级别
	private String services;								// 需要加载的osgi serivce列表
	private String system_lib_props;				// 需要加入classLoader路径中的系统参数，形如System.getProperty("java.home")
	private String load_jars;							// 需要自定义加载进classLoader中的jar路径, url方式
	
	// getter and setter
	public JarLoadMethod getLoad() {
		return load;
	}
	public void setLoad(JarLoadMethod load) {
		this.load = load;
	}
	/**
	 * 由装载方式的字符转换为enum表示，
	 * 无匹配时，使用IGNORE，即没有装载动作
	 * @param  @param loadValue
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
	public String getSystem_lib_props() {
		return system_lib_props;
	}
	public void setSystem_lib_props(String system_lib_props) {
		this.system_lib_props = system_lib_props;
	}
	public String getLoad_jars() {
		return load_jars;
	}
	public void setLoad_jars(String load_jars) {
		this.load_jars = load_jars;
	}
	
	/**
	 * 解析系统lib定义参数
	 * 得到此定义下的所有的jar
	 * @param  @return
	 * @return List<URL>
	 * @throws
	 */
	public List<URL> sysLibPropToUrl() {
		List<URL> urlList = new ArrayList<URL>();
		if (system_lib_props != null) {
			for (String lib_prop : system_lib_props.split(",")) {
				urlList.addAll(getLibraryJar(lib_prop.trim().toLowerCase()));
			}
		}
		
		return urlList;
	}
	
	/**
	 * 定义的加载jar合并到url列表中
	 * @param  @return
	 * @return List<URL>
	 * @throws
	 */
	public List<URL> mergeLoadJars() {
		List<URL> urlList = new ArrayList<URL>();
		if (load_jars != null) {
			for (String load_jar : load_jars.split(",")) {
				try {
					urlList.add(new URL(load_jar));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return urlList;
	}
	
	/**
	 * 取得加载此jar的依赖库环境
	 * @param  @return
	 * @return List<URL>
	 * @throws
	 */
	public List<URL> getEnvUrl() {
		List<URL> urlList = new ArrayList<URL>();
		urlList.addAll(sysLibPropToUrl());
		urlList.addAll(mergeLoadJars());
		return urlList;
	}
	
	/*
	 * 取得系统属性定义下的所有jar，并且以url列表的形式返回
	 */
	private List<URL> getLibraryJar(String sysmte_property) {
		List<URL> urlList = new ArrayList<URL>();
		String lib_pathes = System.getProperty(sysmte_property);
		if (lib_pathes != null) {
			for (String lib_path : lib_pathes.split(":")) {
				if (!".".equals(lib_path)) {
					urlList.addAll(FileUtil.getAllJar(lib_path));
				}
			}
		}
		
		return urlList;
	}
	
	  /**
     * 读取jar中配置文件
     * jar文件路径
     * @param  @param path
     * @param  @return
     * @return Properties
     * @throws CIBusException
     */
    public static JarBusProperty readJarBus(String path) throws CIBusException {
    		return readJarBus(new JarResourceReader(path));
    }
    
    /**
     * 读取jar中配置文件
     * jar文件实体
     * @param  @param jarFile
     * @param  @return
     * @param  @throws CIBusException
     * @return JarBusProperty
     * @throws
     */
    public static JarBusProperty readJarBus(File jarFile) throws CIBusException {
		return readJarBus(new JarResourceReader(jarFile));
    }
    
    /*
     * 由jar资源reader读取资源文件
     */
    private static JarBusProperty readJarBus(JarResourceReader reader) throws CIBusException {
    		reader.addResource(BUS_PROPS);
		JarBusProperty busProperty = new JarBusProperty();
		busProperty.setLoad(reader.getString(BusConstants.JAR_LOAD));
		busProperty.setStartLevel(reader.getInt(BusConstants.JAR_START_LEVEL));
		busProperty.setSystem_lib_props(reader.getString(BusConstants.JAR_SYSTEM_PROP));
		busProperty.setLoad_jars(reader.getString(BusConstants.JAR_LOADER_LIST));
		busProperty.setServices(reader.getString(BusConstants.JAR_SERVICES));
		
		return busProperty;
    }
}

