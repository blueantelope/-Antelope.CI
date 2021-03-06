// com.antelope.ci.bus.server.portal.NoCloseDataInputStream.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.shell.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-14		下午1:21:55 
 */
public class NoCloseDataInputStream extends DataInputStream {

	public NoCloseDataInputStream(InputStream in) {
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void close() throws IOException {
    }

}
