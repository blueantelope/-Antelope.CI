// com.antelope.ci.bus.BundleContextStarter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.startlevel.StartLevel;

import com.antelope.ci.bus.common.JarLoadMethod;


/**
 * osgi包加载内容
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-2		下午9:39:50 
 */
class BundleLoader {
	BundleContext context;
	File jarFile;
	StartLevel startLevel;
	int level;
	JarLoadMethod method;
	List<URL> clsUrlList = null;
	
	BundleLoader(BundleContext context, File jarFile, StartLevel startLevel, int level, JarLoadMethod method) {
		this.context = context;
		this.jarFile = jarFile;
		this.startLevel = startLevel;
		this.level = level;
		this.method = method;
	}
	
	BundleLoader(BundleContext context, File jarFile, StartLevel startLevel, int level, JarLoadMethod method, List<URL> clsUrlList) {
		this.context = context;
		this.jarFile = jarFile;
		this.startLevel = startLevel;
		this.level = level;
		this.method = method;
		this.clsUrlList = clsUrlList;
	}
}

