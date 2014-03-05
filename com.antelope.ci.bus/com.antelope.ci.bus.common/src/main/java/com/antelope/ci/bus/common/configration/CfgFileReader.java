// com.antelope.ci.bus.common.configration.CnfFileReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.configration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 配置文件读取
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-1		下午12:02:50 
 */
public class CfgFileReader extends BasicConfigrationReader {
	public CfgFileReader() {
		super();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String)
	 */
	@Override
	public void addConfig(String config) throws CIBusException {
		removeConfig(config);
		Properties conf = new Properties();
		try {
			conf.load(new FileInputStream(config));
		} catch (Exception e) {
			throw new CIBusException("", e);
		} 
		configMap.put(config, conf);
		for (Object key : conf.keySet()) 
			props.put(key, conf.get(key));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String, int)
	 */
	@Override
	public void addConfig(String config, int start) throws CIBusException {
		removeConfig(config);
		Properties conf = new Properties();
		try {
			conf.load(new FileInputStream(config));
		} catch (Exception e) {
			throw new CIBusException("", e);
		} 
		configMap.put(config, conf);
		for (Object key : conf.keySet()) {
			if (isAdd(key.toString(), start)) {
				props.put(key, conf.get(key));
			}
		}
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

