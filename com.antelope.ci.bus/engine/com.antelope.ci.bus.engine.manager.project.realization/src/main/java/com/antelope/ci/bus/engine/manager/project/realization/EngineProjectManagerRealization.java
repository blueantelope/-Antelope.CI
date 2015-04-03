// com.antelope.ci.bus.engine.service.project.realization.ProjectServiceRealization.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.project.realization;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.engine.manager.EngineManager;
import com.antelope.ci.bus.engine.manager.EngineService;
import com.antelope.ci.bus.engine.manager.ManagerFunctionResult;
import com.antelope.ci.bus.engine.manager.project.CommonEngineProjectManager;
import com.antelope.ci.bus.engine.manager.project.EngineProjectManagerConstants;
import com.antelope.ci.bus.engine.model.project.Project;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月24日		下午4:34:22 
 */
@EngineService
@EngineManager(service_name=EngineProjectManagerConstants.SERVICE_NAME)
public class EngineProjectManagerRealization extends CommonEngineProjectManager {
	private final static Logger log = Logger.getLogger(EngineProjectManagerRealization.class);
	
	@Override
	public ManagerFunctionResult addProject(Project project) {
		log.info("add project from engine");
		
		return null;
	}
}
