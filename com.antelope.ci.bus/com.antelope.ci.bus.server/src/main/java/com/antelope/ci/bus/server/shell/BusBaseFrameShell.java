// com.antelope.ci.bus.server.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.buffer.BusHitBuffer;
import com.antelope.ci.bus.server.shell.buffer.ShellCommandArg;
import com.antelope.ci.bus.server.shell.buffer.ShellCursor;
import com.antelope.ci.bus.server.shell.buffer.ShellScreen;


/**
 * portal shell template class
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午8:30:35 
 */
@Shell(name="base.frame", commandAdapter="com.antelope.ci.bus.server.shell.command.hit.HitAdapter")
public abstract class BusBaseFrameShell extends BusShell {
	protected boolean onHelp;
	protected String cmd;
	
	public BusBaseFrameShell() {
		super();
		initForFrame();
	}

	public BusBaseFrameShell(BusShellSession session) {
		super(session);
		initForFrame();
	}
	
	protected void initForFrame() {
		cmd = new String();
	}
	
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#action(int)
	 */
	@Override protected void action(int c) throws CIBusException {
		try {
			if (noActionForContorl()) {
				cmdArg = new ShellCommandArg(String.valueOf(controlKey), new String[]{});
				execute(cmdArg);
			} else {
				buffer.put((char) c);
				cmdArg = buffer.toCommand();
				execute(cmdArg);
				buffer.reset();
			}
		} catch (Exception e) {
			DevAssistant.assert_exception(e);
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#mainView()
	 */
	@Override
	public void mainView() throws CIBusException {
		view();
		ShellCursor cursor = initCursorPosistion();
		buffer = new BusHitBuffer(io, cursor, new ShellScreen(session.getWidth(), session.getHeigth()));
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#userAction(int)
	 */
	@Override protected boolean userAction(int c) throws CIBusException {
		switch (c) {
			case NetVTKey.TABULATOR:
				buffer.tab();
				return true;
			case NetVTKey.LF:
				cmdArg = buffer.enter();
				execute(cmdArg);
				buffer.reset();
				return true;
			default:
				return false;
		}
	}
	
	protected abstract ShellCursor initCursorPosistion(); 
	
	protected abstract void view() throws CIBusException;
}

