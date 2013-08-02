// com.antelope.ci.bus.BundleStarterThread.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-2		下午9:44:28 
 */
public class BundleLoaderThread extends Thread {
	private BundleLoader loader;
	
	public BundleLoaderThread(BundleLoader loader) {
		this.loader = loader;
	}
	
	/**
	 * 
	 * (non-Javadoc)
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
			 		Bundle bundle = loader.context.installBundle(loader.jarFile.toURI().toString());
			 		loader.startLevel.setBundleStartLevel(bundle, loader.level);
			 		bundle.start();
			 		break;
			 	default:
			 		break;
			 }
		} catch (BundleException e) {
			e.printStackTrace();
		}
	 }
}

