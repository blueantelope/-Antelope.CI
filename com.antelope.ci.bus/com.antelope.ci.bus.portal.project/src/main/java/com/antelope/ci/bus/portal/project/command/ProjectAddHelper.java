// com.antelope.ci.bus.portal.project.command.ProjectAddHelper.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project.command;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.portal.core.shell.command.PortalFormContext;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月3日		下午5:53:31 
 */
public class ProjectAddHelper extends PortalFormContext {
	private final static Logger log = Logger.getLogger(ProjectAddHelper.class);
	private static final ProjectAddHelper helper = new ProjectAddHelper();
	
	public static final ProjectAddHelper getHelper() {
		return helper;
	}
	
	private ProjectAddHelper() {
		super();
	}
}

