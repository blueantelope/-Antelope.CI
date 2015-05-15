// com.antelope.ci.bus.gate.core.shell.BusGateShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.gate.shell;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.gate.shell.command.GateCommandAdapter;
import com.antelope.ci.bus.server.shell.base.BusBaseEchoShell;
import com.antelope.ci.bus.server.shell.base.Shell;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年3月6日		上午10:21:45 
 */
@Shell(name="gate.shell", commandAdapter=GateCommandAdapter.NAME)
public class BusGateShell extends BusBaseEchoShell {
	
	public BusGateShell() {
		super();
	}

	@Override
	protected String prompt() {
		return "gate@antelope.cibus:> ";
	}

	@Override
	protected String header() {
		return "CIBus Gate Shell 0.1";
	}

	@Override
	protected void customShellEnv() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void shutdown() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearContent() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveContent() throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeContent(Object content) throws CIBusException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean handleInput(int c) {
		
		// TODO Auto-generated method stub
		return false;
		
	}

	@Override
	protected void handleMode() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContext(Object... contexts) {
		
		// TODO Auto-generated method stub
		
	}
}
