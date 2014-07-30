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
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.sshd.server.Environment;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.server.shell.buffer.ShellCommandArg;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
import com.antelope.ci.bus.server.shell.command.CommandAdapterFactory;
import com.antelope.ci.bus.server.shell.core.ConnectionData;
import com.antelope.ci.bus.server.shell.core.TerminalIO;

/**
 * shell view template
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午1:06:49
 */
public abstract class BusShell {
	private static final Logger log = Logger.getLogger(BusShell.class);
	protected BusShellSession session;
	protected TerminalIO io;
	protected ConnectionData setting;
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;
	protected boolean opened;
	protected boolean quit;
	protected boolean keyBell;
	private boolean statusSetted;
	protected String status;
	protected String actionStatus;
	protected String lastStatus;
	protected Map<String, BusShell> shellMap;
	protected CommandAdapter commandAdapter;
	protected ClassLoader cloader;
	protected int sort;
	protected Map<String, ShellPalette> paletteMap;
	
	public BusShell(BusShellSession session) {
		this();
		this.session = session;
		this.sort = -1;
		cloader = CommonBusActivator.getClassLoader() != null 
						? CommonBusActivator.getClassLoader() 
						: this.getClass().getClassLoader();
	}

	public BusShell() {
		opened = false;
		quit = false;
		keyBell = false;
		status = BusShellStatus.ROOT;
		statusSetted = false;
		shellMap = null;
		this.sort = -1;
		init();
		actionStatus = BusShellStatus.INIT;
		lastStatus = BusShellStatus.INIT;
		paletteMap = new HashMap<String, ShellPalette>();
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	public int getSort() {
		return sort;
	}
	
	public void addPalette(String name, ShellPalette palette) {
		paletteMap.put(name,  palette);
	}
	
	public ShellPalette getPalette(String name) {
		return paletteMap.get(name);
	}
	
	private void init() {
		Class clazz = this.getClass();
		for (; commandAdapter == null && BusShell.class.isAssignableFrom(clazz); clazz = clazz
				.getSuperclass()) {
			fetchShellInfo(clazz);
		}
	}

	private void fetchShellInfo(Class clazz) {
		if (clazz.isAnnotationPresent(Shell.class)) {
			Shell sa = (Shell) clazz.getAnnotation(Shell.class);
			String caCls = sa.commandAdapter();
			commandAdapter = CommandAdapterFactory.getAdapter(caCls);
			status = sa.status();
		}
	}

	public void setCommandAdapter(CommandAdapter commandAdapter) {
		this.commandAdapter = commandAdapter;
	}

	public void setShellMap(Map<String, BusShell> shellMap) {
		this.shellMap = shellMap;
	}

	public void attatchSession(BusShellSession session) {
		this.session = session;
	}

	public BusShellSession getSession() {
		return this.session;
	}

	public ConnectionData getSetting() {
		return setting;
	}

	public TerminalIO getIO() {
		return io;
	}

	public void setStatus(String status) {
		if (!statusSetted) {
			this.status = status;
			actionStatus = BusShellStatus.INIT;
			lastStatus = BusShellStatus.INIT;
			statusSetted = true;
		}
	}

	public String getStatus() {
		return this.status;
	}

	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}

	public boolean isOpened() {
		return opened;
	}

	public void open() throws CIBusException {
		opened = true;
		environment();
		clear();
		mainView();
		loopAction();
	}

	private void loopAction() throws CIBusException {
		while (true) {
			log.debug("action start");
			action();
			log.debug("action end");
			if (quit) {
				close();
				break;
			}
			if (multiShell()) {
				if (actionStatus.equals(lastStatus)) {
					BusShell lastShell = shellMap.get(actionStatus);
					synchronized (lastShell) {
						lastShell.notify();
						lastShell.refresh();
						lastShell.loopAction();
						waitForWake();
						continue;
					}	
				}
				if (!actionStatus.equals(status)) {
					BusShell wakeShell = shellMap.get(actionStatus);
					synchronized (wakeShell) {
						wakeShell.notify();
						if (wakeShell.getSession() == null)
							wakeShell.attatchSession(session);
						if (wakeShell.isOpened()) {
							wakeShell.setLastStatus(status);
							wakeShell.refresh();
							wakeShell.loopAction();
						} else {
							wakeShell.open();
						}
						waitForWake();
						continue;
					}
				}
			} else {
				if (lastStatus != BusShellStatus.INIT) {
					if (actionStatus.equals(lastStatus)) {
						refresh();
					}
				}
			}
		}
	}
	
	protected void storeCursor() {
		try {
//			io.storeCursor();
			io.homeCursor();
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	@Deprecated
	protected void restoreCursor() {
//		try {
//			io.restoreCursor();
//			io.homeCursor();
//		} catch (IOException e) {
//			DevAssistant.errorln(e);
//		}
	}

	protected void waitForWake() {
		synchronized (shellMap.get(status)) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	protected void execute(ShellCommandArg cmdArg) {
		if (cmdArg != null && cmdArg.exist()) {
			try {
				actionStatus = commandAdapter.execute(status, !multiShell(),
						cmdArg.getCommand(), this, io, cmdArg.getArgs());
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
			switch (BusShellStatus.toBaseStatus(actionStatus)) {
				case QUIT:
					quit = true;
					break;
				case KEEP:
					actionStatus = status;
					break;
				case LAST:
					actionStatus = lastStatus;
					break;
				default:
					if (!actionStatus.equals(status))
						lastStatus = status;
					break;
			}
		}
	}

	protected boolean multiShell() {
		return shellMap != null ? true : false;
	}

	public void close() throws CIBusException {
		clear();
		shutdown();
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
		if (err != null) {
			try {
				err.close();
			} catch (IOException e) {
			}
		}
		session.getCallback().onExit(0);
	}
	
	protected void refresh() throws CIBusException {
		clear();
		mainView();
	}
	
	protected void shift(int x, int y) throws CIBusException {
		ShellUtil.shift(io, x, y, getConsoleWidth(), getConsoleHeight());
	}
	
	protected void move(int x, int y) throws CIBusException {
		try {
			ShellUtil.move(io, x, y);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	protected void shiftTop() throws CIBusException {
		ShellUtil.shiftTop(io);
	}
	
	protected void shiftBottom() throws CIBusException {
		ShellUtil.shiftBottom(io, getHeight());
	}
	
	protected void writeHeader(String header) throws CIBusException {
		ShellUtil.writeHeader(io, header);
	}
	
	protected void writeTail(String tail) throws CIBusException {
		ShellUtil.writeTail(io, tail, getConsoleWidth(), getConsoleHeight());
	}

	protected void shiftLineEnd() throws CIBusException {
		try {
			io.moveLeft(getConsoleWidth());
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	protected void shiftUp(int times) throws CIBusException {
		try {
			io.moveUp(times);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	protected void shiftDown(int times) throws CIBusException {
		try {
			io.moveDown(times);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}

	protected void shiftRight(int times) throws CIBusException {
		try {
			io.moveRight(times);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	protected void shiftLeft(int times) throws CIBusException {
		try {
			io.moveLeft(times);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}
	
	protected void shiftNext(String str) throws CIBusException {
		ShellUtil.shiftNext(io, str);
	}
	
	private void environment() throws CIBusException {
		in = session.getIn();
		out = session.getOut();
		err = session.getErr();
		setting = session.getSetting();
		io = session.getIo();
		custom();
	}

	protected void println(String text) {
		try {
			io.println(text);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}

	protected void println() {
		try {
			io.println();
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}

	protected void print(String text) throws CIBusException {
		try {
			io.write(text);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}

	protected void printf(String format, Object... values) throws CIBusException {
		String text = String.format(format, values);
		print(text);
	}

	protected void error(String text) throws CIBusException {
		try {
			io.error(text);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}

	protected void errorf(String format, Object... args) throws CIBusException {
		error(String.format(format, args));
	}

	protected void errorln(String text) throws CIBusException {
		try {
			io.errorln(text);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
	}

	protected String readInput(boolean mark) throws CIBusException {
		int in;
		try {
			in = io.read();
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
		StringBuffer strBuf = new StringBuffer();
		while (in != NetVTKey.ENTER) {
			try {
				if (in == NetVTKey.DELETE || in == NetVTKey.BACKSPACE) {
					if (strBuf.length() > 0) {
						ShellUtil.backspace(io);
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
			} catch (IOException e) {
				throw new CIBusException("", e);
			}
		}
		try {
			io.write(NetVTKey.CRLF);
		} catch (IOException e) {
			throw new CIBusException("", e);
		}
		return strBuf.toString();
	}
	
	protected void print(ShellText text) {
		try {
			ShellUtil.print(io, text);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void println(ShellText text) {
		try {
			ShellUtil.println(io, text);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void printFormat(String str) {
		try {
			ShellUtil.printFormat(io, str);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void printlnFormat(String str) {
		try {
			ShellUtil.printlnFormat(io, str);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}

	protected int getConsoleWidth() {
		return setting.getTerminalColumns();
	}

	protected int getConsoleHeight() {
		return setting.getTerminalRows();
	}

	protected void clear() throws CIBusException {
		ShellUtil.clear(io);
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

	public abstract void mainView() throws CIBusException;

	protected abstract void action() throws CIBusException;

	protected abstract void shutdown() throws CIBusException;
	
	public abstract void clearContent() throws CIBusException;
	
	public abstract void writeContent(Object content) throws CIBusException;
}
