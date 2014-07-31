// com.antelope.ci.bus.common.configration.PropertiesReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.configration;

import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-28		下午4:58:51 
 */
public class IsolateResourceReader extends ResourceReader {
	protected Map<String, Properties> propsMap;		// 配置文件hash表
	
	public IsolateResourceReader() {
		super();
		propsMap = new ConcurrentHashMap<String, Properties>();
	}
	
	@Override public void addConfig(String config) throws CIBusException {
		if (!propsMap.containsKey(config)) {
			ResourceBundle bundle = ResourceBundle.getBundle(config, locale);
			Properties props = new Properties();
			for (String key : bundle.keySet()) 
				props.put(key, bundle.getObject(key));
			propsMap.put(config, props);
		}
	}
	
	@Override public void addConfig(String config, ClassLoader classLoader) throws CIBusException {
		if (!propsMap.containsKey(config)) {
			ResourceBundle bundle = ResourceBundle.getBundle(config, locale, classLoader);
			Properties props = new Properties();
			for (String key : bundle.keySet()) 
				props.put(key, bundle.getObject(key));
			propsMap.put(config, props);
		}
	}
	
	@Override public void addConfig(String resource, int start) throws CIBusException {
		
	}
	
	@Override public Properties getIsolateProps(String config) {
		return propsMap.get(config);
	}
}

