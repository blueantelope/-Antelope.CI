// com.antelope.ci.bus.server.shell.command.hit.RefreshHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command.hit;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandType;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-9		下午5:21:43 
 */
@Command(
		name="refresh",
		commands="r, R",
		status=BusShellStatus.GLOBAL,
		type=CommandType.HIT)
public class RefreshHit extends Hit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.BaseCommand#execute(com.antelope.ci.bus.server.shell.BusShell, java.lang.String, java.lang.Object[])
	 */
	@Override
	public String execute(BusShell shell, String status, Object... args) {
		try {
			shell.refresh();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		return BusShellStatus.KEEP;
	}

}

