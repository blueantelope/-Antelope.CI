// com.antelope.ci.bus.BundleStarter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.JarLoadMethod;
import com.antelope.ci.bus.common.StringUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-26		下午2:07:03 
 */
class BundleExecutor {
	private BundleLoader loader;

	public BundleExecutor(BundleLoader loader) {
		this.loader = loader;
	}

	/**
	 * 执行bundle操作
	 * 包括install和start
	 * @param  
	 * @return void
	 * @throws
	 */
	public void execute() {
		try {
			if (loader.method == JarLoadMethod.INSTALL || loader.method == JarLoadMethod.START) {
				Bundle bundle = loader.context.installBundle(loader.jarFile.toURI().toString());
				if (loader.clsUrlList != null) {
					attachBundleClassUrl(bundle);
				}
				if (loader.method == JarLoadMethod.START) {
					startBundle(bundle);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 将lib_ext目录下的jar包附加到扩展bundle中
	 * bundle启动时，自动将这些jar的路径加载进classpath
	 */
	private void attachBundleClassUrl(Bundle bundle) {
		String ext_libs = StringUtil.convertUrlList(loader.clsUrlList, ",");
		bundle.getHeaders().put(Constants.BUNDLE_CLASSPATH, ext_libs);
//		bundle.getHeaders().put(BusConstants.BUS_EXT_LIBS, ext_libs);
	}
	
	/*
	 * 启动bundle
	 */
	private void startBundle(Bundle bundle) {
		try {
			loader.startLevel.setBundleStartLevel(bundle, loader.level);
			bundle.start();
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}
}

