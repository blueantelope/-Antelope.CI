// com.antelope.ci.bus.common.xml.XmlElement.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.xml;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-14		上午11:51:58 
 */
@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface XmlElement {
	/*
	 * 节点名称
	 */
	String name();
	
	/*
	 * 是否必须存在，默认为不必须
	 */
	boolean necessary() default false;
	
	/*
	 * 是否有子节点
	 */
	boolean hasChildren() default false;
	
	/*
	 * 是否是个list,多个相同的element
	 */
	boolean isList() default false;
	
	
	/*
	 * list内部定义的class
	 */
	Class listClass() default Object.class;
}



