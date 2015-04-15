// com.antelope.ci.bus.common.xml.XmlAttribute.java
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
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-14		上午11:50:38 
 */
@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface XmlAttribute {
	/*
	 * 属性名称
	 */
	String name();
	
	/*
	 * 是否必须存在，默认为不必须
	 */
	boolean necessary() default false;
	
	/*
	 * 默认值 默认为空字符串
	 */
	String defaulted() default "";
}


