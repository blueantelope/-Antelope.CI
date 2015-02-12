// com.antelope.ci.bus.server.shell.command.hit.HelpHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.shell.command.hit;

import java.io.IOException;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.shell.BusShell;
import com.antelope.ci.bus.shell.BusShellStatus;
import com.antelope.ci.bus.shell.command.Command;
import com.antelope.ci.bus.shell.command.CommandType;
import com.antelope.ci.bus.shell.command.HelpContent;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-6		下午9:47:12 
 */
@Command(
		name="help",
		commands="h, H",
		status=BusShellStatus.ROOT,
		type=CommandType.HIT,
		beforeClear=true)
public class HelpHit extends Hit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.shell.command.BaseCommand#execute(com.antelope.ci.bus.shell.BusShell, java.lang.String, java.lang.Object[])
	 */
	@Override
	public String execute(BusShell shell, Object... args) {
		try {
			shell.getIO().println(HelpContent.getContent().getHitContent(BusShellStatus.HELP));
		} catch (IOException e) {
			DevAssistant.errorln(e);
		} finally {
			return BusShellStatus.HELP;
		}
	}
}

