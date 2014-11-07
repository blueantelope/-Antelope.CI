// com.antelope.ci.bus.portal.core.shell.command.QuitFormPortalHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.portal.core.shell.BusPortalShellMode;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandHelper;
import com.antelope.ci.bus.server.shell.command.CommandType;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月6日		下午6:25:56 
 */
@Command(
		name="submit_form", 
		commands="\"" + CommandHelper.escCommand + "q,+" + CommandHelper.escCommand + "Q\"",
		status=BusShellStatus.GLOBAL, 
		type=CommandType.HIT, 
		mode=BusPortalShellMode.FORM)
public class QuitFormPortalHit extends PortalHit {
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.portal.core.shell.command.PortalHit#executeOnMain(com.antelope.ci.bus.portal.core.shell.BusPortalShell, java.lang.Object[])
	 */
	@Override
	protected String executeOnMain(BusPortalShell shell, Object... args) {
		try {
			shell.exitInput();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		return BusShellStatus.KEEP;
	}
}

