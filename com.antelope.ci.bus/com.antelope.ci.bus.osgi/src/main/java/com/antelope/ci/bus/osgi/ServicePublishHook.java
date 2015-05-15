// com.antelope.ci.bus.osgi.ServicePublishHook.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.osgi;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.ClassFinder;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-9		上午11:59:28 
 */
public abstract class ServicePublishHook extends Thread {
	protected BundleContext m_context;
	protected BusContext context;
	protected List<String> serviceList = new Vector<String>();
	protected List<String> scanedList = new Vector<String>();
	protected static final Logger log = Logger.getLogger(ServicePublishHook.class);
	protected String packetpath;
	
	@Deprecated
	public ServicePublishHook(BundleContext m_context, String packetpath) {
		this.m_context = m_context;
		this.packetpath = packetpath;
	}
	
	public ServicePublishHook(BusContext context, String packetpath) {
		this.context = context;
		this.packetpath = packetpath;
	}
	
	public void run() {
		while (true) {
			String cls_name = "";
			try {
				List<String>  classList = ClassFinder.findClasspath(packetpath, BusOsgiUtil.getBundleClassLoader(m_context));
				for (String cls : classList) {
					cls_name = cls;
					boolean scaned = false;
					for (String scanedCls : scanedList) {
						if (scanedCls.equals(cls)) {
							scaned = true;
							break;
						}
					}
					if (!scaned) {
						scanedList.add(cls);
						Class clazz = Class.forName(cls, false, BusOsgiUtil.getBundleClassLoader(m_context));
						ServicePublishInfo info = fetchService(clazz);
						if (info.canPublish) {
							if (context != null) {
								if (!((IService) info.service).publish(context))
									BusOsgiUtil.publishService(context.getBundleContext(), info.service, info.serviceName);
							} else if (!((IService) info.service).publish(m_context)) {
								BusOsgiUtil.publishService(m_context, info.service, info.serviceName);
							}
							serviceList.add(cls);
							log.info("add service :" + cls_name);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("problem for add service :" + cls_name);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
	
	protected abstract ServicePublishInfo fetchService(Class clazz); 
	
	protected static class ServicePublishInfo {
		public ServicePublishInfo() {
			
		}
		public boolean canPublish = false;
		public Object service;
		public String serviceName;
	}
}