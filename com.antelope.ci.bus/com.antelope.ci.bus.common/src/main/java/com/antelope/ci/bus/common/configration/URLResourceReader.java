// com.antelope.ci.bus.common.configration.MultiPropertiesReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.configration;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * url方式属性配置文件读取
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-8		下午3:06:39 
 */
public class URLResourceReader extends BasicConfigrationReader {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String)
	 */
	@Override
	public void addConfig(String config) throws CIBusException {
		removeConfig(config);
		try {
			URL url = new URL(config);
			InputStream input = url.openConnection().getInputStream();
			Properties res_props= new Properties();
			res_props.load(input);
			configMap.put(config, res_props);
			props.putAll(res_props);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String, int)
	 */
	@Override
	public void addConfig(String config, int start) throws CIBusException {
		removeConfig(config);
		try {
			URL url = new URL(config);
			InputStream input = url.openConnection().getInputStream();
			Properties res_props= new Properties();
			res_props.load(input);
			configMap.put(config, res_props);
			for (Object k : res_props.keySet()) {
				String key = (String) k;
				if (isAdd(key, start)) {
					props.put(key, res_props.getProperty(key));
				}
			}
		} catch (Exception e) {
			throw new CIBusException("", e);
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

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String, java.lang.ClassLoader)
	 */
	@Override
	public void addConfig(String config, ClassLoader classLoader) throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

}

