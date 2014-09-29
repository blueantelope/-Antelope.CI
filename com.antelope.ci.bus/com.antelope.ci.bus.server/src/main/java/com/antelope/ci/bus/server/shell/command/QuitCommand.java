// com.antelope.ci.bus.server.shell.command.hit.CommandQuit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command;

import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.BusShellStatus;


/**
 * quit system
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-13		下午5:44:04 
 */
@Command(
		name="quit",
		commands="q, Q",
		status=BusShellStatus.LAST,
		type=CommandType.HIT)
public class QuitCommand extends BaseCommand {
	@Override protected String execute(BusShell shell, String status, Object... args) {
		return BusShellStatus.LAST;
	}
}