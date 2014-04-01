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
import java.util.Map;

import org.apache.sshd.server.Environment;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.server.shell.buffer.ShellCommandArg;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
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
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	public int getSort() {
		return sort;
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
			Object o = ProxyUtil.newObject(caCls);
			if (o == null)
				o = ProxyUtil.newObject(caCls,
						CommonBusActivator.getClassLoader());
			commandAdapter = (CommandAdapter) o;
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
			action();
			if (quit) {
				close();
				break;
			}
			if (multiShell()) {
				if (actionStatus.equals(lastStatus)) {
					BusShell lastShell = shellMap.get(actionStatus);
					lastShell.notify();
					lastShell.refresh();
					waitForWake();
					continue;
				}
				if (!actionStatus.equals(status)) {
					BusShell wakeShell = shellMap.get(actionStatus);
					wakeShell.setLastStatus(status);
					wakeShell.notify();
					if (wakeShell.getSession() == null)
						wakeShell.attatchSession(session);
					if (wakeShell.isOpened())
						wakeShell.refresh();
					else
						wakeShell.open();
					waitForWake();
					continue;
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
						cmdArg.getCommand(), io, cmdArg.getArgs());
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
			status = actionStatus;
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
	
	protected void shift(int x, int y) throws IOException {
		ShellUtil.shift(io, x, y, getConsoleWidth(), getConsoleHeight());
	}
	
	protected void move(int x, int y) throws IOException {
		ShellUtil.move(io, x, y);
	}
	
	protected void shiftTop() throws IOException {
		ShellUtil.shiftTop(io);
	}
	
	protected void shiftBottom() throws IOException {
		ShellUtil.shiftBottom(io, getHeight());
	}
	
	protected void writeHeader(String header) throws IOException {
		ShellUtil.writeHeader(io, header);
	}
	
	protected void writeTail(String tail) throws IOException {
		ShellUtil.writeTail(io, tail, getConsoleWidth(), getConsoleHeight());
	}

	protected void shiftLineEnd() throws IOException {
		io.moveLeft(getConsoleWidth());
	}
	
	protected void shiftUp(int times) throws IOException {
		io.moveUp(times);
	}
	
	protected void shiftDown(int times) throws IOException {
		io.moveDown(times);
	}

	protected void shiftRight(int times) throws IOException {
		io.moveRight(times);
	}
	
	protected void shiftLeft(int times) throws IOException {
		io.moveLeft(times);
	}
	
	protected void shiftNext(String str) throws IOException {
		io.moveLeft(StringUtil.getWordCount(str));
		io.moveDown(1);
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

	protected String readInput(boolean mark) throws IOException {
		int in = io.read();
		StringBuffer strBuf = new StringBuffer();
		while (in != TerminalIO.ENTER) {
			if (in == TerminalIO.DELETE || in == TerminalIO.BACKSPACE) {
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
		}
		io.write(TerminalIO.CRLF);
		return strBuf.toString();
	}
	
	protected void print(ShellText text) {
		try {
			ShellUtil.print(io, text);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void printFormatText(String str) {
		try {
			ShellUtil.printFormatText(io, str);
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

	protected abstract void mainView() throws CIBusException;

	protected abstract void action() throws CIBusException;

	protected abstract void shutdown() throws CIBusException;
}
