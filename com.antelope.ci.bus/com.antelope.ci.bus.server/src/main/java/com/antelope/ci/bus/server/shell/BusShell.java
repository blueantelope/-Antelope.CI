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
import java.nio.CharBuffer;

import org.apache.sshd.server.Environment;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * shell view template
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午1:06:49
 */
public abstract class BusShell {
	public enum SHELL_TYPE {
		PORTAL, COMMAND
	}
	
	protected BusShellSession session;
	protected TerminalIO io;
	protected ConnectionData setting;
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;
	protected boolean onShell;
	protected boolean quit;
	protected boolean onHelp;
	protected SHELL_TYPE shellType;

	public BusShell(BusShellSession session, SHELL_TYPE shellType) {
		this.session = session;
		this.shellType = shellType;
		onShell = true;
		quit = false;
		onHelp = false;
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
		switch (shellType) {
			case PORTAL:
				actionPortal();
				break;
			case COMMAND:
				try {
					io.write(commandPrompt());
				} catch (IOException e) {
					throw new CIBusException("", e);
				}
				actionCommand();
				break;
		}
	}
	
	protected void actionCommand() {
		try {
			CharBuffer command_buf = CharBuffer.allocate(1024);
			int c;
			while ((c = io.read()) != -1) {
				try {
					io.write((byte) c);
					command_buf.put((char) c);
					if (c == 10) {
						command_buf.flip();
						handleCommand(command_buf.toString());
						command_buf.clear();
						io.println();
						io.write(commandPrompt());
					}
				} catch (Exception e) {
					DevAssistant.errorln(e);
				}
			}
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void actionPortal() {
		while (onShell) {
			if (quit) {
				break;
			}
			try {
				int c = io.read();
				if (c != -1) {
					if (onHelp) {
						switch (c) {
							case 'q':
							case 'Q':
								refresh();
								onHelp = false;
							default:
								keyHelpAnswer(c);
								break;
						}
					} else {
						switch (c) {
							case 'f':
				            case 'F': 		// refresh portal window
				            	refresh();
				            	break;
							case 'q':
							case 'Q':
								close();
								break;
							case 'h':
							case 'H':
								clear();
								io.write(help());
								onHelp = true;
								break;
							default:
								keyAnswer(c);
								break;
						}
					}
				} 
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	public void close() throws CIBusException {
		clear();
		shutdown();
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) { }
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) { }
		}
		if (err != null) {
			try {
				err.close();
			} catch (IOException e) { }
		}
		session.getCallback().onExit(0);
		quit = true;
	}
	
	protected void refresh() throws CIBusException {
		clear();
		show();
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
    
    protected int getHeight() {
    	return Integer.valueOf(getEnv(Environment.ENV_LINES));
    }
    
    protected int getWidth() {
    	return Integer.valueOf(getEnv(Environment.ENV_COLUMNS));
    }
    
    protected String getEnv(String key) {
    	return session.getEnv().getEnv().get(key);
    }

	protected abstract void custom() throws CIBusException;
	
	protected abstract void show() throws CIBusException;
	
	protected abstract void shutdown() throws CIBusException;
	
	protected abstract void keyAnswer(int c) throws CIBusException;
	
	protected abstract String help();
	
	protected abstract void keyHelpAnswer(int c) throws CIBusException;
	
	protected abstract String commandPrompt();
	
	protected abstract  void handleCommand(String command) throws CIBusException;
}
