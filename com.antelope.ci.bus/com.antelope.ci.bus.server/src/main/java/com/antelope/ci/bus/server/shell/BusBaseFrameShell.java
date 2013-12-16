// com.antelope.ci.bus.server.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.buffer.BusHitBuffer;
import com.antelope.ci.bus.server.shell.buffer.ShellCommandArg;
import com.antelope.ci.bus.server.shell.buffer.ShellCursor;
import com.antelope.ci.bus.server.shell.buffer.ShellScreen;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * portal shell template class
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-22		下午8:30:35 
 */
@Shell(commandAdapter="com.antelope.ci.bus.server.shell.command.hit.HitAdapter")
public abstract class BusBaseFrameShell extends BusShell {
	protected BusHitBuffer buffer;
	protected boolean onHelp;
	protected String cmd;
	
	public BusBaseFrameShell() {
		super();
		cmd = new String();
	}

	public BusBaseFrameShell(BusShellSession session) {
		super(session);
		cmd = new String();
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
			ShellCommandArg cmdArg;
			if (c != -1) {
				if (keyBell)
					io.bell();
				switch (c) {
					case TerminalIO.LEFT:
						buffer.left();
						break;
					case TerminalIO.RIGHT:
						buffer.right();
						break;
					case TerminalIO.UP:
						buffer.up();
						break;
					case TerminalIO.DOWN:
						buffer.down();
						break;
					case TerminalIO.DELETE:
						buffer.delete();
						break;
					case TerminalIO.BACKSPACE:
						buffer.backspace();
						break;
					case TerminalIO.SPACE:
						buffer.space();
						break;
					case TerminalIO.TABULATOR:
						buffer.tab();
						break;
					case TerminalIO.ENTER:
						cmdArg = buffer.enter();
						execute(cmdArg);
						buffer.reset();
						break;
					default:
						buffer.put((char) c);
						cmdArg = buffer.toCommand();
						execute(cmdArg);
						buffer.reset();
						break;
				}
			} 
		} catch (Exception e) {
			DevAssistant.errorln(e);
			e.printStackTrace();
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#mainView()
	 */
	@Override
	protected void mainView() throws CIBusException {
		view();
		ShellCursor cursor = initCursorPosistion();
		buffer = new BusHitBuffer(io, cursor, new ShellScreen(session.getWidth(), session.getHeigth()));
	}
	
	protected abstract ShellCursor initCursorPosistion(); 
	
	protected abstract void view() throws CIBusException;
}

