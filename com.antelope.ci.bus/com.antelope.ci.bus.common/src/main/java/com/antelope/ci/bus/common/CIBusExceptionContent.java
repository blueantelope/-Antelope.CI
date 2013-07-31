// com.antelope.ci.bus.common.CIBusExceptionContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 异常的内容
 * 由配置文件定义，可以增加异常配置文件
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-7-31 下午2:11:08
 */
public class CIBusExceptionContent {
	// singleton
	private static final CIBusExceptionContent exContent = new CIBusExceptionContent();

	/**
	 * 异常资源定义单例
	 * @param  @return
	 * @return CIBusExceptionContent
	 * @throws
	 */
	public static final CIBusExceptionContent getExContent() {
		return exContent;
	}

	private Map<String, ResourceBundle> exMap; 		// 异常资源集合
	private Properties exProps; 									// 异常定义集合
	private static final int addStart = 1000;					// 增加定义异常的开始下标，只能从这以后开始定义，之前为系统保留
	private static final String default_content = "com.antelope.ci.bus.common.exception";				// 默认异常定义

	/*
	 * 构造函数
	 */
	private CIBusExceptionContent() {
		exMap = new HashMap<String, ResourceBundle>();
		exProps = new Properties();
		initContent();				// 初始化异常定义
	}
	
	/*
	 * 初始化异常定义
	 * 增加默认定义
	 */
	private void initContent() {
		ResourceBundle bundle = ResourceBundle.getBundle(default_content);
		exMap.put(default_content, bundle); 		// 异常资源集合中增加
		for (String code : bundle.keySet()) {
			exProps.put(code, bundle.getString(code)); // 异常代码加入到异常定义
		}
	}

	/**
	 * 增加异常定义资源
	 * @param  @param define
	 * @return void
	 * @throws
	 */
	public void addContent(String content) {
		removeContent(content); 				// 首先移除重复的异常资源定义
		ResourceBundle bundle = ResourceBundle.getBundle(content);
		exMap.put(content, bundle); 			// 异常资源集合中增加
		for (String code : bundle.keySet()) {
			if (isAdd(code))
				exProps.put(code, bundle.getString(code)); // 异常代码加入到异常定义
		}
	}
	
	/*
	 * 异常定义的code是否可加
	 * 如果是数字，必须是大于1000
	 */
	private boolean isAdd(String code) {
		if (code != null && !"".equals(code)) {
			char first = code.charAt(0);
			if (first != '0')
				return true;
			String s = code.substring(1);
			try {
				int i = Integer.valueOf(s);
				if (i > addStart)
					return true;
				return false;
			} catch (Exception e) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 移除一个异常定义资源
	 * @param  @param define
	 * @return void
	 * @throws
	 */
	public void removeContent(String content) {
		for (String excontent : exMap.keySet()) {
			if (excontent.equals(content)) {
				exMap.remove(excontent); // 异常资源集合中移除
				ResourceBundle bundle = exMap.get(exMap);
				for (String code : bundle.keySet()) {
					exProps.remove(code); // 异常定义中移除
				}
				break;
			}
		}
	}

	/**
	 * 由异常代码取得异常定义内容
	 * @param  @param code
	 * @param  @return
	 * @return String
	 * @throws
	 */
	String getException(String code) {
		return exProps.getProperty(code);
	}
}
