// com.antelope.ci.bus.portal.project.command.NewProjectHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.project.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.portal.project.BusProjectShellStatus;
import com.antelope.ci.bus.portal.shell.ShellLineContentSet;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.command.hit.Hit;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-27		下午6:10:08 
 */
@Command(name="new_project", commands="n, N", status=BusProjectShellStatus.PROJECT, type=CommandType.HIT, beforeClear=false)
public class NewProjectHit extends Hit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.BaseCommand#execute(com.antelope.ci.bus.server.shell.BusShell, com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.String, java.lang.Object[])
	 */
	@Override
	protected String execute(BusShell shell, TerminalIO io, String status, Object... args) {
		try {
			ShellLineContentSet contentSet = new ShellLineContentSet();
			List<String> line = new ArrayList<String>();
			line.add("test");
			contentSet.addLine(line);
			shell.writeContent(contentSet);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
		
		return BusProjectShellStatus.PROJECT;
	}

}

