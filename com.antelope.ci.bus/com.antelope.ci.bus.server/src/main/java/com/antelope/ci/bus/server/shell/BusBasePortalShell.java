// com.antelope.ci.bus.server.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午8:30:35 
 */
public abstract class BusBasePortalShell extends BusShell {
	protected boolean onHelp;

	public BusBasePortalShell(BusShellSession session) {
		super(session);
		onHelp = false;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#action()
	 */
	@Override
	protected void action() throws CIBusException {
		try {
			int c = io.read();
			if (c != -1) {
				if (onHelp) {
					switch (c) {
						case 'q':
						case 'Q':
							refresh();
							onHelp = false;
						default:
							answerHelp(c);
							break;
					}
				} else {
					switch (c) {
						case 'f':
			            case 'F': 		// refresh portal window
				            	refresh();
				            	break;
						case 'q':
						case 'Q':
							quit = true;
							break;
						case 'h':
						case 'H':
							clear();
							if (!StringUtil.empty(help()))
								io.write(help());
							onHelp = true;
							break;
						default:
							answer(c);
							break;
					}
				}
			} 
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}

	protected abstract void answer(int c) throws CIBusException;
	
	protected abstract String help();
	
	protected abstract void answerHelp(int c) throws CIBusException;
}

