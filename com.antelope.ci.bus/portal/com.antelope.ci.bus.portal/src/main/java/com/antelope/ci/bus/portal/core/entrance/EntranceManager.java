// com.antelope.ci.bus.portal.entrance.EntranceManager.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.entrance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.BusOsgiUtil;
import com.antelope.ci.bus.server.BusServerCondition;


/**
 * 
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-18		下午10:11:47 
 */
public class EntranceManager {
	private static final Logger log = Logger.getLogger(EntranceManager.class);
	private static Map<String, Entrance> entranceMap = new HashMap<String, Entrance>();
	
	public static void monitor(BundleContext m_context, BusServerCondition server_condition) {
		new EntranceMonitor(m_context, server_condition).start();
	}
	
	private static class EntranceMonitor extends Thread {
		private BundleContext m_context;
		private BusServerCondition server_condition;
		
		private EntranceMonitor(BundleContext m_context, BusServerCondition server_condition) {
			this.m_context = m_context;
			this.server_condition = server_condition;
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					List<String> classList = ClassFinder.findClasspath("com.antelope.ci.bus.portal", 
							BusOsgiUtil.getBundleClassLoader(m_context));
					unmount(classList);
					mount(classList);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) { }
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
		
		private void unmount(List<String>  classList) {
			List<String> unmountList = new ArrayList<String>();
			for (String entrance_cls :entranceMap.keySet()) {
				boolean is_unmount = true;
				for (String cls : classList) {
					if (cls.equals(entrance_cls)) {
						is_unmount = false;
						break;
					}
				}
				if (is_unmount)
					unmountList.add(entrance_cls);
			}
			for (String unmount_cls : unmountList) {
				final Entrance entrance = entranceMap.get(unmount_cls);
				log.info("mount entrance of portal : " + unmount_cls);
				new Thread() {
					public void run() {
						try {
							entrance.unmount();
						} catch (CIBusException e) {
							log.error(e);
						}
					}
				}.start();
				entranceMap.remove(unmount_cls);
			}
		}
		
		private void mount(List<String> classList) {
			for (String cls : classList) {
				try {
					Class clazz;
					try {
						clazz = ProxyUtil.loadClass(cls);
					} catch (CIBusException e) {
						clazz = ProxyUtil.loadClass(cls, BusOsgiUtil.getBundleClassLoader(m_context));
					}
					if (Entrance.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(PortalEntrance.class)) {
						if (entranceMap.get(cls) == null) {
							final Entrance entrance = (Entrance) clazz.newInstance();
							entrance.init(server_condition);
							log.info("mount entrance of portal : " + cls);
							new Thread() {
								public void run() {
									try {
										entrance.mount();
									} catch (CIBusException e) {
										log.error(e);
									}
								}
							}.start();
							entranceMap.put(cls, entrance);
						}
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
		
	}
}
