// com.antelope.ci.bus.engine.model.ModelData.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * function annotation for model
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月24日		上午11:38:48 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface ModelData {
	String name() default "";
	String getter() default "";
	String setter() default "";
}
