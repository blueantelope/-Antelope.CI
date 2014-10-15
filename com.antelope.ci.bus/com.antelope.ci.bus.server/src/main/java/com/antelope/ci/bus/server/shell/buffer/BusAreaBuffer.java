// com.antelope.ci.bus.server.shell.buffer.BusScreenBuffer.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.buffer;

import java.io.IOException;
import java.util.List;

import com.antelope.ci.bus.common.ASCII;
import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-10		下午3:34:01 
 */
public class BusAreaBuffer extends BusBuffer {
	protected ShellArea area;

	public BusAreaBuffer(TerminalIO io, ShellArea area) {
		super(io);
		this.area = area;
	}

	public BusAreaBuffer(TerminalIO io, int x, int y, int width, int height) {
		this(io, new ShellArea(new ShellCursor(x, y), width, height));
	}
	
	public BusAreaBuffer(TerminalIO io, ShellArea area, int bufSize) {
		super(io, bufSize);
		this.area = area;
	}

	public BusAreaBuffer(TerminalIO io, int x, int y, int width, int height, int bufSize) {
		super(io, bufSize);
		this.area = new ShellArea(new ShellCursor(x, y), width, height);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#backspace()
	 */
	@Override
	public boolean backspace() throws CIBusException {
		boolean op = false;
		if (!area.locateStart()) {
			try {
				io.eraseToBeginOfLine();
			} catch (IOException e) {
				throw new CIBusException("", "backspace error", e);
			}
			buffer.position(buffer.position()-1);
			op = true;
		}
		
		return op;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#delete()
	 */
	@Override
	public boolean delete() throws CIBusException {
		boolean op = false;
		if (!area.locateEnd()) {
			try {
				io.eraseToEndOfLine();
			} catch (IOException e) {
				throw new CIBusException("", "delete error", e);
			}
			buffer.position(buffer.position()-1);
			op = true;
		}
		
		return op;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#left()
	 */
	@Override
	public boolean left() {
		DIRECTION direction = area.directMovetLeft();
		try {
			move(direction);
			return true;
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#right()
	 */
	@Override
	public boolean right() {
		DIRECTION direction = area.directMoveRight();
		try {
			move(direction);
			return true;
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#down()
	 */
	@Override
	public boolean down() {
		DIRECTION direction = area.directMoveDown();
		try {
			move(direction);
			return true;
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#up()
	 */
	@Override
	public boolean up() {
		DIRECTION direction = area.directMoveUp();
		try {
			move(direction);
			return true;
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#tab()
	 */
	@Override
	public void tab() {
		try {
			put((char) ASCII.HT.getDec());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#space()
	 */
	@Override
	public void space() {
		try {
			put((char) ASCII.SPACE.getDec());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#tabTip()
	 */
	@Override
	public void tabTip() {
		// nothing
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#printTips(java.util.List, int)
	 */
	@Override
	public void printTips(List<String> cmdList, int width) {
		// nothing
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#exitSpace()
	 */
	@Override
	public boolean exitSpace() {
		// nothing
		return false;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.buffer.BusBuffer#put(char)
	 */
	@Override
	public void put(char c) throws CIBusException {
		try {
			int index = area.index();
			char[] latter = fromPosition(index);
			char[] insertValue = null;
			if (latter != null) {
				insertValue = StringUtil.insert(latter, c);
				io.write(insertValue);
				io.moveLeft(latter.length);
			} else {
				io.write(c);
			}
			insert(index, c, insertValue);
			area.go();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	protected void insert(int index, char c, char[] insertValue) {
		if (insertValue != null) {
			buffer.position(index);
			buffer.put(insertValue);
		} else {
			buffer.put(c);
		}
	}
	
	protected char[] fromPosition(int index) {
		int length = buffer.position() - index;
		char[] latter = null;
		if (length > 0) {
			latter = new char[length];
			read(index, latter);
		}
		
		return latter;
	}
	
	protected void move(DIRECTION direction) throws CIBusException {
		try {
			switch (direction) {
				case UP:
					io.moveUp(1);
					break;
				case DOWN:
					io.moveDown(1);
					break;
				case LEFT:
				case LEFT_UP:
					io.moveLeft(1);
					break;
				case RIGHT:
				case RIGTH_DOWN:
					io.moveRight(1);
					break;
				default:
					break;
			}
			area.move(direction);
		} catch (IOException e) {
			throw new CIBusException("", "cursor move error", e);
		}
	}
}