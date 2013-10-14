// com.antelope.ci.bus.server.portal.IoSessionInputStream.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.shell;

import java.io.IOException;
import java.io.InputStream;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午2:45:51
 */
public class IoSessionInputStream extends InputStream {
	private final Object mutex = new Object();

	private final IoBuffer buf;

	private volatile boolean closed;

	private volatile boolean released;

	private IOException exception;

	public IoSessionInputStream() {
		buf = IoBuffer.allocate(16);
		buf.setAutoExpand(true);
		buf.limit(0);
	}

	@Override
	public int available() {
		if (released) {
			return 0;
		}

		synchronized (mutex) {
			return buf.remaining();
		}
	}

	@Override
	public void close() {
		if (closed) {
			return;
		}

		synchronized (mutex) {
			closed = true;
			releaseBuffer();

			mutex.notifyAll();
		}
	}

	@Override
	public int read() throws IOException {
		synchronized (mutex) {
			if (!waitForData()) {
				return -1;
			}

			return buf.get() & 0xff;
		}
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		synchronized (mutex) {
			if (!waitForData()) {
				return -1;
			}

			int readBytes;

			if (len > buf.remaining()) {
				readBytes = buf.remaining();
			} else {
				readBytes = len;
			}

			buf.get(b, off, readBytes);

			return readBytes;
		}
	}

	private boolean waitForData() throws IOException {
		if (released) {
			return false;
		}

		synchronized (mutex) {
			while (!released && buf.remaining() == 0 && exception == null) {
				try {
					mutex.wait();
				} catch (InterruptedException e) {
					IOException ioe = new IOException(
							"Interrupted while waiting for more data");
					ioe.initCause(e);
					throw ioe;
				}
			}
		}

		if (exception != null) {
			releaseBuffer();
			throw exception;
		}

		if (closed && buf.remaining() == 0) {
			releaseBuffer();

			return false;
		}

		return true;
	}

	private void releaseBuffer() {
		if (released) {
			return;
		}

		released = true;
	}

	public void write(IoBuffer src) {
		synchronized (mutex) {
			if (closed) {
				return;
			}

			if (buf.hasRemaining()) {
				this.buf.compact();
				this.buf.put(src);
				this.buf.flip();
			} else {
				this.buf.clear();
				this.buf.put(src);
				this.buf.flip();
				mutex.notifyAll();
			}
		}
	}

	public void throwException(IOException e) {
		synchronized (mutex) {
			if (exception == null) {
				exception = e;

				mutex.notifyAll();
			}
		}
	}
}
