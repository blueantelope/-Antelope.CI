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
import com.antelope.ci.bus.server.shell.ShellUtil;
import com.antelope.ci.bus.server.shell.buffer.ShellArea.DIRECTION;
import com.antelope.ci.bus.server.shell.core.TerminalIO;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-10		下午3:34:01 
 */
public abstract class BusAreaBuffer extends BusBuffer {
	protected ShellArea area;
	protected int put_index;
	protected char[] put_chars;
	protected int latter_index;
	protected char[] latter_chars;
	protected int successor_num;

	public BusAreaBuffer(TerminalIO io, ShellArea area) {
		super(io);
		this.area = area;
		this.put_index = 0;
		this.put_chars = null;
		this.latter_index = 0;
		this.latter_chars = null;
		this.successor_num = 0;
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
			earse(0);
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
		if (area.distanceToLimit() > 1) {
			earse(1);
			op = true;
		}
		
		return op;
	}
	
	private void earse(int bacOrDel) throws CIBusException {
		try {
			String fromBuffer = read();
			int area_index = area.index();
			if (bacOrDel == 0)
				area_index--;
			String latter_half = StringUtil.subStringVT(fromBuffer, area_index);
			int count = StringUtil.placeholder(latter_half.charAt(0));
			int from_index = fromBuffer.length() - latter_half.length();
			int back_count = 1;
			int remove_length = 1;
			if (count > 0) {
				back_count++;
				remove_length += count;
			}
			remove(from_index, remove_length);
			if (bacOrDel == 0)
				back(back_count);
			else
				area.decrease(back_count);
			fromBuffer = read();
			
			int lines = area.linesToLimit();
			int n = 0;
			int x = area.getPositionx();
			int y = area.getPositiony();
			do {
				if (n != 0) {
					x = area.getOriginx();
					ShellUtil.shift(io, x, y);
				}
				io.eraseToEndOfLine();
				rewriteLatter(x, y-lines);
				if (area.distanceToLimit() > 0) {
					ShellUtil.shift(io, x, y);
					int rewrite_start =   area.distanceFromOrigin(x, y);
					int rewrite_end = rewrite_start + area.getWidth();
					io.write(StringUtil.subStringVT(fromBuffer, rewrite_start, rewrite_end).getBytes());
				}
				y++; n++;
			} while (n < lines);
			
			ShellUtil.shift(io, area.getPositionx(), area.getPositiony());
		} catch (IOException e) {
			throw new CIBusException("", "delete error", e);
		}
	}
	
	private void back(int backs) throws CIBusException {
		try {
			io.moveLeft(backs);
			area.back(backs);
		} catch (IOException e) {
			throw new CIBusException("", "buffer back error", e);
		}
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
		if (area.atEnd())
			return;
		
		try {
			io.write(c);
			if (successor_num > 0)
				successor_num--;
			if (successor_num == 0 && put_index == 0) {
				successor_num = StringUtil.placeholder(c);
				put_chars = new char[successor_num+1];
				gen_latter_chars();
			}
			put_chars[put_index++] = c;
			if (successor_num == 0) {
				char[] insert_chars = put_chars;
				if (latter_chars != null) {
					io.write(latter_chars);
					read(latter_index);
					io.moveLeft(StringUtil.lengthVT(new String(latter_chars)));
					buffer.position(latter_index);
					insert_chars = StringUtil.concat(put_chars, latter_chars);
				}
				buffer.put(insert_chars);
				area.go(StringUtil.lengthVT(new String(put_chars)));
				put_index = 0;
			}
		} catch (IOException e) {
			throw new CIBusException("", e);
		}	
	}
	
	protected void gen_latter_chars() throws CIBusException {
		if (buffer.position() ==0)
			return;
		
		latter_index = StringUtil.subStringVT(read(), 0, area.index()).getBytes().length;
		int length = buffer.position() - latter_index;
		if (length > 0) {
			latter_chars = new char[length];
			read(latter_index, latter_chars);
		}
		
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
					io.moveLeft(1);
					break;
				case LEFT_UP:
					io.moveUp(1);
					io.moveRight(area.getWidth());
					break;
				case RIGHT:
					io.moveRight(1);
					break;
				case RIGTH_DOWN:
					io.moveDown(1);
					io.moveLeft(area.getWidth());
					break;
				default:
					break;
			}
			area.move(direction);
		} catch (IOException e) {
			throw new CIBusException("", "cursor move error", e);
		}
	}
	
	protected void write(byte[] bs) throws IOException {
		int n = 0;
		int size = bs.length;
		while (n < size) {
			char c = (char) bs[n++];
			int placeholder = StringUtil.placeholder(c);
			if (placeholder == 0) {
				io.write(c);
				area.go();
				headDown();
			} else {
				char[] cs = new char[placeholder + 1];
				cs[0] = c;
				int m = 1;
				while (m < placeholder && n < size) {
					cs[m++] = (char) bs[n++];
					if (m < placeholder)
						headDown();
				}
				io.write(cs);
				headDown();
			}
		}
	}
	
	protected void headDown() throws IOException {
		if (area.newLine()) {
			io.moveDown(1);
			io.moveLeft(area.getWidth());
		}
	}
	
	protected abstract void rewriteAhead(int x, int y);
	
	protected abstract void rewriteLatter(int x, int y);
}