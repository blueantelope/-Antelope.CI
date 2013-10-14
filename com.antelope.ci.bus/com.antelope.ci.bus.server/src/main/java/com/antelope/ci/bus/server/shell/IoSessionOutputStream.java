// com.antelope.ci.bus.server.portal.IoSessionOutputStream.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午2:46:26 
 */
public class IoSessionOutputStream extends OutputStream {
    private final IoSession session;

    private WriteFuture lastWriteFuture;

    public IoSessionOutputStream(IoSession session) {
        this.session = session;
    }

    @Override
    public void close() throws IOException {
        try {
            flush();
        } finally {
            session.close(true).awaitUninterruptibly();
        }
    }

    private void checkClosed() throws IOException {
        if (!session.isConnected()) {
            throw new IOException("The session has been closed.");
        }
    }

    private synchronized void write(IoBuffer buf) throws IOException {
        checkClosed();
        WriteFuture future = session.write(buf);
        lastWriteFuture = future;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        write(IoBuffer.wrap(b.clone(), off, len));
    }

    @Override
    public void write(int b) throws IOException {
        IoBuffer buf = IoBuffer.allocate(1);
        buf.put((byte) b);
        buf.flip();
        write(buf);
    }

    @Override
    public synchronized void flush() throws IOException {
        if (lastWriteFuture == null) {
            return;
        }

        lastWriteFuture.awaitUninterruptibly();
        if (!lastWriteFuture.isWritten()) {
            throw new IOException(
                    "The bytes could not be written to the session");
        }
    }
}
