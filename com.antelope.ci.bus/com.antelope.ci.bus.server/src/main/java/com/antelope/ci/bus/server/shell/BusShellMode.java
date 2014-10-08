// com.antelope.ci.bus.server.shell.BusShellMode.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusShellStatus.BaseStatus;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-25		上午11:41:45 
 */
public class BusShellMode {
	public enum BaseMode {
		@Mode(code=1, name="shell.mode.main", simple="main")
		MAIN(1, "shell.mode.main", "main"),
		@Mode(code=2, name="shell.mode.input", simple="input")
		INPUT(2, "shell.mode.input", "input"),
		@Mode(code=3, name="shell.mode.edit", simple="edit")
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
		
		public static BaseStatus toMode(int code) throws CIBusException {
			for (BaseStatus bs : BaseStatus.values()) {
				if (bs.getCode() == code)
					return bs;
			}
			
			throw new CIBusException("", "unknown mode code");
		}
	}
	
	public static final String MAIN				= "shell.mode.main";
	public static final String INPUT 			= "shell.mode.input";
	public static final String EDIT				= "shell.mode.edit";
	
	public static boolean isMain(String modeName) {
		return isBaseMode(modeName, BaseMode.MAIN);
	}
	
	public static boolean isInput(String modeName) {
		return isBaseMode(modeName, BaseMode.INPUT);
	}
	
	public static boolean isEdit(String modeName) {
		return isBaseMode(modeName, BaseMode.EDIT);
	}
	
	private static boolean isBaseMode(String modeName, BaseMode mode) {
		if (!StringUtil.empty(modeName)) {
			try {
				if (BaseMode.toMode(modeName) == mode)
					return true;
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
				return false;
			}
			
			return false;
		}
		
		return false;
	}
}
