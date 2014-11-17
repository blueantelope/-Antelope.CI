// com.antelope.ci.bus.server.shell.command.test.TestCommandAdapter.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.command.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.server.shell.BusShell;
import com.antelope.ci.bus.server.shell.BusShellMode;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.command.Command;
import com.antelope.ci.bus.server.shell.command.CommandHelper;
import com.antelope.ci.bus.server.shell.command.CommandHelper.COMMAND_SIGN;
import com.antelope.ci.bus.server.shell.command.CommandType;
import com.antelope.ci.bus.server.shell.command.ICommand;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年11月17日		下午3:39:05 
 */
public class TestCommandAdapter extends TestCase {
	@Test public void testMatch() {
		System.out.println("test match");
		RightCommand rightCommand = new RightCommand();
		String rightStr = new String(new char[]{(char)NetVTKey.RIGHT});
		String downStr = new String(new char[]{(char)NetVTKey.DOWN});
		if (rightStr.equals(downStr)) 
			System.out.println("down equal right");
		boolean matched = match(rightCommand, rightStr);
		System.out.println("matched : " + matched);
	}
	
	@Command(
			name="right.test", 
			commands=CommandHelper.downCommand, 
			status=BusShellStatus.GLOBAL, 
			type=CommandType.HIT, 
			mode=BusShellMode.MAIN)
	private static class RightCommand implements ICommand {
		@Override
		public String getIdentity() {
			return null;
			
		}
		@Override
		public Command getContent() {
			return null;
		}
		@Override
		public String execute(boolean refresh, BusShell shell, Object... args) {
			return null;
		}
	}
	
	protected boolean match(ICommand command, String cmdStr) {
		Command cmd = command.getClass().getAnnotation(Command.class);
		for (String scmd : cmd.commands().split(",")) {
			if (StringUtil.empty(scmd)) continue;
			if (scmd.length() > 1) 		scmd = scmd.trim();
			
			StringBuffer cmdBuf = new StringBuffer();
			String[] cmdEntries = CommandHelper.split(scmd);
			for (String cmdEntry : cmdEntries) {
				COMMAND_SIGN csign = COMMAND_SIGN.toSign(cmdEntry);
				switch (csign) {
					case NUMBER:
						String c = csign.truncate(cmdEntry);
						if (StringUtil.isNumeric(c))
							cmdBuf.append((char) Integer.parseInt(c));
						break;
					default:
						cmdBuf.append(cmdEntry);
						break;
				}
			}
			if (cmdBuf.toString().equals(cmdStr))
				return true;
			
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestCommandAdapter.class);
	}
}

