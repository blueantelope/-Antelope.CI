// com.antelope.ci.bus.osgi.BusActivatorHelperProxy.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月11日		下午12:55:24 
 */
public class BusActivatorContextProxy {
	private static final BusActivatorContextProxy contextProxy = new BusActivatorContextProxy();
	
	public static final BusActivatorContextProxy getContextProxy() {
		return contextProxy;
	}
	
	private BusActivatorContext context;
	private boolean init;
	private BusActivatorContextProxy() {
		init = false;
	}
	
	public void initContext(BusActivatorContext context) {
		if (!init) {
			this.context = context;
			init = true;
		}
	}
	
	public BusActivatorContext getContext() {
		return context;
	}
}
