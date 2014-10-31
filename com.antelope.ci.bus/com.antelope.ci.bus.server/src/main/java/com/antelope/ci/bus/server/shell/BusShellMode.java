// com.antelope.ci.bus.server.shell.BusShellMode.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.Mode.BaseModeType;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-25		上午11:41:45 
 */
public class BusShellMode {
	public enum BaseMode {
		@Mode(code=1, name="shell.mode.main", simple="main", type=Mode.TYPE_MAIN)
		MAIN(1, "shell.mode.main", "main"),
		@Mode(code=2, name="shell.mode.input", simple="input", type=Mode.TYPE_INPUT)
		INPUT(2, "shell.mode.input", "input"),
		@Mode(code=3, name="shell.mode.edit", simple="edit", type=Mode.TYPE_EDIT)
		EDIT(3, "shell.mode.edit", "edit");
		
		private int code;
		private String name;
		private String simple;
		private BaseMode(int code, String name, String simple) {
			this.code = code;
			this.name = name;
			this.simple = simple;
		}
		
		public int getCode() {
			return code;
		}
		
		public String getName() {
			return name;
		}
		
		public String getSimple() {
			return simple;
		}
		
		public static BaseMode toMode(String name) throws CIBusException {
			for (BaseMode bs : BaseMode.values()) {
				if (bs.getName().equalsIgnoreCase(name))
					return bs;
			}
			
			throw new CIBusException("", "unknown mode name");
		}
		
		public static BaseMode toMode(int code) throws CIBusException {
			for (BaseMode bs : BaseMode.values()) {
				if (bs.getCode() == code)
					return bs;
			}
			
			throw new CIBusException("", "unknown mode code");
		}
	}
	
	public static final String MAIN				= "shell.mode.main";
	public static final String INPUT 			= "shell.mode.input";
	public static final String EDIT				= "shell.mode.edit";
	
	private static List<ModeExtenstion> modeList;
	private static Map<String, Mode> modeMap;
	
	static {
		modeList = new Vector<ModeExtenstion>();
		modeMap = new ConcurrentHashMap<String, Mode>();
		addModeToMap(BaseMode.class, false);
	}
	
	public static void	addModeClass(Class modeClass) {
		addModeToMap(modeClass, true);
	}
	
	public static void addModeToMap(Class modeClass) {
		addModeToMap(modeClass, true);
	}
	
	private static void addModeToMap(Class modeClass, boolean isExtension) {
		for (Field f : modeClass.getFields()) {
			if (f.isAnnotationPresent(Mode.class)) {
				Mode mode = f.getAnnotation(Mode.class);
				modeMap.put(ProxyUtil.classpathForField(f), mode);
				if (isExtension)
					modeList.add(new ModeExtenstion(mode.name(), modeClass));
			}
		}
	}
	
	public static boolean isMain(String modeName) {
		return isType(modeName, Mode.TYPE_MAIN);
	}
	
	public static boolean isInput(String modeName) {
		return isType(modeName, Mode.TYPE_INPUT);
	}
	
	public static boolean isEdit(String modeName) {
		return isType(modeName, Mode.TYPE_EDIT);
	}
	
	public static BaseModeType getBaseModeType(String name) {
		Mode mode = searchMode(name);
		if (mode != null) {
			try {
				return BaseModeType.toType(mode.type());
			} catch (CIBusException e) {
				DevAssistant.assert_exception(e);
			}
		}
		
		return null;
	}
	
	private static boolean isType(String name, String type) {
		Mode mode = searchMode(name);
		if (mode != null && mode.type().equalsIgnoreCase(type))
			return true;
		return false;
	}
	
	private static Mode searchMode(String name) {
		for (Map.Entry<String, Mode> entry : modeMap.entrySet()) {
			Mode mode = entry.getValue();
			if (name.equalsIgnoreCase(mode.name()))
				return mode;
		}
		return null;
	}
	
	private static class ModeExtenstion {
		private String name;
		private Class cls;
		public ModeExtenstion(String name, Class cls) {
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
