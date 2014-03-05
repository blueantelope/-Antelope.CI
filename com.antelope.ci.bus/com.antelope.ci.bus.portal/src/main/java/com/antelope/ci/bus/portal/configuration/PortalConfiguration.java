// com.antelope.ci.bus.portal.configuration.PortalConfiguration.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.configuration;

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
 * @Date	 2014-3-3		下午5:09:55 
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface PortalConfiguration {
	String xml();
	
	String properties();
}

