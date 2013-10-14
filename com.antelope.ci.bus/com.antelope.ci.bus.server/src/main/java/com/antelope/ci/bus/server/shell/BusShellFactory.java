// com.antelope.ci.bus.server.BusPortalShell.java
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

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午12:43:31 
 */
public class BusShellFactory implements Factory<Command> {
	public Command create() {
        return new BusPortalShellCommand();
    }
	
	static class BusPortalShellCommand implements Command, Runnable {
		private InputStream in;
		private OutputStream out;
		private OutputStream err;
		private ExitCallback callback;
		private boolean running;
		private Thread portal_thread;
		private BusShell shell;

		@Override
		public void setInputStream(InputStream in) {
			this.in = in;
		}

		@Override
		public void setOutputStream(OutputStream out) {
			this.out = out;
		}

		@Override
		public void setErrorStream(OutputStream err) {
			this.err = err;
		}

		@Override
		public void setExitCallback(ExitCallback callback) {
			this.callback = callback;
		}

		@Override
		public void start(Environment env) throws IOException {
			running = true;
			portal_thread = new Thread(this, "bus.shell.portal");
			portal_thread.start();
		}

		@Override
		public void destroy() {
			portal_thread.interrupt();
		}

		@Override
		public void run() {
			try {
				BusShellSession session = new BusShellSession(in, out, err);
				shell = new BusPortalShell(session);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			try {
				while (running) {
					try {
						shell.showMainFrame();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} finally {
				try {
					shell.clear();
				} catch (IOException ex) { /* ignore */ }
				try {
					in.close();
				} catch (IOException ex) { /* ignore */ }
				try {
					out.close();
				} catch (IOException ex) { /* ignore */ }
				callback.onExit(0);
			}
		}
	}
}

