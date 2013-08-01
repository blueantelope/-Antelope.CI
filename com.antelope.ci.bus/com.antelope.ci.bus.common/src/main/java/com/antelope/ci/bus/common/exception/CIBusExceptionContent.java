// com.antelope.ci.bus.common.CIBusExceptionContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common.exception;

import com.antelope.ci.bus.common.configration.ResourceReader;


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

	private ResourceReader exReader; 						// 异常资源集合
	private static final int addStart = 1000;					// 增加定义异常的开始下标，只能从这以后开始定义，之前为系统保留
	private static final String default_content = "com.antelope.ci.bus.common.exception";				// 默认异常定义

	/*
	 * 构造函数
	 */
	private CIBusExceptionContent() {
		exReader =new ResourceReader();
		initContent();				// 初始化异常定义
	}
	
	/*
	 * 初始化异常定义
	 * 增加默认定义
	 */
	private void initContent() {
		try {
			exReader.addResource(default_content);
		} catch (CIBusException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加异常定义资源
	 * @param  @param define
	 * @return void
	 * @throws
	 */
	public void addContent(String content) {
		try {
			exReader.addResource(content, addStart);
		} catch (CIBusException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 移除一个异常定义资源
	 * @param  @param define
	 * @return void
	 * @throws
	 */
	public void removeContent(String content) {
		try {
			exReader.removeResource(content);
		} catch (CIBusException e) {
			e.printStackTrace();
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
		return exReader.getString(code);
	}
}
