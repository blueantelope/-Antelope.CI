// com.antelope.ci.bus.server.service.ServiceManager.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.ResourceUtil;


/**
 * 对外提供servcie管理
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午12:31:45 
 */
public class ServicePublisher {
	private static final Logger log = Logger.getLogger(ServicePublisher.class);
	private static List<String> serviceList = new ArrayList<String>();

	public static void publish(final BundleContext m_context) {
		new Thread() {
			public void run() {
				while (true) {
					String cls_name = "";
					try {
						List<String>  classList = ResourceUtil.getClassUrl("com.antelope.ci.bus.server.service");
						List<String> regList = new ArrayList<String>();
						for (String cls : classList) {
							cls_name = cls;
							boolean isReg = true;
							for (String service : serviceList) {
								if (cls.equals(service)) {
									isReg = false;
									break;
								}
							}
							if (isReg) {
								Class clazz = Class.forName(cls);
								if (Service.class.isAssignableFrom(clazz)) {
									Service service = (Service) clazz.newInstance();
									service.register(m_context);
									regList.add(cls);
									log.info("add service :" + cls_name);
								}
							}
						}
						serviceList.addAll(regList);
					} catch (Exception e) {
						log.warn("problem for add service :" + cls_name);
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}
}

