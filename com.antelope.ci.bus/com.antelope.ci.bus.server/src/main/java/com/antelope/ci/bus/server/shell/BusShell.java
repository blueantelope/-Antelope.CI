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
	protected String errorMessage;

	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;

	protected BusShell() {
	}
	
	public BusShell(BusShellSession session) throws IOException {
		this.session = session;
		in = session.getIn();
		out = session.getOut();
		err = session.getErr();

		setting = new ConnectionData();
		setting.setNegotiatedTerminalType("vt100");
		io = new TerminalIO(in, out, setting);

		init();
	}

	public ConnectionData getSetting() {
		return setting;
	}

	public TerminalIO getIO() {
		return io;
	}

	

	private void init() throws IOException {
		if (io.available() > 0) {
			// todo: parse telent protocol
		}
	}

	public void println(String text) throws IOException {
		io.println(text);
	}

	public void print(String text) throws IOException {
		io.write(text);
	}

	public void printf(String format, Object... values) throws IOException {
		String text = String.format(format, values);
		print(text);
	}

	public void error(String text) throws IOException {
		io.error(text);
	}

	public void errorf(String format, Object... args) throws IOException {
		error(String.format(format, args));
	}

	public void errorln(String text) throws IOException {
		io.errorln(text);
	}

	public void clear() throws IOException {
		io.eraseScreen();
		io.homeCursor();
	}


	/*
	 * show help documents
	 */
	protected void help() throws IOException {
		println("test for @Antelope CI BUS");
		boolean quit = false;
		while (!quit) {
			int c = io.read(1);
			switch (c) {
			case 'q':
			case 'Q': // quit help and reutrn portal
				quit = true;
				break;
			default:
				break;
			}
		}
	}

	public boolean changePassword() throws IOException {
		return true;
	}

	protected void resetIo() throws IOException {
		io.setUnderlined(false);
		io.setBold(false);
		io.setReverse(false);
	}

	public String readInput(boolean mark) throws IOException {
		int in = io.read(1);
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
			in = io.read(1);
			// in = io.read ();
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

	public void sendDelete() throws IOException {
		io.write(new String(new byte[] { 27, '[', '1', 'D' }));// 光标左移一位
		io.write(new String(new byte[] { 27, '[', 'K' }));// 删除光标到行尾部分的内容
		io.flush();
	}

	private char confirmChangePassword() {
		return 'c';
	}

	private static boolean isValidInput(String input) {
		if (input == null)
			return false;
		input = input.trim();
		return "yes".equalsIgnoreCase(input) || "y".equalsIgnoreCase(input)
				|| "no".equalsIgnoreCase(input) || "n".equalsIgnoreCase(input);
	}

	public String getErrorMessage() {
		return errorMessage;
	}


	public abstract int login() throws IOException;

	public abstract void showBanner() throws IOException;

	public abstract void showMainFrame() throws IOException;
}
