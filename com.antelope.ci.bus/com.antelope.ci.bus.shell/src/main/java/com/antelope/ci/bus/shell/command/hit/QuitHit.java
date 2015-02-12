// com.antelope.ci.bus.server.shell.command.hit.QuitHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.shell.command.hit;

import com.antelope.ci.bus.shell.BusShell;
import com.antelope.ci.bus.shell.BusShellStatus;
import com.antelope.ci.bus.shell.command.Command;
import com.antelope.ci.bus.shell.command.CommandType;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-6		下午9:44:40 
 */
@Command(
		name="quit",
		commands="q, Q",
		status=BusShellStatus.GLOBAL,
		type=CommandType.HIT)
public class QuitHit extends Hit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.shell.command.BaseCommand#execute(com.antelope.ci.bus.shell.BusShell, java.lang.String, java.lang.Object[])
	 */
	@Override
	public String execute(BusShell shell, Object... args) {
		return BusShellStatus.QUIT;
	}
}

