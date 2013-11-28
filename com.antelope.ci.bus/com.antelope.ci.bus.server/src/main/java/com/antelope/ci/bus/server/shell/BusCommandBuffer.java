// com.antelope.ci.bus.server.shell.BusCommandBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 * virtual terminal命令缓存
 * 特殊字符的处理，包括终端和缓存
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-26		上午10:10:07 
 */
public class BusCommandBuffer {
	private static final int BUF_SIZE = 1024;
	private static final int TAB_SIZE = 4;
	private CharBuffer buffer;
	private int cursor;
	private TerminalIO io;
	private final int cursorStart;
	private int tipsLines;
	private int tipsCols;
	private List<Integer> blankList;
	private boolean cmdShowed;
	private int cmdShowCursor;
	private int cmdWidth;
	private int cmdCols;
	private int cmdTabLine;
	private boolean inCmdTab;
	private List<String> cmdList;
	
	public BusCommandBuffer(TerminalIO io, int cursorStart) {
		buffer = CharBuffer.allocate(BUF_SIZE);
		this.io = io;
		this.cursorStart = cursorStart == 0 ? 0 : cursorStart - 1;
		blankList = new ArrayList<Integer>();
		reset();
	}
	
	public void setCursor() {
		cursor = buffer.position() + cursorStart;
	}
	
	public boolean cmdShowed() {
		return cmdShowed;
	}
	
	public boolean inCmdTab() {
		return inCmdTab;
	}
	
	// 增加空格光标位
	public void addBlank() {
		resetTabCursor();
		blankList.add(cursor);
	}
	
	// 空格光标位是否存在
	public boolean exitBlank() {
		resetBlankList();
		return blankList.isEmpty() ? false : true;
	}
	
	public void reset() {
		cursor = cursorStart;
		buffer.clear();
		cmdShowed = false;
		tipsLines = 0;
		tipsCols = 0;
		cmdShowCursor = 0;
		cmdWidth = 0;
		cmdCols = 0;
		cmdTabLine = -1;
		inCmdTab = false;
	}
	
	public void put(char c) throws IOException {
		if (inCmdTab)
			return;
		if ((cursor-cursorStart) != buffer.position()) {
			int offset = cursor - cursorStart;
			int count = buffer.position() - offset;
			String fillStr = new String(buffer.array(), offset, count);
			buffer.position(buffer.position() - fillStr.length());
			buffer.put(c);
			buffer.put(fillStr);
			io.eraseToEndOfLine();
			io.write(c + fillStr);
			io.moveLeft(fillStr.length());
			cursor++;
		} else {
			buffer.put(c);
			io.write(c);
			cursor += 1;
		}
	}
	
	public String read() {
		int mark = buffer.position();
		buffer.flip();
		String s = buffer.toString();
		buffer.position(mark);
		buffer.limit(buffer.capacity());
		return s;
	}
	
	public CommandArgs enter(char c) throws CIBusException {
		try {
			io.write(c);
		} catch(IOException e) {
			throw new CIBusException("", e);
		}
		buffer.flip();
		String line = buffer.toString();
		String[] strs = line.split(" ");
		String command = "";
		String[] args = new String[0];
		if (strs.length > 0) {
			command = strs[0];
			args = new String[strs.length-1];
			int n = 0;
			while (n < args.length) {
				args[n] = strs[++n];
			}
		}
		return new CommandArgs(command, args);
	}
	
	public static class CommandArgs {
		private String command;
		private String[] args;
		public CommandArgs(String command, String[] args) {
			super();
			this.command = command;
			this.args = args;
		}
		public String getCommand() {
			return command;
		}
		public String[] getArgs() {
			return args;
		}
	}
	
	// 向右删除多个字符
	public void delete(int times) {
		int n = 0;
		while (n < times) {
			if (!delete())
				break;
		}
	}
	
	// 向右删除1个字符
	public boolean delete() {
		if (inCmdTab)
			return false;
		boolean isDel = false;
		if ((cursor-cursorStart+1) < buffer.position()) {				//　至少有一个字符存在
			isDel = true;
			try {
				int offset = cursor - cursorStart + 1;
				int count = buffer.position() - offset - 1;
				String fillStr = new String(buffer.array(), offset, count);
				buffer.position(buffer.position() - fillStr.length() - 1);
				buffer.put(fillStr);
				io.moveRight(1);	
				io.eraseToEndOfLine();
				io.write(fillStr);
				io.moveLeft(fillStr.length() + 1);
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return isDel;
	}
	
	// 向左删除多个字符
	public void backspace(int times) {
		int n = 0;
		while (n < times) {
			if (!backspace())
				break;
		}
	}
	
	// 向左删除一个字符
	public boolean backspace() {
		if (inCmdTab)
			return false;
		boolean isBack = false;
		if (cursor > cursorStart) {
			isBack = true;
			try {
				int offset = cursor - cursorStart;
				int count = buffer.position() - offset;
				String fillStr = new String(buffer.array(), offset, count);
				buffer.position(buffer.position() - fillStr.length() - 1);
				buffer.put(fillStr);
				io.moveLeft(1);					// 光标左移一位
				io.eraseToEndOfLine();		// 删除光标到行尾部分的内容
				io.write(fillStr);
				io.moveLeft(fillStr.length());
				cursor--;
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return isBack;
	}
	
	public void left(int times) {
		int n = 0;
		while (n < times) {
			if (!left())
				break;
		}
	}
	
	public boolean left() {
		if (inCmdTab)
			return false;
		boolean moved = false;
		if (cursor > cursorStart) {
			moved = true;
			try {
				io.moveLeft(1);
				cursor--;
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return moved;
	}
	
	public void right(int times) {
		int n = 0;
		while (n < times) {
			if (!left())
				break;
		}
	}
	
	public boolean right() {
		if (inCmdTab)
			return false;
		boolean moved = false;
		if ((cursor-cursorStart) < buffer.position()) {
			moved = true;
			try {
				io.moveRight(1);
				cursor++;
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return moved;
	}
	
	public void selectCommand() {
		int position = cmdShowCursor / cmdWidth;
		int cmdIndex = cmdTabLine * cmdCols + position;
		try {
			io.moveUp(cmdTabLine+1);
			io.moveLeft(cmdShowCursor);
			io.moveRight(cursorStart + 1);
			io.eraseToEndOfLine();
			String cmd = cmdList.get(cmdIndex-1);
			io.write(cmd);
			cursor = cursorStart + cmd.length();
			buffer.clear();
			buffer.put(cmd);
		} catch (IOException e) {}
		resetTabInfo();
		cmdShowed = true;
	}
	
	public void tabCommand() {
		int position = cmdShowCursor / cmdWidth;
		int cmdIndex = cmdTabLine * cmdCols + position;
		if (cmdShowed && cmdList != null && !cmdList.isEmpty() && cmdIndex < cmdList.size()) {
			inCmdTab = true;
			try {
				if (cmdShowCursor == cmdWidth || cmdShowCursor == 0) {
					cmdShowCursor = 0;
					io.moveLeft(cursor+1);
					io.moveDown(1);
					cmdTabLine++; 
				}
				cmdIndex = cmdTabLine * cmdCols + position;
				io.eraseLine();
				TabCommandInfo headerInfo = getTabCommandInfo(position, cmdIndex);
				position = headerInfo.getPosition();
				cmdIndex = headerInfo.getCmdIndex();
				io.write(headerInfo.getCmdSets());
				io.setReverse(true);
				io.write(cmdList.get(cmdIndex));
				cmdIndex++;
				io.setReverse(false);
				cmdShowCursor += cmdWidth;
				if (cmdShowCursor < cmdWidth*cmdCols && cmdIndex < cmdList.size()) {
					String tail = getTabCommandInfo(position, cmdIndex).getCmdSets();
					io.write(tail);
				}
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	private TabCommandInfo getTabCommandInfo(int position, int cmdIndex) {
		StringBuffer cmds = new StringBuffer();
		while (position < cmdShowCursor/cmdWidth) {
			String cmd = cmdList.get(cmdIndex);
			cmds.append(cmd);
			int cmdSize = cmd.length();
			while (cmdSize < cmdWidth) {
				cmds.append((char) TerminalIO.BLANK);
				cmdSize++;
			}
			position++;
			cmdIndex++;
		}
		
		return new TabCommandInfo(position, cmdIndex, cmds.toString());
	}
	
	private class TabCommandInfo {
		private int position;
		private int cmdIndex;
		private String cmdSets;
		public TabCommandInfo(int position, int cmdIndex, String cmdSets) {
			super();
			this.position = position;
			this.cmdIndex = cmdIndex;
			this.cmdSets = cmdSets;
		}
		public int getPosition() {
			return position;
		}
		public int getCmdIndex() {
			return cmdIndex;
		}
		public String getCmdSets() {
			return cmdSets;
		}
	}
	
	// tab显示相关命令列表
	public void printCommands(List<String> cmdList, int width) {
		resetTabCursor();
		this.cmdList = cmdList;
		if (cmdList.isEmpty())			// 空命令列表，直接返回
			return;
		
		int maxLen = 0;
		for (String cmd : cmdList) {
			maxLen = cmd.length() > maxLen ? cmd.length() : maxLen;
		}
		cmdWidth = maxLen + TAB_SIZE;
		cmdCols = width / cmdWidth;
		cmdCols = cmdCols == 0 ? 1: cmdCols;
		int n = 0;
		StringBuffer cmdStrs = new StringBuffer();
		while (n < cmdList.size()) {
			if (n % cmdCols == 0) {
				cmdStrs.append('\n');
				tipsLines++;
				tipsCols = 0;
			}
			String cmd = cmdList.get(n);
			cmdStrs.append(cmd);
			int cmdSize = cmd.length();
			while (cmdSize < cmdWidth) {
				cmdStrs.append((char) TerminalIO.BLANK);
				cmdSize++;
			}
			tipsCols += cmdWidth;
			n++;
		}
		if (n > 0) {
			try {
				io.write(cmdStrs.toString());
			} catch (IOException e) { }
			cmdShowed = true;
		}
		try {
			io.moveUp(tipsLines);
			io.moveLeft(tipsCols);
			io.moveRight(cursor + 1);
		} catch (IOException e) { }
	}
	
	private void resetTabCursor() {
		if (tipsLines != 0) {
			try {
				int n = 0;
				while (n < tipsLines) {
					io.moveDown(1);
					io.eraseLine();
					n++;
				}
				io.moveUp(tipsLines);
				resetTabInfo();
			} catch (IOException e) {}
		}
	}
	
	private void resetTabInfo() {
		cmdShowCursor = 0;
		cmdWidth = 0;
		cmdCols = 0;
		cmdTabLine = -1;
		inCmdTab = false;
		cmdShowed = false;
	}
	
	private void resetBlankList() {
		List<Integer> delList = new ArrayList<Integer>();
		for (Integer i : blankList) {
			if (i > cursor) {
				delList.add(i);
			}
		}
		blankList.removeAll(delList);
	}
}

