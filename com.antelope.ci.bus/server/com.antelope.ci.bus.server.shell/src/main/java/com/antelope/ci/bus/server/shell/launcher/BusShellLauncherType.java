// com.antelope.ci.bus.shell.BusShellLauncherType.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.launcher;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月15日		下午5:22:12 
 */
public class BusShellLauncherType {
	private static final Logger log = Logger.getLogger(BusShellLauncherType.class);
	
	public enum BaseShellLauncherType {
		@ShellLauncher(name="shell.launcher.container", simple="container", clazz=BusShellContainerLauncher.clazz)
		CONTAINER("shell.launcher.container", "container", BusShellContainerLauncher.clazz),
		@ShellLauncher(name="shell.launcher.proxy", simple="proxy", clazz=BusShellProxyLauncher.clazz)
		PROXY("shell.launcher.proxy", "proxy", BusShellProxyLauncher.clazz);
		
		private String name;
		private String simple;
		private String clazz;
		private BaseShellLauncherType(String name, String simple, String clazz) {
			this.name = name;
			this.simple = simple;
			this.clazz = clazz;
		}
		public String getName() {
			return name;
		}
		public String getSimple() {
			return simple;
		}
		public String getClazz() {
			return clazz;
		}
		
		public static BaseShellLauncherType fromName(String name) throws CIBusException {
			if (StringUtil.empty(name))
				throw new CIBusException("", "name of BaseShellLauncherType empty");
				
			for (BaseShellLauncherType type : BaseShellLauncherType.values())
				if (type.getName().equalsIgnoreCase(name.trim()))
					return type;
			
			throw new CIBusException("", "unknow name of BaseShellLauncherType");
		}
		
		public static BaseShellLauncherType fromSimple(String simple) throws CIBusException {
			if (StringUtil.empty(simple))
				throw new CIBusException("", "simple of BaseShellLauncherType empty");
				
			for (BaseShellLauncherType type : BaseShellLauncherType.values())
				if (type.getSimple().equalsIgnoreCase(simple.trim()))
					return type;
			
			throw new CIBusException("", "unknow simple of BaseShellLauncherType");
		}
	}
	
	private static List<BusShellLauncherType> typeList;
	private static Map<String, ShellLauncher> launcherMap;
	
	static {
		typeList = new Vector<BusShellLauncherType>();
		launcherMap = new ConcurrentHashMap<String, ShellLauncher>();
		addLauncherToMap(BaseShellLauncherType.class);
	}
	
	public static void addLauncherToMap(Class clazz) {
		for (Field f : clazz.getFields()) {
			if (f.isAnnotationPresent(ShellLauncher.class)) {
				ShellLauncher launcher = f.getAnnotation(ShellLauncher.class);
				String launcherName = ProxyUtil.classpathForField(f);
				if (!launcherMap.containsKey(launcherName))
					launcherMap.put(launcherName, launcher);
			}
		}
	}
	
	public static ShellLauncher getLauncher(String clazz) {
		if (!StringUtil.empty(clazz))
			return null;
		for (Map.Entry<String, ShellLauncher> entry : launcherMap.entrySet()) {
			ShellLauncher launcher = entry.getValue();
			if (clazz.equalsIgnoreCase(launcher.clazz()))
				return launcher;
		}
		return null;
	}
	
	public static boolean isContainer(String... values) {
		return isBaseLauncher(BaseShellLauncherType.CONTAINER, values);
	}

	public static boolean isProxy(String... values) {
		return isBaseLauncher(BaseShellLauncherType.PROXY, values);
	}
	
	private static boolean isBaseLauncher(BaseShellLauncherType type, String... values) {
		ShellLauncher launcher = searchLauncher(values);
		if (launcher != null) {
			try {
				String name = launcher.name();
				String simple = launcher.simple();
				if (!StringUtil.empty(name)) {
					try {
						if (BaseShellLauncherType.fromName(name) != null)
							return true;
					} catch (CIBusException e) {}
				}
				if (!StringUtil.empty(simple)) {
					try {
						if (BaseShellLauncherType.fromSimple(simple) != null)
							return true;
					} catch (CIBusException e) {}
				}
			} catch (Exception e) {
				log.warn(e);
				DevAssistant.errorln(e);
			}
		}
		return false;
	}
	
	private static ShellLauncher searchLauncher(String... values) {
		if (values == null || values.length == 0)
			return null;
		String simple = values[0];
		String name = null;
		if (values.length > 1)
			name = values[1];
		for (Map.Entry<String, ShellLauncher> entry : launcherMap.entrySet()) {
			ShellLauncher launcher = entry.getValue();
			if (!StringUtil.empty(name)) {
				if (name.equalsIgnoreCase(launcher.name()))
					return launcher;
			}
			if (!StringUtil.empty(simple)) {
				if (simple.equalsIgnoreCase(launcher.simple()))
					return launcher;
			}
		}
		return null;
	}
}

