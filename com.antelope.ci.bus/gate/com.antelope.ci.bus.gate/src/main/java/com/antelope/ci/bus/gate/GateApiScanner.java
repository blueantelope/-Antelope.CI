// com.antelope.ci.bus.gate.api.GateApiScanner.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.gate.api.GateApi;
import com.antelope.ci.bus.gate.api.IGateApi;
import com.antelope.ci.bus.osgi.BusServiceInfo;
import com.antelope.ci.bus.osgi.InjectHelper;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月25日		下午4:52:39 
 */
public class GateApiScanner {
	private final static Logger log = Logger.getLogger(GateApiScanner.class);
	private final static long SCAN_START_DELAY = 2 * 1000;
	private final static long SCAN_PERIOD = 2 * 1000;
	private final static String SCAN_PACKAGE = "com.antelope.ci.bus.gate.api";
	
	private final static GateApiScanner scanner = new GateApiScanner();
	
	public final static GateApiScanner getScanner() {
		return scanner;
	}
	
	private Map<Integer, IGateApi> apiMap;
	private Map<Integer, List<Method>> setterMap;
	private Map<String, List<BusServiceInfo>> serviceMap;
	private ClassLoader classLoaer;
	
	private GateApiScanner() {
		apiMap = new ConcurrentHashMap<Integer, IGateApi>();
		setterMap = new ConcurrentHashMap<Integer, List<Method>>();
	}
	
	public void setServiceMap(Map<String, List<BusServiceInfo>> serviceMap) {
		this.serviceMap = serviceMap;
	}
	
	public void setClassLoader(ClassLoader classLoaer) {
		this.classLoaer = classLoaer;
	}
	
	public IGateApi getGateApi(int oc) {
		return apiMap.get(oc);
	}
	
	public void start() {
		new Thread() {
			@Override
			public void run() {
				if (SCAN_START_DELAY > 0) {
					try {
						Thread.sleep(SCAN_START_DELAY);
					} catch (InterruptedException e) {}
				}
				while (true) {
					if (serviceMap != null && !serviceMap.isEmpty())
						scan();
					try {
						Thread.sleep(SCAN_PERIOD);
					} catch (InterruptedException e) { }
				}
			}
			
			/* scan all children packages */
			private void scan() {
				try {
					List<String>  classList = ClassFinder.findClasspath(SCAN_PACKAGE, classLoaer);
					for (String cls : classList) {
						Class clazz = Class.forName(cls, false, classLoaer);
						boolean apiLoaded = false;
						if (IGateApi.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(GateApi.class)) {
							GateApi api = (GateApi) clazz.getAnnotation(GateApi.class);
							if (!setterMap.containsKey(api.oc()))
								setterMap.put(api.oc(), new ArrayList<Method>());
							List setterList = setterMap.get(api.oc());
							if (!apiMap.containsKey(api.oc())) {
								apiLoaded = true;
								apiMap.put(api.oc(), (IGateApi)ProxyUtil.newObject(clazz, classLoaer));
							}
							IGateApi gateApi = apiMap.get(api.oc());
							if (!apiLoaded || !setterList.isEmpty())
								InjectHelper.injectService(gateApi, serviceMap, setterList);
						}
					}
				} catch (Exception e) {
					log.warn("problem to scan gate api class:" + e);
				}
			}
		}.start();
	}
}
