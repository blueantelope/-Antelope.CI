// com.antelope.ci.bus.framework.test.inner.TestBundleActivator.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.framework.test.inner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-9-30 下午4:42:31
 */
public class TestBundleActivator implements BundleActivator {
	public void start(BundleContext context) throws Exception {
		Thread.sleep(200);
	}

	public void stop(BundleContext context) throws Exception {
		Thread.sleep(200);
	}
}
