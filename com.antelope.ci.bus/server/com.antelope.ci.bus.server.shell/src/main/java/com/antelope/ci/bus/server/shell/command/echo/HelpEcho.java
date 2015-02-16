// com.antelope.ci.bus.server.shell.command.BusHelpCommand.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command.echo;

import java.io.IOException;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.server.shell.base.BusShell;
import com.antelope.ci.bus.server.shell.base.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.command.HelpContent;



/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-25		下午9:00:24 
 */
@Command(name="help", commands="help", status=BusShellStatus.ROOT, type=CommandType.ECHO)
public class HelpEcho extends Echo {

	public HelpEcho() {
		super();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.BaseCommand#execute(com.antelope.ci.bus.server.shell.base.BusShell, java.lang.Object[])
	 */
	@Override
	public String execute(BusShell shell, Object... args) {
		try {
			shell.getIO().println(HelpContent.getContent().getEchoContent(BusShellStatus.HELP));
		} catch (IOException e) {
			DevAssistant.errorln(e);
		} finally {
			return BusShellStatus.ROOT;
		}
	}
}

