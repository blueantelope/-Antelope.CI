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
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-9		上午11:59:28 
 */
public abstract class ServicePublishHook extends Thread {
	private static final Logger log = Logger.getLogger(ServicePublishHook.class);
	protected enum CONTEXT_TYPE{BUS, BUNDLE};
	protected BusContext context;
	protected BundleContext m_context;
	protected List<String> serviceList = new Vector<String>();
	protected List<String> scanedList = new Vector<String>();
	protected String packetpath;
	protected CONTEXT_TYPE ctype;
	
	/**
	 * @Deprecated replace by {@link #ServicePublishHook(BusContext context, String packetpath)}
	 */
	@Deprecated
	public ServicePublishHook(BundleContext m_context, String packetpath) {
		this.m_context = m_context;
		this.packetpath = packetpath;
		ctype = CONTEXT_TYPE.BUNDLE;
	}
	
	public ServicePublishHook(BusContext context, String packetpath) {
		this.context = context;
		this.packetpath = packetpath;
		ctype = CONTEXT_TYPE.BUS;
	}
	
	public void run() {
		ClassLoader cloader;
		try {
			cloader = fetchClassloader();
		} catch (CIBusException e) {
			return;
		}
		
		while (true) {
			String cls_name = "";
			try {
				List<String>  classList = ClassFinder.findClasspath(packetpath, cloader);
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
						Class clazz = Class.forName(cls, false, cloader);
						ServicePublishInfo info = fetchService(clazz);
						if (info.canPublish) {
							boolean published = false;
							switch (ctype) {
								case BUS:
									if (!((IService) info.service).publish(context)) {
										BusOsgiUtil.publishService(context.getBundleContext(), info.service, info.serviceName);
										published = true;
									}
									break;
								case BUNDLE:
									if (!((IService) info.service).publish(m_context)) {
										BusOsgiUtil.publishService(m_context, info.service, info.serviceName);
										published = true;
									}
									break;
							}
							if (published) {
								log.info("add service :" + cls_name);
								serviceList.add(cls);
							}
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
	
	private ClassLoader fetchClassloader() throws CIBusException {
		switch (ctype) {
			case BUS:
				return context.getClassLoader();
			case BUNDLE:
				log.warn("depreacted publish context BundleContext, replace by BusContext");
				return BusOsgiUtil.getBundleClassLoader(m_context);
			default:
				throw new CIBusException("", "unknown publish context type");
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