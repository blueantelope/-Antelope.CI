// com.antelope.ci.bus.portal.core.shell.BusPortalShellMode.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell;

import com.antelope.ci.bus.server.shell.Mode;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月6日		下午12:02:59 
 */
public class BusPortalShellMode {
	@Mode(code=5, name="shell.mode.form", simple="form", type=Mode.TYPE_INPUT)
	public static final String FORM				= "shell.mode.form";
}