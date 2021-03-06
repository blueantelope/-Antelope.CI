// com.antelope.ci.bus.server.shell.BusCommandBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.util.TerminalIO;


/**
 * virtual terminal命令缓存
 * 特殊字符的处理，包括终端和缓存
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-26		上午10:10:07 
 */
public class BusShellEchoBuffer extends BusShellBuffer {
	private int cursor;
	private final int cursorStart;
	private List<Integer> spaceList;
	private List<String> tipList;
	private int tipLines;
	private int tipCols;
	private int tipLineLimit;
	private boolean tipShowed;
	private int tipShowCursor;
	private int tipWidth;
	private int tipTabCol;
	private int tipTabLine;
	
	public BusShellEchoBuffer(TerminalIO io, int cursorStart) {
		super(io);
		this.io = io;
		this.cursorStart = cursorStart;
		spaceList = new ArrayList<Integer>();
		reset();
	}
	
	public void setCursor() {
		cursor = buffer.position() + cursorStart;
	}
	
	public boolean tipShowed() {
		return tipShowed;
	}
	
	// 增加空格光标位
	public void addSpace() {
		clearTips();
		spaceList.add(cursor);
	}
	
	// 空格光标位是否存在
	public boolean exitSpace() {
		resetSpaceList();
		return spaceList.isEmpty() ? false : true;
	}
	
	public void reset() {
		super.reset();
		cursor = cursorStart;
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
	
	public void put(char c) throws CIBusException {
		try {
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
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusShellBuffer#delete()
	 */
	@Override public boolean delete() throws CIBusException {
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

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusShellBuffer#backspace()
	 */
	@Override public boolean backspace() throws CIBusException {
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
	
	public boolean up() {
		if (inTip) {
			upTip();
			return false;
		}
		boolean moved = false;
		return false;
	}
	
	public boolean down() {
		if (inTip) {
			downTip();
			return false;
		}
		boolean moved = false;
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
					io.moveLeft(tipWidth);
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
					io.eraseToEndOfLine();
					io.setReverse(true);
					io.write(tipContent);
					io.setReverse(false);
					io.write(tail);
					io.moveLeft(tail.length());
					tipShowCursor -= tipWidth;
					tipTabCol--;
					if (tipTabCol == tipCols) {
						io.moveLeft(1);
						tipShowCursor -= 1;
					} else if (tipTabLine + 1 == tipLines && tipList.size() % tipTabCol < tipCols) {
						io.moveLeft(1);
					}
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
					if (tipCols *  (tipTabLine + 1) <= tipList.size() && tipCols < tipList.size())
						io.moveLeft(tail.length());
					else
						io.moveLeft(tail.length() + 1);
					tipShowCursor += tipWidth;
					tipTabCol++;
				}
			} catch (IOException e) {}
		}
	}
	
	// 上移命令提示
	public void upTip() {
		int tipIndex = tipTabLine * tipCols + tipTabCol - 1;
		if (inTip && tipTabLine != 0) {
			upOrDownTip(tipIndex, true);
		}
	}
	
	// 下移命令提示
	public void downTip() {
		int tipIndex = tipTabLine * tipCols + tipTabCol - 1;
		if (inTip && tipTabLine <  tipLines && (tipIndex + tipCols) < tipList.size()) {
			upOrDownTip(tipIndex, false);
		}
	}
	
	private void upOrDownTip(int tipIndex, Boolean up) {
		try {
			String curTips = getTabTips(tipTabCol-1, tipIndex).getTips();
			if (up)
				tipIndex -= tipCols;
			else
				tipIndex += tipCols;
			String tip = tipList.get(tipIndex);
			String tipContent = packTip(tip);
			String tail = getTabTips(tipTabCol, tipIndex+1).getTips();
			fillTip(tipTabLine+1, tip, true);
			io.moveLeft(tipWidth - 1);
			io.eraseToEndOfLine();
			io.write(curTips);
			if (up)
				io.moveUp(1);
			else
				io.moveDown(1);
			io.moveLeft(curTips.length());
			if (curTips.length() < tipWidth * tipCols && tipTabCol * tipWidth + tail.length() < tipWidth * tipCols)
				io.moveRight(1);
			io.eraseToEndOfLine();
			io.setReverse(true);
			io.write(tipContent);
			io.setReverse(false);
			io.write(tail);
			io.moveLeft(tail.length());
			if (up)
				tipTabLine--;
			else
				tipTabLine++;
			if (tipTabCol * tipWidth + tail.length() < tipWidth * tipCols)
				io.moveLeft(1);
		} catch (IOException e) {}
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
			tipStr += (char) NetVTKey.SPACE;
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
				tips.append((char) NetVTKey.SPACE);
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
				cursor = cursorStart  + cmd.length();
				return;
			} catch (IOException e) { }
		}
		
		this.tipList = cmdList;
		this.tipLineLimit = width;
		int maxLen = 0;
		for (String tip : tipList) {
			maxLen = tip.length() > maxLen ? tip.length() : maxLen;
		}
		tipWidth = maxLen + tabSize;
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
				tips.append((char) NetVTKey.SPACE);
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
	
	private void resetSpaceList() {
		List<Integer> delList = new ArrayList<Integer>();
		for (Integer i : spaceList) {
			if (i > cursor) {
				delList.add(i);
			}
		}
		spaceList.removeAll(delList);
	}

	@Override
	public void tab() {
		// nothing to do
	}

	@Override
	public void space() {
		try {
			put((char) NetVTKey.SPACE);
			addSpace();
		} catch(CIBusException e) {
			DevAssistant.errorln(e);
		}
	}

	@Override public ShellCommandArg enter() {
		if (inTip) {
			enterTip();
			clearTips();
			return null;
		}
		
		if (tipShowed)
			clearTips();
		return super.enter();
	}
}

