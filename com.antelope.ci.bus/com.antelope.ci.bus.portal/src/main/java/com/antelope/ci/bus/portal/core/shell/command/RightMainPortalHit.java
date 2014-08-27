// com.antelope.ci.bus.portal.core.shell.command.RightMainPortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.server.shell.BusShellMode;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-26		上午11:33:38 
 */
@Command(
		name="right_main", 
		commands=CommandAdapter.rightCommand, 
		status=BusShellStatus.GLOBAL, 
		type=CommandType.HIT, 
		mode=BusShellMode.MAIN)
public class RightMainPortalHit extends MainCommonPortalHit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.portal.core.shell.command.MainCommonPortalHit#executeOnMain(com.antelope.ci.bus.portal.core.shell.BusPortalShell, com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.String, java.lang.Object[])
	 */
	@Override protected String executeOnMain(BusPortalShell shell, TerminalIO io, String status, Object... args) {
		right(shell);
		return BusShellStatus.KEEP;
	}

}