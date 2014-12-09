// com.antelope.ci.bus.osgi.BusServiceInfo.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;

import org.osgi.framework.ServiceReference;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-6-23		下午6:14:13 
 */
public class BusServiceInfo {
	public String className;
	public Object service;
	public ServiceReference ref;

	public BusServiceInfo(String className, Object service, ServiceReference ref) {
		super();
		this.className = className;
		this.service = service;
		this.ref = ref;
	}
	
	public String getClassName() {
		return className;
	}

	public Object getService() {
		return service;
	}

	public ServiceReference getRef() {
		return ref;
	}
}

