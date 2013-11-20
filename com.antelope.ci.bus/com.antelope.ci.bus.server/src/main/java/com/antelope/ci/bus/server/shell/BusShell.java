// com.antelope.ci.bus.server.BusPortal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午1:06:49
 */
public abstract class BusShell {
	protected BusShellSession session;
	protected TerminalIO io;
	protected ConnectionData setting;
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;

	public BusShell(BusShellSession session) {
		this.session = session;
	}

	public ConnectionData getSetting() {
		return setting;
	}

	public TerminalIO getIO() {
		return io;
	}
	
	public void open() throws CIBusException {
		environment();
		clear();
		show();
	}
	
	public void close() throws CIBusException {
		clear();
		shutdown();
	}

	private void environment() throws CIBusException {
		in = session.getIn();
		out = session.getOut();
		err = session.getErr();
		setting = new ConnectionData();
		setting.setNegotiatedTerminalType("vt100");
		io = new TerminalIO(in, out, setting);
		custom();
	}

	protected void println(String text) {
		try {
			io.println(text);
		} catch (IOException e) {
			
		}
	}

	protected void print(String text) throws IOException {
		io.write(text);
	}

	protected void printf(String format, Object... values) throws IOException {
		String text = String.format(format, values);
		print(text);
	}

	protected void error(String text) throws IOException {
		io.error(text);
	}

	protected void errorf(String format, Object... args) throws IOException {
		error(String.format(format, args));
	}

	protected void errorln(String text) throws IOException {
		io.errorln(text);
	}

	protected void resetIo() throws IOException {
		io.setUnderlined(false);
		io.setBold(false);
		io.setReverse(false);
	}

	protected String readInput(boolean mark) throws IOException {
		int in = io.read();
		StringBuffer strBuf = new StringBuffer();
		while (in != TerminalIO.ENTER) {
			if (in == TerminalIO.DELETE || in == TerminalIO.BACKSPACE) {
				if (strBuf.length() > 0) {
					sendDelete();
					strBuf.deleteCharAt(strBuf.length() - 1);
				}
			} else {
				if (!mark) {
					io.write((byte) in);
				} else {
					io.write("*");
				}

				strBuf.append((char) in);
			}
			in = io.read();
		}
		io.write(TerminalIO.CRLF);
		return strBuf.toString();
	}

	public int getConsoleWidth() {
		return setting.getTerminalColumns();
	}

	public int getConsoleHeight() {
		return setting.getTerminalRows();
	}

	protected void sendDelete() throws IOException {
		io.write(new String(new byte[] { 27, '[', '1', 'D' }));// 光标左移一位
		io.write(new String(new byte[] { 27, '[', 'K' }));// 删除光标到行尾部分的内容
		io.flush();
	}
	
    protected void clear () throws CIBusException {
    		try {
    			io.eraseScreen ();
    			io.homeCursor ();
    		} catch (IOException e) {
    			throw new CIBusException("", e);
    		}
    }

	protected abstract void custom() throws CIBusException;
	
	protected abstract void show() throws CIBusException;
	
	protected abstract void shutdown() throws CIBusException;
}
