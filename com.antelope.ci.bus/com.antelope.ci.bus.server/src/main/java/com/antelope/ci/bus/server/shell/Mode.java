// com.antelope.ci.bus.server.shell.Mode.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-25		上午11:42:40 
 */
@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface Mode {
	public enum BaseModeType {
		_DEFAULT(0, "_default"),
		MAIN(1, "main"),
		INPUT(2, "input"),
		EDIT(3, "edit");
		
		private int code;
		private String name;
		private BaseModeType(int code, String name) {
			this.code = code;
			this.name = name;
		}
		
		public int getCode() {
			return code;
		}
		
		public String getName() {
			return name;
		}
		
		public static BaseModeType toType(String name) throws CIBusException {
			for (BaseModeType bs : BaseModeType.values()) {
				if (bs.getName().equalsIgnoreCase(name))
					return bs;
			}
			
			throw new CIBusException("", "unknown mode type name");
		}
		
		public static BaseModeType toType(int code) throws CIBusException {
			for (BaseModeType bs : BaseModeType.values()) {
				if (bs.getCode() == code)
					return bs;
			}
			
			throw new CIBusException("", "unknown mode type code");
		}
	}
	
	public static final String TYPE_MAIN = "main";
	public static final String TYPE_INPUT = "input";
	public static final String TYPE_EDIT = "edit";
	
	String name();
	
	int code();
	
	String simple();
	
	String type() default TYPE_MAIN;
}