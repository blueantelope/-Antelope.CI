// com.antelope.ci.bus.portal.shell.command.PortalHitAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell.command;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Scope;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.CommonHit;
import com.antelope.ci.bus.portal.core.shell.BusPortalShell;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.BusShellStatus.BaseStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.ICommand;
import com.antelope.ci.bus.server.shell.command.hit.HitAdapter;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-14		下午4:43:28 
 */
public class PortalCommandAdapter extends HitAdapter {
	private static final String COMMAND_FORM_PREFIX = "command.status.global.shell.mode.form";
	
	public PortalCommandAdapter() {
		super();
		try {
			init_add();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	private void init_add() throws CIBusException {
		addCommands("com.antelope.ci.bus.portal.core.shell.command");
	}
	
	@Override protected void afterExecute(BusShell shell, ICommand command,  Object... args) throws CIBusException {
		if (shell != null) {
			BusPortalShell portalShell = (BusPortalShell) shell;
			Command cmdContent = command.getContent();
			CommonHit hit = null;
			// look for hit of block 
			if (cmdContent != null) {
				String scope = EU_Scope.NATIVE.getName();
				if (BaseStatus.toStatus(cmdContent.status()) == BaseStatus.GLOBAL)
					scope = EU_Scope.GLOBAL.getName();
				hit = portalShell.getPortal().getHit(scope, cmdContent.mode(), cmdContent.name());
			}
			portalShell.focus(hit);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.CommandAdapter#showCommands(com.antelope.ci.bus.server.shell.BusShell, java.lang.String, int)
	 */
	@Override
	public void showCommands(BusShell shell, String prCmd, int width) {
		
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.command.CommandAdapter#userExecute(com.antelope.ci.bus.server.shell.BusShell, boolean, java.lang.String, java.lang.Object[])
	 */
	@Override
	protected String userExecute(BusShell shell, boolean refresh, String cmd, Object... args) throws CIBusException {
		BusPortalShell portalShell = (BusPortalShell) shell;
		if (portalShell.formCommandMode()) {
			userFinal = true;
			for (String key : globalCommandMap.keySet()) {
				if (key.startsWith(COMMAND_FORM_PREFIX)) {
					String rs = execute(key, globalCommandMap, shell, refresh, cmd, args);
					if (rs != null) {
						userFinal = false;
						portalShell.finishFormCommandMode();
						return rs;
					}
				}
			}
		}
		
		return null;
	}
}