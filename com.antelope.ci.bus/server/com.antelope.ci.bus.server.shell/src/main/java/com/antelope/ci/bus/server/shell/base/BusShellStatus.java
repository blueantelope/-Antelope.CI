// com.antelope.ci.bus.server.shell.command.CommandStatus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.ProxyUtil;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		下午12:27:49 
 */
public class BusShellStatus {
	public enum BaseStatus {
		@ShellStatus(code=-1, name="command.status.none")
		NONE(-1, "command.status.none"),
		@ShellStatus(code=0, name="command.status.global")
		GLOBAL(0, "command.status.global"),
		@ShellStatus(code=1, name="command.status.init")
		INIT(1, "command.status.init"),
		@ShellStatus(code=2, name="command.status.root")
		ROOT(2, "command.status.root"),
		@ShellStatus(code=3, name="command.status.help")
		HELP(3, "command.status.help"),
		@ShellStatus(code=4, name="command.status.quit")
		QUIT(4, "command.status.quit"),
		@ShellStatus(code=5, name="command.status.last")
		LAST(5, "command.status.last"),
		@ShellStatus(code=6, name="command.status.keep")
		KEEP(6, "command.status.keep");
		
		private int code;
		private String name;
		private BaseStatus(int code, String name) {
			this.code = code;
			this.name = name;
		}
		
		public int getCode() {
			return code;
		}
		
		public String getName() {
			return name;
		}
		
		public static BaseStatus toStatus(String name) {
			for (BaseStatus es : BaseStatus.values()) {
				if (es.getName().equalsIgnoreCase(name))
					return es;
			}
			
			return NONE;
		}
		
		public static BaseStatus toStatus(int code) {
			for (BaseStatus es : BaseStatus.values()) {
				if (es.getCode() == code)
					return es;
			}
			
			return NONE;
		}
	}
	
	public static final String NONE 				= "command.status.none";
	public static final String GLOBAL 			= "command.status.global";
	public static final String INIT					= "command.status.init";
	public static final String ROOT 				= "command.status.root";
	public static final String HELP				= "command.status.help";
	public static final String QUIT				= "command.status.quit";
	public static final String LAST				= "command.status.last";
	public static final String KEEP				= "command.status.keep";
	
	private static List<StatusExtenstion> statusList;
	private static Map<String, ShellStatus> statusMap;
	
	static {
		statusList = new Vector<StatusExtenstion>();
		statusMap = new ConcurrentHashMap<String, ShellStatus>();
		addStatusToMap(BaseStatus.class, false);
	}
	
	public static void addStatusClass(Class statusClass) {
		addStatusToMap(statusClass, true);
	}
	
	public static void removeStatusClass(Class statusClass) {
		int d_index = 0;
		for (StatusExtenstion se : statusList) {
			Class sc = se.getCls();
			if (sc == statusClass) {
				removeStatusFromMap(sc);
				break;
			}
			d_index++;
		}
		statusList.remove(d_index);
	}
	
	private static void addStatusToMap(Class statusClass, boolean isExtension) {
		for (Field f : statusClass.getFields()) {
			if (f.isAnnotationPresent(ShellStatus.class)) {
				ShellStatus status = f.getAnnotation(ShellStatus.class);
				statusMap.put(ProxyUtil.classpathForField(f), status);
				if (isExtension)
					statusList.add(new StatusExtenstion(status.name(), statusClass));
			}
		}
	}
	
	private static void removeStatusFromMap(Class statusClass) {
		List<String> delList = new ArrayList<String>();
		for (Field f : statusClass.getFields()) {
			if (f.isAnnotationPresent(ShellStatus.class)) {
				if (statusMap.get(f.getName()) != null)
					delList.add(f.getName());
			}
		}
		for (String del : delList)
			statusMap.remove(del);
	}
	
	public static int code(String name) {
		for (Map.Entry<String, ShellStatus> entry : statusMap.entrySet()) {
			ShellStatus status = entry.getValue();
			if (name.equalsIgnoreCase(status.name()))
				return status.code();
		}
		return -1;
	}
	
	public static BaseStatus toBaseStatus(int code) {
		return BaseStatus.toStatus(code);
	}
	
	public static BaseStatus toBaseStatus(String name) {
		return BaseStatus.toStatus(name);
	}
	
	public static String getStartStatus() {
		if (statusList.isEmpty())
			return statusList.get(0).getName();
		
		return ROOT;
	}
	
	private static class StatusExtenstion {
		private String name;
		private Class cls;
		public StatusExtenstion(String name, Class cls) {
			super();
			this.name = name;
			this.cls = cls;
		}
		public String getName() {
			return name;
		}
		public Class getCls() {
			return cls;
		}
	}
}