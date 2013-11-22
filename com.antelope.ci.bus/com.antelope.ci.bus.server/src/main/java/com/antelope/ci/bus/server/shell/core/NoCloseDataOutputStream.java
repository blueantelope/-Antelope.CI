// com.antelope.ci.bus.server.portal.NoCloseDataOutputStream.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午1:21:23 
 */
public class NoCloseDataOutputStream extends DataOutputStream {
	public NoCloseDataOutputStream(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	@Override
    public void close() throws IOException {
    }
}

