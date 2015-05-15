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
public class BusContextProxy {
	private static final BusContextProxy contextProxy = new BusContextProxy();
	
	public static final BusContextProxy getContextProxy() {
		return contextProxy;
	}
	
	private BusContext context;
	private boolean init;
	private BusContextProxy() {
		init = false;
	}
	
	public void initContext(BusContext context) {
		if (!init) {
			this.context = context;
			init = true;
		}
	}
	
	public BusContext getContext() {
		return context;
	}
}
