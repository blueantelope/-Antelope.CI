// com.antelope.ci.bus.common.configration.JarResourceReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.configration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * Jar资源文件reader
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-2		下午9:12:04 
 */
public class JarResourceReader extends BasicConfigrationReader {
	private JarFile jarFile;				// jar文件
	
	/*
	 * 由jar文件建立jar实体
	 */
	public JarResourceReader(File jar) throws CIBusException {
		super();
		try {
			jarFile = new JarFile(jar);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 由jar路径建立jar实体
	 */
	public JarResourceReader(String jarPath) throws CIBusException {
		this(new File(jarPath));
	}
	
	/**
	 * 这里的resource指的是jar里的资源文件
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String)
	 */
	@Override
	public void addConfig(String config) throws CIBusException {
		JarEntry entry = jarFile.getJarEntry(config);
		Properties jarProps = new Properties();
		try {
			jarProps.load(jarFile.getInputStream(entry));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
		configMap.put(config, jarProps);
		for (Object key : jarProps.keySet()) 
			props.put(key, jarProps.get(key));
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String, int)
	 */
	@Override
	public void addConfig(String config, int start) throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addInputStream(java.io.InputStream)
	 */
	@Override
	public void addInputStream(InputStream in) throws CIBusException {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addInputStream(java.io.InputStream, int)
	 */
	@Override
	public void addInputStream(InputStream in, int start) throws CIBusException {
		
	}

	@Override
	public void addConfig(String config, ClassLoader classLoader)
			throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

}

