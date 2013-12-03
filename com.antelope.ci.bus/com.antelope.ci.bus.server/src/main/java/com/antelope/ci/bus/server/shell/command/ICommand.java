// com.antelope.ci.bus.server.shell.command.Command.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * 命令接口
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-3		上午9:40:01 
 */
public interface ICommand {
	public String execute(TerminalIO io, Object... args);
}

