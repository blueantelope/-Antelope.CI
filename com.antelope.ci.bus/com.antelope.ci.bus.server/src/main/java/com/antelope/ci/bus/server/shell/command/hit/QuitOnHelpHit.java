// com.antelope.ci.bus.server.shell.command.hit.QuitOnHelpHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command.hit;

import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.BaseCommand;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-6		下午9:49:33 
 */
@Command(name="quit", commands="q, Q", status=BusShellStatus.HELP, type=CommandType.HIT)
public class QuitOnHelpHit extends BaseCommand implements Hit {

	@Override
	public String execute(TerminalIO io, Object... args) {
		return BusShellStatus.LAST;
	}

}

