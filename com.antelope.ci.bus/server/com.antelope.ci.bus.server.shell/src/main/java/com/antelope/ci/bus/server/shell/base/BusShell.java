// com.antelope.ci.bus.server.BusPortal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.shell.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.common.BusChannel;
import com.antelope.ci.bus.server.common.BusSession;
import com.antelope.ci.bus.server.shell.base.BusShellMode.BaseShellMode;
import com.antelope.ci.bus.server.shell.buffer.BusShellBuffer;
import com.antelope.ci.bus.server.shell.buffer.ShellCommandArg;
import com.antelope.ci.bus.server.shell.command.CommandAdapter;
import com.antelope.ci.bus.server.shell.command.CommandAdapterFactory;
import com.antelope.ci.bus.server.shell.util.ConnectionData;
import com.antelope.ci.bus.server.shell.util.ShellUtil;
import com.antelope.ci.bus.server.shell.util.TerminalIO;

/**
 * shell view template
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午1:06:49
 */
public abstract class BusShell extends BusChannel {
	private static final Logger log = Logger.getLogger(BusShell.class);
	protected TerminalIO io;
	protected ConnectionData setting;
	protected boolean keyBell;
	private boolean statusSetted;
	protected String status;
	protected String actionStatus;
	protected String lastStatus;
	protected Map<String, BusShell> shellMap;
	protected CommandAdapter commandAdapter;
	protected int sort;
	protected Map<String, ShellPalette> paletteMap;
	protected BusShellBuffer input;
	protected boolean activeMoveAction;
	protected boolean activeEditAction;
	protected boolean activeUserAction;
	protected String mode;
	protected int controlKey;
	protected ShellCommandArg cmdArg;
	protected volatile boolean editMode;
	protected volatile boolean lastEditMode;
	
	public BusShell() {
		super();
	}
	
	public BusShell(BusSession session) {
		super(session);
		this.sort = -1;
	}

	public void clearData() {
		input.reset();
	}
	
	public void replaceBuffer(BusShellBuffer buffer) {
		this.input = buffer;
	}
	
	public int getControlKey() {
		return controlKey;
	}
	
	public boolean useMoveAction() {
		return activeMoveAction;
	}

	public void openMoveAction() {
		this.activeMoveAction = true;
	}
	
	public void closeMoveAction() {
		this.activeMoveAction = false;
	}
	
	public boolean useEditAction() {
		return activeEditAction;
	}

	public void openEditAction() {
		this.activeEditAction = true;
	}
	
	public void closeEditAction() {
		this.activeMoveAction = false;
	}
	
	public void openUserAction() {
		this.activeUserAction = true;
	}
	
	public void closeUserAction() {
		this.activeUserAction = false;
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
	
	@Override
	protected void init() {
		quit = false;
		keyBell = false;
		status = BusShellStatus.ROOT;
		statusSetted = false;
		shellMap = null;
		this.sort = -1;
		Class clazz = this.getClass();
		for (; commandAdapter == null && BusShell.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
			fetchShellInfo(clazz);
		}
		actionStatus = BusShellStatus.INIT;
		lastStatus = BusShellStatus.INIT;
		paletteMap = new HashMap<String, ShellPalette>();
		activeMoveAction = false;
		activeEditAction = false;
		activeUserAction = false;
		mode = BusShellMode.MAIN;
		controlKey = -1;
		editMode = false;
		lastEditMode = false;
	}

	private void fetchShellInfo(Class clazz) {
		if (clazz.isAnnotationPresent(Shell.class)) {
			Shell sa = (Shell) clazz.getAnnotation(Shell.class);
			String caCls = sa.commandAdapter();
			commandAdapter = CommandAdapterFactory.getAdapter(caCls, getClassLoader());
			status = sa.status();
		}
	}

	public void setCommandAdapter(CommandAdapter commandAdapter) {
		this.commandAdapter = commandAdapter;
	}

	public void setShellMap(Map<String, BusShell> shellMap) {
		this.shellMap = shellMap;
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
	
	public String getMode() {
		return this.mode;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void enterMainMode() {
		this.mode = BaseShellMode.MAIN.getName();
	}
	public void enterInputMode() {
		this.mode = BaseShellMode.INPUT.getName();
	}
	public void enterEditMode() {
		this.mode = BaseShellMode.EDIT.getName();
	}

	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}

	public boolean isOpened() {
		return opened;
	}
	
	public void runCommand(String name) {
		try {
			commandAdapter.execute(this, !multiShell(), name);
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}

	@Override
	protected void load() throws CIBusException {
		refresh();
		loopAction();
	}
	
	private void reload() throws CIBusException {
		load();
	}
	
	private void loopAction() throws CIBusException {
		int c = -1;
		boolean activeAnotherShell = false;
		boolean activeLastShell = false;
		BusShell activeShell = null;
		while (true) {
			controlKey = -1;
			handleMode();
			try {
				c = io.read();
			} catch (IOException e) {
				throw new CIBusException("", e);
			}
			if (c == -1)
				break;
			if (handleInput(c))
				continue;
			resetControlKey(c);
			try {
				boolean ran = false;
				ran = defaultAction(c);
				if (!ran) {
					if (activeUserAction)
						ran = userAction(c);
					if (!ran)
						action(c);
				}
			} catch (Exception e) {
				DevAssistant.assert_exception(e);
				throw new CIBusException("", e);
			}
			
			if (quit) {
				close();
				break;
			}
			if (multiShell()) {
				if (actionStatus.equals(lastStatus)) {
					activeShell = shellMap.get(actionStatus);
					activeAnotherShell = true;
					activeLastShell = true;
					break;
				}
				if (!actionStatus.equals(status)) {
					activeShell = shellMap.get(actionStatus);
					activeAnotherShell = true;
					break;
				}
			} else {
				if (lastStatus != BusShellStatus.INIT) {
					if (actionStatus.equals(lastStatus))
						refresh();
				}
			}
		}
		
		if (activeAnotherShell) {
			if (activeShell.getSession() == null)
				activeShell.attatchSession(session);
			if (activeShell.isOpened()) {
				if (!activeLastShell)
					activeShell.setLastStatus(status);
				activeShell.reload();
			} else {
				activeShell.open();
			}
		}
	}
	
	protected void storeCursor() {
		try {
			io.homeCursor();
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	@Deprecated
	protected void restoreCursor() {

	}

	protected void execute(ShellCommandArg cmdArg) {
		if (cmdArg != null && cmdArg.exist()) {
			try {
				actionStatus = commandAdapter.execute(this, !multiShell(), cmdArg.getCommand(), cmdArg.getArgs());
			} catch (CIBusException e) {
				actionStatus = BusShellStatus.KEEP;
				DevAssistant.errorln(e);
			}
			if (actionStatus == null)
				actionStatus = BusShellStatus.KEEP;
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

	@Override
	protected void release() throws CIBusException {
		clear();
		shutdown();
	}
	
	public void refresh() throws CIBusException {
		clear();
		mainView();
	}
	
	public void shift(int x, int y) throws CIBusException {
		ShellUtil.shift(io, x, y, getConsoleWidth(), getConsoleHeight());
	}
	
	public void move(int x, int y) throws CIBusException {
		ShellUtil.move(io, x, y);
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
	
	protected void customEnv() throws CIBusException {
		BusShellSession shellSession = (BusShellSession) session;
		setting = shellSession.getSetting();
		io = shellSession.getIo();
		customShellEnv();
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
		while (in != NetVTKey.LF) {
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
		return ((BusShellSession) session).getHeigth();
	}

	protected int getWidth() {
		return ((BusShellSession) session).getWidth();
	}
	
	public void left() {
		input.left();
	}
	
	public void right() {
		input.right();
	}
	
	public void up() {
		input.up();
	}
	
	public void down() {
		input.down();
	}
	
	protected void delete() throws CIBusException {
		input.delete();
	}
	
	protected void backspace() throws CIBusException {
		input.backspace();
	}
	
	protected void space() throws CIBusException {
		input.space();
	}
	
	protected void onKeyVoice() {
		if (keyBell) {
			try {
				io.bell();
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected boolean defaultAction(int key) {
		onKeyVoice();
		if (moveAction(key))	return true;
		if (editAction(key))		return true;
		return false;
	}
	
	protected boolean moveAction(int key) {
		if (activeMoveAction) {
			switch (key) {
				case NetVTKey.LEFT:
					input.left();
					return true;
				case NetVTKey.RIGHT:
					input.right();
					return true;
				case NetVTKey.UP:
					input.up();
					return true;
				case NetVTKey.DOWN:
					input.down();
					return true;
				default:
					return false;
			}
		}
		
		return false;
	}
	
	protected boolean editAction(int key) {
		if (activeEditAction) {
			switch (key) {
				case NetVTKey.DELETE:
					try {
						input.delete();
					} catch (CIBusException e) {
						DevAssistant.errorln(e);
					}
					return true;
				case NetVTKey.BACKSPACE:
					try {
						input.backspace();
					} catch (CIBusException e) {
						DevAssistant.errorln(e);
					}
					return true;
				case NetVTKey.SPACE:
					input.space();
					return true;
				default:
					return false;
			}
		}
		
		return false;
	}

	protected boolean actionForContorl() {
		if (controlKey != -1)
			return true;
		return false;
	}
	
	protected void resetControlKey(int c) {
		controlKey = -1;
		for (int k : NetVTKey.Set) {
			if (k == c) {
				controlKey = k;
				break;
			}
		}
	}

	protected abstract void customShellEnv() throws CIBusException;
	
	protected abstract boolean userAction(int c) throws CIBusException;
	
	public abstract void mainView() throws CIBusException;

	protected abstract void action(int c) throws CIBusException;

	protected abstract void shutdown() throws CIBusException;
	
	public abstract void clearContent() throws CIBusException;
	
	public abstract void moveContent() throws CIBusException;
	
	public abstract void writeContent(Object content) throws CIBusException;
	
	protected abstract boolean handleInput(int c);
	
	protected abstract void handleMode();
}
