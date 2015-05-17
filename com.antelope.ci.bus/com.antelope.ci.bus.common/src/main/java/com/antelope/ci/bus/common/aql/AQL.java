// com.antelope.ci.bus.common.aql.AQL.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.aql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月16日		下午4:54:53 
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface AQL {
	String aql();
}

