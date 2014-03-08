// com.antelope.ci.bus.common.Debug.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.io.PrintStream;


/**
 * 开发调试辅助工具
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-26		上午10:39:56 
 */
public class DevAssistant {
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
	
	/**
	 * 输出异常
	 * @param  @param s
	 * @return void
	 * @throws
	 */
	public static void assert_exception(Exception e) {
		if (isAssert())
			e.printStackTrace(System.err);
	}
	
	/*
	 * assert是否打开, -ea参数
	 */
	private static boolean isAssert() {
		boolean opened = false;
		assert opened = true;
		return opened;
	}
	
	public static void println(String str) {
		println(System.out, str);
	}
	
	public static void errorln(String err) {
		println(System.err, err);
	}
	
	public static void errorln(Exception e) {
		e.printStackTrace();
	}
	
	private static void println(PrintStream ps, String message) {
		if (System.getProperty(BusConstants.BUS_RUN_MODE) != null && 
				System.getProperty(BusConstants.BUS_RUN_MODE).equalsIgnoreCase(RUN_MODE.DEV.getName())) {
			ps.println(message);
		}
	}
}

