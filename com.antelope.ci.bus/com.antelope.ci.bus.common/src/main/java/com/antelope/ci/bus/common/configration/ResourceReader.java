// com.antelope.ci.bus.common.ResourceReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.configration;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 资源文件读取
 * 支付本地化
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-1		上午9:40:42 
 */
public class ResourceReader extends BasicConfigrationReader {
	protected Locale locale;											// 指定本地化
	
	public ResourceReader() {
		super();
		locale = Locale.getDefault();	// 默认国家，与系统有关
	}
	
	public ResourceReader(String lang) throws CIBusException {
		super();
		validLocale(lang);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String)
	 */
	public void addConfig(String config) throws CIBusException {
		removeResource(config);
		ResourceBundle bundle = ResourceBundle.getBundle(config, locale);
		configMap.put(config, bundle);
		for (String key : bundle.keySet()) 
			props.put(key, bundle.getObject(key));
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.common.configration.BasicConfigrationReader#addConfig(java.lang.String, int)
	 */
	public void addConfig(String resource, int start) throws CIBusException {
		removeResource(resource);
		ResourceBundle bundle = ResourceBundle.getBundle(resource, locale);
		configMap.put(resource, bundle);
		for (String key : bundle.keySet()) {
			if (isAdd(key, start)) {
				props.put(key, bundle.getObject(key));
			}
		}
	}
	
	/*
	 * 验证给出的语言字符串是否为正确的
	 */
	private void validLocale(String lang) throws CIBusException {
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale l : locales) {
			if (l.equals("lang")) {
				locale = new Locale(lang);
				return;
			}
		}
		throw new CIBusException("");
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
	public void addConfig(String resource, ClassLoader classLoader) throws CIBusException {
		removeResource(resource);
		ResourceBundle bundle = ResourceBundle.getBundle(resource, locale, classLoader);
		configMap.put(resource, bundle);
		for (String key : bundle.keySet()) 
			props.put(key, bundle.getObject(key));
	}
	
	public Properties getIsolateProps(String config) {
		return null;
	}
}

