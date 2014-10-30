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
	public static final String TYPE_MAIN = "main";
	public static final String TYPE_INPUT = "input";
	public static final String TYPE_EDIT = "edit";
	
	String name();
	
	int code();
	
	String simple();
	
	String type() default TYPE_MAIN;
}