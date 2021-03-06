// com.antelope.ci.bus.service.project.ProjectService.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.manager.project;

import com.antelope.ci.bus.engine.manager.ManagerFunctionResult;
import com.antelope.ci.bus.engine.model.project.Project;


/**
 * 工程服务接口定义
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-6-24		下午2:23:48 
 */
public interface EngineProjectManager {
	/**
	 * 建立新工程
	 * @param  @param project
	 * @param  @return
	 * @return ServiceOpResult
	 * @throws
	 */
	public ManagerFunctionResult addProject(Project project);
}

