// com.antelope.ci.bus.framework.BusClassLoader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.framework;

import java.net.URL;
import java.net.URLClassLoader;


/**
 * bus定义的loader.url类加载器
 * 扩展于URLClassLoader
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-9		下午9:21:56 
 */
public class BusClassLoader extends URLClassLoader {

	public BusClassLoader(URL[] urls) {
		super(urls);
	}

	/**
	 * 查找类或资源
	 * @param  @param name
	 * @param  @param isClass
	 * @param  @return
	 * @param  @throws ClassNotFoundException
	 * @return Object
	 * @throws
	 */
	public Object findClassOrResource(String name, boolean isClass) throws ClassNotFoundException {
		if (isClass) {
			return super.loadClass(name);
		} else {
			return super.findResource(name);
		}
			
	}
}

