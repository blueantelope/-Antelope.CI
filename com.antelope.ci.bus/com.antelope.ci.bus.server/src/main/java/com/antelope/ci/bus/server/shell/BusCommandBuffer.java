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
	private List<Integer> blankList;
	private List<String> tipList;
	private int tipLines;
	private int tipCols;
	private int tipLineLimit;
	private boolean tipShowed;
	private int tipShowCursor;
	private int tipWidth;
	private int tipTabCol;
	private int tipTabLine;
	private boolean inTip;
	
	public BusCommandBuffer(TerminalIO io, int cursorStart) {
		buffer = CharBuffer.allocate(BUF_SIZE);
		this.io = io;
		this.cursorStart = cursorStart;
		blankList = new ArrayList<Integer>();
		reset();
	}
	
	public void setCursor() {
		cursor = buffer.position() + cursorStart;
	}
	
	public boolean tipShowed() {
		return tipShowed;
	}
	
	public boolean inCmdTab() {
		return inTip;
	}
	
	// 增加空格光标位
	public void addBlank() {
		clearTips();
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
		tipLines = 0;
		tipCols = 0;
		tipLineLimit = 0;
		tipShowCursor = 0;
		tipWidth = 0;
		tipTabLine = -1;
		tipTabCol = -1;
		tipShowed = false;
		inTip = false;
	}
	
	public void put(char c) throws IOException {
		if (inTip)
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
			cursor++;
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
		if (inTip)
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
		if (inTip)
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
		if (inTip) {
			leftTip();
			return false;
		}
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
		if (inTip) {
			rightTip();
			return false;
		}
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
	
	public void enterTip() {
		try {
			io.moveUp(tipTabLine + 1);
			int m = tipShowCursor - cursor;
			if (m > 0)
				io.moveLeft(m);
			else
				io.moveRight(-m);
		} catch (IOException e) {}
		tipShowCursor = 0;
		tipTabLine = -1;
		tipTabCol = -1;
		inTip = false;
	}
	
	// tab选择命令提示
	public void tabTip() {
		if (tipShowed && tipList != null && !tipList.isEmpty()) {
			inTip = true;
			try {
				tipShowCursor = 0;
				tipTabLine++;
				tipTabCol = 1;
				String tip = tipList.get(0);
				String tipContent = packTip(tip);
				fillTip(0, tip, false);
				io.moveDown(1);
				int m = cursor + 1 - tipWidth;
				if (m > 0)
					io.moveLeft(m);
				else
					io.moveRight(-m);
				io.eraseToBeginOfLine();
				io.moveLeft(tipWidth);
				io.setReverse(true);
				io.write(tipContent);
				io.setReverse(false);
				io.moveLeft(1);
				tipShowCursor = tipWidth - 1;
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	// 左移命令提示
	public void leftTip() {
		int tipIndex = tipTabLine * tipCols + tipTabCol - 1;
		if (inTip && tipIndex > 0) {
			try {
				if (tipShowCursor == tipWidth - 1) {
					String tip = tipList.get(tipIndex - 1);
					String tipContent = packTip(tip);
					fillTip(tipTabLine+1, tip, true);
					io.moveLeft(1);
					io.eraseToBeginOfLine();
					io.moveLeft(tipWidth - 1);
					io.write(packTip(tipList.get(tipIndex)));
					io.moveUp(1);
					io.moveLeft(tipWidth);
					io.moveRight(tipWidth * tipCols - tipWidth);
					io.eraseToEndOfLine();
					io.setReverse(true);
					io.write(tipContent);
					io.setReverse(false);
					tipTabLine--; 
					tipTabCol = tipCols;
					tipShowCursor = tipWidth * tipCols - 1;
				} else {
					String tip = tipList.get(tipIndex - 1);
					String tipContent = packTip(tip);
					String tail = getTabTips(tipTabCol-1, tipIndex).getTips();
					fillTip(tipTabLine+1, tip, true);
					io.moveLeft(tipWidth * 2 - 1);
					if (tipShowCursor == tipWidth * tipCols && tipWidth * tipCols == tipLineLimit) 
						io.moveRight(1);
					io.eraseToEndOfLine();
					io.setReverse(true);
					io.write(tipContent);
					io.setReverse(false);
					io.write(tail);
					io.moveLeft(tail.length());
					tipShowCursor -= tipWidth;
					tipTabCol--;
					if (tipTabCol * tipWidth + tail.length() < tipWidth * tipCols)
						io.moveLeft(1);
				}
			} catch (IOException e) {}
		}
	}
	
	// 右移命令提示
	public void rightTip() {
		int tipIndex = tipTabLine * tipCols + tipTabCol;
		if (inTip && tipIndex < tipList.size()) {
			try {
				if (tipTabCol == tipCols) {
					if (tipList.size() / tipCols > tipTabLine) {
						String tip = tipList.get(tipIndex);
						String tipContent = packTip(tip);
						fillTip(tipTabLine+1, tip, true);
						io.moveLeft(tipWidth - 1);
						io.eraseToEndOfLine();
						io.write(getTabTips(tipTabCol-1, tipIndex-1).getTips());
						io.moveDown(1);
						io.moveLeft(tipWidth * (tipCols - 1));
						io.eraseToBeginOfLine();
						io.moveLeft(tipWidth);
						io.setReverse(true);
						io.write(tipContent);
						io.setReverse(false);
						io.moveLeft(1);
						tipTabLine++; 
						tipTabCol = 1;
						tipShowCursor = tipWidth - 1;
					}
				} else {
					String tip = tipList.get(tipIndex);
					String tipContent = packTip(tip);
					String tail = getTabTips(tipTabCol+1, tipIndex+1).getTips();
					fillTip(tipTabLine+1, tip, true);
					io.moveLeft(tipWidth - 1);
					io.eraseToEndOfLine();
					io.write(packTip(tipList.get(tipIndex-1)));
					io.setReverse(true);
					io.write(tipContent);
					io.setReverse(false);
					io.write(tail);
					io.moveLeft(tail.length());
					tipShowCursor += tipWidth;
					tipTabCol++;
					if (tipTabCol * tipWidth + tail.length() < tipWidth * tipCols)
						io.moveLeft(1);
				}
			} catch (IOException e) {}
		}
	}
	
	public void upTip() {
		if (inTip) {
			
		}
	}
	
	public void downTip() {
		if (inTip) {
			
		}
	}
	
	// shell处放入命令提示
	private void fillTip(int ups, String tip, boolean restore) {
		try {
			if (ups == 0) {
				io.moveLeft(cursor - cursorStart);
			} else {
				io.moveUp(ups);
				int m = tipShowCursor - cursorStart;
				if (m > 0)
					io.moveLeft(m);
				else
					io.moveRight(-m);
			}
			io.eraseToEndOfLine();
			io.write(tip);
			buffer.clear();
			buffer.put(tip);
			cursor = cursorStart + tip.length();
			if (restore) {
				io.moveDown(ups);
				int m = tipShowCursor - cursor;
				if (m > 0)
					io.moveRight(m);
				else 
					io.moveLeft(-m);
			}
		} catch (IOException e) { }
	}
	
	public void selectTip() {
		int position = tipShowCursor / tipWidth;
		int tipIndex = tipTabLine * tipCols + position;
		if (tipShowed && tipList != null && !tipList.isEmpty() && tipIndex < tipList.size()) {
			inTip = true;
			try {
				if (tipShowCursor == tipWidth || tipShowCursor == 0) {
					tipShowCursor = 0;
					io.moveLeft(cursor+1);
					io.moveDown(1);
					tipTabLine++; 
				}
				tipIndex = tipTabLine * tipCols + position;
				io.eraseLine();
				TabCommandTip headerInfo = getTabTips(position, tipIndex);
				position = headerInfo.getPosition();
				tipIndex = headerInfo.getCmdIndex();
				io.write(headerInfo.getTips());
				io.setReverse(true);
				io.write(tipList.get(tipIndex));
				tipIndex++;
				io.setReverse(false);
				tipShowCursor += tipWidth;
				if (tipShowCursor < tipWidth*tipCols && tipIndex < tipList.size()) {
					String tail = getTabTips(position, tipIndex).getTips();
					io.write(tail);
				}
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	private String packTip(String tip) {
		String tipStr = tip;
		int tipSize = tip.length();
		while (tipSize < tipWidth) {
			tipStr += (char) TerminalIO.BLANK;
			tipSize++;
		}
		return tipStr;
	}
	
	private TabCommandTip getTabTips(int position, int tipIndex) {
		StringBuffer tips = new StringBuffer();
		while (position < tipCols && tipIndex < tipList.size()) {
			String tip = tipList.get(tipIndex);
			tips.append(tip);
			int tipSize = tip.length();
			while (tipSize < tipWidth) {
				tips.append((char) TerminalIO.BLANK);
				tipSize++;
			}
			position++;
			tipIndex++;
		}
		
		return new TabCommandTip(position, tipIndex, tips.toString());
	}
	
	private class TabCommandTip {
		private int position;
		private int cmdIndex;
		private String tips;
		public TabCommandTip(int position, int cmdIndex, String tips) {
			super();
			this.position = position;
			this.cmdIndex = cmdIndex;
			this.tips = tips;
		}
		public int getPosition() {
			return position;
		}
		public int getCmdIndex() {
			return cmdIndex;
		}
		public String getTips() {
			return tips;
		}
	}
	
	// tab显示相关命令列表
	public void printTips(List<String> cmdList, int width) {
		clearTips();
		
		if (cmdList.isEmpty())			// 空命令列表，直接返回
			return;
		if (cmdList.size() == 1) {
			try {
				String cmd = cmdList.get(0);
				io.moveLeft(cursor - cursorStart);
				io.write(cmd);
				buffer.clear();
				buffer.put(cmd);
				cursor += cmd.length();
				return;
			} catch (IOException e) { }
		}
		
		this.tipList = cmdList;
		this.tipLineLimit = width;
		int maxLen = 0;
		for (String tip : tipList) {
			maxLen = tip.length() > maxLen ? tip.length() : maxLen;
		}
		tipWidth = maxLen + TAB_SIZE;
		tipCols = width / tipWidth;
		tipCols = tipCols == 0 ? 1 : tipCols;
		int n = 0;
		int tipCursor = 0;
		StringBuffer tips = new StringBuffer();
		while (n < tipList.size()) {
			if (n % tipCols == 0) {
				tips.append('\n');
				tipLines++;
				tipCursor = 0;
			}
			String tip = tipList.get(n);
			tips.append(tip);
			int tipSize = tip.length();
			while (tipSize < tipWidth) {
				tips.append((char) TerminalIO.BLANK);
				tipSize++;
			}
			tipCursor += tipWidth;
			n++;
		}
		if (n > 0) {
			try {
				io.write(tips.toString());
			} catch (IOException e) { }
			tipShowed = true;
		}
		try {
			io.moveUp(tipLines);
			io.moveLeft(tipCursor);
			io.moveRight(cursor);
		} catch (IOException e) { }
	}
	
	public void clearTips() {
		if (tipLines != 0) {
			try {
				int n = 0;
				while (n < tipLines) {
					io.moveDown(1);
					io.eraseLine();
					n++;
				}
				io.moveUp(tipLines);
				resetTabInfo();
			} catch (IOException e) {}
		}
	}
	
	private void resetTabInfo() {
		tipShowCursor = 0;
		tipWidth = 0;
		tipLines = 0;
		tipCols = 0;
		tipLineLimit = 0;
		tipTabLine = -1;
		tipTabCol = -1;
		inTip = false;
		tipShowed = false;
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

