// com.antelope.ci.bus.common.configration.CnfFileReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.configration;

import java.io.FileInputStream;
import java.util.Properties;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 配置文件读取
 *
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
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addResource(java.lang.String)
	 */
	@Override
	public void addResource(String resource) throws CIBusException {
		removeResource(resource);
		Properties conf = new Properties();
		try {
			conf.load(new FileInputStream(resource));
		} catch (Exception e) {
			throw new CIBusException("", e);
		} 
		resourceMap.put(resource, conf);
		for (Object key : conf.keySet()) 
			props.put(key, conf.get(key));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addResource(java.lang.String, int)
	 */
	@Override
	public void addResource(String resource, int start) throws CIBusException {
		removeResource(resource);
		Properties conf = new Properties();
		try {
			conf.load(new FileInputStream(resource));
		} catch (Exception e) {
			throw new CIBusException("", e);
		} 
		resourceMap.put(resource, conf);
		for (Object key : conf.keySet()) {
			if (isAdd(key.toString(), start)) {
				props.put(key, conf.get(key));
			}
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#removeResource(java.lang.String)
	 */
	@Override
	public void removeResource(String resource) throws CIBusException {
		for (String key : resourceMap.keySet()) {
			if (key.equals(resource)) {
				resourceMap.remove(key);
				Properties prop = (Properties) resourceMap.get(key);
				for (Object bk : prop.keySet()) 
					props.remove(bk);
			}
		}
	}

	

}

