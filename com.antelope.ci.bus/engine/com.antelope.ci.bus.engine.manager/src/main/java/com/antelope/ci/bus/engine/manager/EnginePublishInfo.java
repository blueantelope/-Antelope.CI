// com.antelope.ci.bus.engine.manager.EnginePublishInfo.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年12月8日		下午5:31:52 
 */
public class EnginePublishInfo {
	private boolean checkPart;
	private String partClasspath;
	private boolean checkService;
	private String serviceClasspath;
	
	public EnginePublishInfo(boolean checkPart, String partClasspath,
			boolean checkService, String serviceClasspath) {
		super();
		this.checkPart = checkPart;
		this.partClasspath = partClasspath;
		this.checkService = checkService;
		this.serviceClasspath = serviceClasspath;
	}
	
	public static EnginePublishInfo createPartPublish(String partClasspath) {
		return new EnginePublishInfo(true, partClasspath, false, null);
	}
	
	public static EnginePublishInfo createServicePublish(String serviceClasspath) {
		return new EnginePublishInfo(false, null, true, serviceClasspath);
	}
	
	// getter and setter
	public boolean isCheckPart() {
		return checkPart;
	}
	public void setCheckPart(boolean checkPart) {
		this.checkPart = checkPart;
	}
	
	public String getPartClasspath() {
		return partClasspath;
	}
	public void setPartClasspath(String partClasspath) {
		this.partClasspath = partClasspath;
	}
	
	public boolean isCheckService() {
		return checkService;
	}
	public void setCheckService(boolean checkService) {
		this.checkService = checkService;
	}

	public String getServiceClasspath() {
		return serviceClasspath;
	}
	public void setServiceClasspath(String serviceClasspath) {
		this.serviceClasspath = serviceClasspath;
	}
}