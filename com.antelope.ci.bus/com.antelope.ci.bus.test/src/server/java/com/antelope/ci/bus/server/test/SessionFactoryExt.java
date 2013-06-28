// com.antelope.ci.bus.server.test.SessionFactoryExt.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.test;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.sshd.common.session.AbstractSession;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.session.SessionFactory;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-6-11 下午10:17:27
 */
public class SessionFactoryExt extends SessionFactory {

	protected AbstractSession doCreateSession(IoSession ioSession)
			throws Exception {
		System.out.println("建立新的连接");
		AbstractSession session = null;
		try {
			session = new ServerSession(server, ioSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("建立sessoin = " + session);
		AbstractSession.attachSession(ioSession, session);
		System.out.println("附加session = " + ioSession.getAttribute(AbstractSession.SESSION));
		return session;
	}
	
	@Override
	public void messageReceived(IoSession ioSession, Object message) throws Exception {
		System.out.println("接收session = " + ioSession.getAttribute(AbstractSession.SESSION));
		super.messageReceived(ioSession, message);
    }
}
