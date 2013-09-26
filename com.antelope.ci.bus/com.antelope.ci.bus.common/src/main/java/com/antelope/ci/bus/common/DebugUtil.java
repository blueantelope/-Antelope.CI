// com.antelope.ci.bus.common.Debug.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 * 调试工具
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-26		上午10:39:56 
 */
public class DebugUtil {

	/**
	 * assert打开，输出assert调试语句
	 * @param  @param s
	 * @return void
	 * @throws
	 */
	public static void assert_out(String s) {
		if (isAssert())
			System.out.println(s);
	}
	
	/**
	 * assert错误输出
	 * @param  @param s
	 * @return void
	 * @throws
	 */
	public static void assert_err(String s) {
		if (isAssert())
			System.err.println(s);
	}
	
	/*
	 * assert是否打开, -ea参数
	 */
	private static boolean isAssert() {
		boolean opened = false;
		assert opened = true;
		return opened;
	}
}

