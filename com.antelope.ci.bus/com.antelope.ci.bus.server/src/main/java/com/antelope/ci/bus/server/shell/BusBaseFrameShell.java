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
					case NetVTKey.LEFT:
						buffer.left();
						break;
					case NetVTKey.RIGHT:
						buffer.right();
						break;
					case NetVTKey.UP:
						buffer.up();
						break;
					case NetVTKey.DOWN:
						buffer.down();
						break;
					case NetVTKey.DELETE:
						buffer.delete();
						break;
					case NetVTKey.BACKSPACE:
						buffer.backspace();
						break;
					case NetVTKey.SPACE:
						buffer.space();
						break;
					case NetVTKey.TABULATOR:
						buffer.tab();
						break;
					case NetVTKey.ENTER:
						cmdArg = buffer.enter();
						execute(cmdArg);
						buffer.reset();
						break;
					default:
						break;
				}
				
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
	
	protected abstract ShellCursor initCursorPosistion(); 
	
	protected abstract void view() throws CIBusException;
}

