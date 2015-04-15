// com.antelope.ci.bus.client.SshAttribute.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 *
 * @author blueantelope
 * @version 0.1
 * @Date 2015年4月15日 下午4:09:02
 */
public class SshAttribute extends Attribute {
	protected PipedOutputStream sender;
	protected PipedInputStream in;
	protected ByteArrayOutputStream out;
	protected ByteArrayOutputStream err;

	public SshAttribute() throws IOException {
		super();
		sender = new TeePipedOutputStream(new ByteArrayOutputStream());
		in = new PipedInputStream(sender);
		out = new ByteArrayOutputStream();
		err = new ByteArrayOutputStream();
	}

	public PipedOutputStream getSender() {
		return sender;
	}
	public PipedInputStream getIn() {
		return in;
	}
	public ByteArrayOutputStream getOut() {
		return out;
	}
	public ByteArrayOutputStream getErr() {
		return err;
	}

	private static class TeePipedOutputStream extends PipedOutputStream {
		private OutputStream tee;

		public TeePipedOutputStream(OutputStream tee) {
			this.tee = tee;
		}

		@Override
		public void write(int b) throws IOException {
			super.write(b);
			tee.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			super.write(b, off, len);
			tee.write(b, off, len);
		}
	}
}
