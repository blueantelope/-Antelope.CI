// com.antelope.ci.bus.server.common.Condition.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.common;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		下午4:34:08 
 */
public class BusCondition {
	protected Object[] contexts;
	protected ClassLoader classLoader;
	
	public BusCondition() {
		this(null, null);
	}
	
	public BusCondition(ClassLoader classLoader) {
		this(null, classLoader);
	}
	
	public BusCondition(Object[] contexts, ClassLoader classLoader) {
		super();
		this.contexts = contexts;
		init(classLoader);
	}
	
	protected void init(ClassLoader classLoader) {
		if (classLoader == null) {
			this.classLoader = Thread.currentThread().getContextClassLoader();
		} else {
			this.classLoader = classLoader;
		}
	}
	
	// getter and setter
	public Object[] getContexts() {
		return contexts;
	}
	public void setContexts(Object[] contexts) {
		this.contexts = contexts;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
}
