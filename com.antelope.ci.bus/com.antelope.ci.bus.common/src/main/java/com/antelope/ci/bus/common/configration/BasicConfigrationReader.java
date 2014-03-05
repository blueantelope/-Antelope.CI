// com.antelope.ci.bus.common.configration.BasicConfigrationReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.configration;

import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 基本的配置reader
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-1		上午11:57:24 
 */
public abstract class BasicConfigrationReader {
	protected Map<String, Object> configMap;		// 配置文件hash表
	protected Properties props;								// 配置内容
	
	public BasicConfigrationReader() {
		init();
	}
	
	
	/*
	 * 初始化基本数据
	 */
	private void init() {
		configMap = new HashMap<String, Object>();	// 初始化资源文件hash表
		props = new Properties();										// 初始化配置内容
	}
	
	
	/**
	 * 增加资源文件到资源表中去
	 * @param  @param config
	 * @return void
	 * @throws CIBusException
	 */
	public abstract void addConfig(String config) throws CIBusException;
	
	/**
	 * 增加资源文件到资源表中去，用定义的classloader加载资源文件
	 * @param  @param config
	 * @param  @param classLoader
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	public abstract void addConfig(String config, ClassLoader classLoader) throws CIBusException;
	
	/**
	 * 增加资源记录
	 * 并且记录中的属性需要满足如果key为int型，
	 * 必须为大于start的值
	 * @param  @param config
	 * @param  @param start
	 * @return void
	 * @throws CIBusException
	 */
	public abstract void addConfig(String config, int start) throws CIBusException;
	
	/**
	 * 由输入流加载配置
	 * @param  @param in
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	public abstract void addInputStream(InputStream in) throws CIBusException;
	
	/**
	 * 由输入流加载配置
	 * 并且记录中的属性需要满足如果key为int型，
	 * 必须为大于start的值
	 * @param  @param in
	 * @param  @param start
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	public abstract void addInputStream(InputStream in, int start) throws CIBusException;
	
	public void removeConfig(String resource) throws CIBusException {
		for (String key : configMap.keySet()) {
			if (key.equals(resource)) {
				configMap.remove(key);
				Properties delProps = (Properties) configMap.get(key);
				for (Object dk : delProps.keySet()) 
					props.remove(dk);
			}
		}
	}
	
	/**
	 * 从资源表中删除资源
	 * @param  @param resource
	 * @return void
	 * @throws CIBusException
	 */
	public void removeResource(String resource) throws CIBusException {
		for (String key : configMap.keySet()) {
			if (key.equals(resource)) {
				configMap.remove(key);
				ResourceBundle bundle = (ResourceBundle) configMap.get(key);
				for (String bk : bundle.keySet()) 
					props.remove(bk);
			}
		}
	}
	
	/**
	 * 取得所有配置项
	 * @param  @return
	 * @return Properties
	 * @throws
	 */
	public Properties getProps() {
		return props;
	}
	
	
	/**
	 * 取得字符串配置项
	 * @param  @param key
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public String getString(String key) {
		return props.getProperty(key);
	}
	
	/**
	 * 取得int配置项
	 * @param  @param key
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return Integer
	 * @throws
	 */
	public Integer getInt(String key) throws CIBusException {
		try {
			return Integer.valueOf(props.getProperty(key));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 取得long配置项
	 * @param  @param key
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return Long
	 * @throws
	 */
	public Long getLong(String key) throws CIBusException {
		try {
			return Long.valueOf(props.getProperty(key));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 取得float配置项
	 * @param  @param key
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return Float
	 * @throws
	 */
	public Float getFloat(String key) throws CIBusException {
		try {
			return Float.valueOf(props.getProperty(key));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 取得float配置项
	 * @param  @param key
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return Double
	 * @throws
	 */
	public Double getDouble(String key) throws CIBusException {
		try {
			return Double.valueOf(props.getProperty(key));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 取得number配置项
	 * @param  @param key
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return Number
	 * @throws
	 */
	public Number getNumber(String key) throws CIBusException {
		try {
			return NumberFormat.getInstance().parse(props.getProperty(key));
		} catch (ParseException e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 取得bool配置项
	 * @param  @param key
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return Boolean
	 * @throws
	 */
	public Boolean getBoolean(String key) throws CIBusException {
		try {
			return Boolean.valueOf(props.getProperty(key)).booleanValue();
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 取得字符串配置项，如果没有取得就返回一个默认的结果
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public String getString(String key, String def) {
		return props.getProperty(key, def);
	}
	
	/**
	 * 取得int配置项，如果没有取得就返回一个默认的结果
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return Integer
	 * @throws
	 */
	public Integer getInt(String key, int def) {
		try {
			return Integer.valueOf(props.getProperty(key));
		} catch (Exception e) {
			return def;
		}
	}
	
	/**
	 * 取得long配置项，如果没有取得就返回一个默认的结果
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return Long
	 * @throws
	 */
	public Long getLong(String key, long def) {
		try {
			return Long.valueOf(props.getProperty(key));
		} catch (Exception e) {
			return def;
		}
	}
	
	/**
	 * 取得float配置项，如果没有取得就返回一个默认的结果
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return Float
	 * @throws
	 */
	public Float getFloat(String key, float def) {
		try {
			return Float.valueOf(props.getProperty(key));
		} catch (Exception e) {
			return def;
		}
	}
	
	/**
	 * 取得float配置项，如果没有取得就返回一个默认的结果
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return Double
	 * @throws
	 */
	public Double getDouble(String key, double def) {
		try {
			return Double.valueOf(props.getProperty(key));
		} catch (Exception e) {
			return def;
		}
	}
	
	/**
	 * 取得number配置项，如果没有取得就返回一个默认的结果
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return Number
	 * @throws
	 */
	public Number getNumber(String key, Number def) {
		try {
			return NumberFormat.getInstance().parse(props.getProperty(key));
		} catch (ParseException e) {
			return def;
		}
	}
	
	/**
	 * 取得boolean配置项，如果没有取得就返回一个默认的结果
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return Boolean
	 * @throws
	 */
	public Boolean getBoolean(String key, boolean def) {
		String value = props.getProperty(key);
		if (value != null)
			value.trim();
		if ("true".equalsIgnoreCase(value))
			return true;
		if ("false".equalsIgnoreCase(value))
			return false;
		
		return def;
	}
	
	/*
	 * code是否可加
	 * 如果是数字，必须是大于start
	 */
	protected boolean isAdd(String code, int start) {
		if (code != null && !"".equals(code)) {
			char first = code.charAt(0);
			if (first != '0')
				return true;
			String s = code.substring(1);
			try {
				int i = Integer.valueOf(s);
				if (i > start)
					return true;
				return false;
			} catch (Exception e) {
				return true;
			}
		}
		
		return false;
	}
}

