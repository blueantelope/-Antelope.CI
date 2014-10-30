// com.antelope.ci.bus.portal.project.BusProjectShellMode.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project;

import com.antelope.ci.bus.server.shell.Mode;
import com.antelope.ci.bus.server.shell.ModeClass;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年10月30日		下午5:06:47 
 */
@ModeClass
public class BusProjectShellMode {
	@Mode(code=11, name="shell.mode.project.form.add", simple="project_add", type=Mode.TYPE_INPUT)
	public static final String PROJECT_FORM_ADD				= "shell.mode.project.form.add";
}

