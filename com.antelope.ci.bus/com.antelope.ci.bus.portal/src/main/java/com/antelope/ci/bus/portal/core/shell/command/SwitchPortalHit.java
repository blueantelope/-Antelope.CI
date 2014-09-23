// com.antelope.ci.bus.portal.shell.command.SwitchShellHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.PortalShellUtil;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandHelper;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-28		下午5:37:11 
 */
@Command(
		name="switch_portal", 
		commands=CommandHelper.tabCommand,
		status=BusShellStatus.GLOBAL, 
		type=CommandType.HIT, 
		beforeClear=true)
public class SwitchPortalHit extends MainCommonPortalHit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.portal.core.shell.command.MainCommonPortalHit#executeOnMain(com.antelope.ci.bus.portal.core.shell.BusPortalShell, com.antelope.ci.bus.server.shell.core.TerminalIO, java.lang.String, java.lang.Object[])
	 */
	@Override protected String executeOnMain(BusPortalShell shell, TerminalIO io,
			String status, Object... args) {
		return PortalShellUtil.getNextStatus(status);
	}
}