// com.antelope.ci.bus.server.api.launcher.BusAPICondition.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api.launcher;

import com.antelope.ci.bus.server.api.base.SimpleBusAPI;
import com.antelope.ci.bus.server.common.BusCondition;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月7日		下午9:41:02 
 */
public class BusAPICondition extends BusCondition {
	public static final String DEFAULT_API = SimpleBusAPI.class.getName();
	protected String apiClass;
	
	public BusAPICondition() {
		super();
		init();
	}
	
	public BusAPICondition(ClassLoader classLoader) {
		super(classLoader);
		init();
	}
	
	private void init() {
		apiClass = DEFAULT_API;
	}
	
	public void setApiClass(String apiClass) {
		this.apiClass = apiClass;
	}
	
	public String getApiClass() {
		return this.apiClass;
	}
}
