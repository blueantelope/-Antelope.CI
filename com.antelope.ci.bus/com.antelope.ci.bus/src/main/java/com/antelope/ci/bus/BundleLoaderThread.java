// com.antelope.ci.bus.BundleStarterThread.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-8-2 下午9:44:28
 */
public class BundleLoaderThread extends Thread {
	private BundleLoader loader;

	public BundleLoaderThread(BundleLoader loader) {
		this.loader = loader;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			switch (loader.method) {
				case INSTALL:
					loader.context.installBundle(loader.jarFile.toURI().toString());
					break;
				case START:
					try {
						if (loader.clsUrlList == null) {
							startBundle();
						} else {
							StartByClassLoader();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 使用指定的classLoader来运行
	 */
	private void StartByClassLoader() {
		JarFile jar = null;
		try {
			jar = new JarFile(loader.jarFile);
			Manifest mf = jar.getManifest();
			String className = mf.getMainAttributes().getValue("Bundle-Activator");
			if (className != null) {
				Bundle bundle = loader.context.installBundle(loader.jarFile.toURI().toString());
				List<URL> bundleClsUrl = new ArrayList<URL>();
				bundleClsUrl.addAll(loader.clsUrlList);
				bundleClsUrl.add(loader.jarFile.toURI().toURL());
//				Util.loadJarToBundle(bundle, bundleClsUrl);
				URLClassLoader bundleClassLoader = new URLClassLoader(bundleClsUrl.toArray(new URL[bundleClsUrl.size()]));
				BundleActivator activator = (BundleActivator) bundleClassLoader.loadClass(className).newInstance();
				loader.startLevel.setBundleStartLevel(bundle, loader.level);
				activator.start(loader.context);
			} else {
				startBundle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jar != null) {
				try {
					jar.close();
				} catch (IOException e) { }
			}
		}
	}
	
	/*
	 * 启动运行bundle
	 */
	private void startBundle() {
		try {
			Bundle bundle = loader.context.installBundle(loader.jarFile.toURI().toString());
			loader.startLevel.setBundleStartLevel(bundle, loader.level);
			bundle.start();
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}
}
