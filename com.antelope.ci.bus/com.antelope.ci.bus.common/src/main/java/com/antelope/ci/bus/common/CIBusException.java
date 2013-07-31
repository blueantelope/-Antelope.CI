// com.antelope.ci.bus.common.CIBusException.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * 自定义异常类
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午2:07:54 
 */
public class CIBusException extends Exception {
	private static final String suffix = "CIBusEx";				// 异常定义前缀
	
	private String code;							// 异常代码
	private Throwable cause = null;			// 异常的详细内容
	private String message;						// 异常信息
	
	/**
	 * 
	 * Creates a new instance of ARException.
	 *
	 * @param code
	 */
	public CIBusException(String code) {
		super();
		this.code = code;
		message = suffix + code + " - " + CIBusExceptionContent.getExContent().getException(code);
	}
	
	/**
	 * 
	 * Creates a new instance of ARException.
	 *
	 * @param code
	 * @param value
	 */
	public CIBusException(String code, String value) {
		super();
		this.code = code;
		message = suffix + code + " - " + value;
	}
	
	/**
	 * 
	 * Creates a new instance of ARException.
	 *
	 * @param code
	 * @param cause
	 */
	public CIBusException(String code, Throwable cause) {
		this(code);
		this.cause = cause;
	}
	
	/**
	 * 
	 * Creates a new instance of ARException.
	 *
	 * @param code
	 * @param value
	 * @param cause
	 */
	public CIBusException(String code, String value, Throwable cause) {
		this(code, value);
		this.cause = cause;
	}
	
	/**
	 * 只取得异常代码定义
	 * (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Throwable#getCause()
	 */
	@Override
	public Throwable getCause() {
		if (cause != null)
			return cause;
		
		return super.getCause();
	}

	/**
	 * 增加异常代码打印
	 * (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace() {
		System.err.println(message);
		super.printStackTrace();
	}

	/**
	 * 增加异常代码打印
	 * (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	@Override
	 public void printStackTrace(PrintStream s) {
		 synchronized (s) {
			 s.println(message);
		 }
		 super.printStackTrace(s);
	 }
	
	/**
	 * 增加异常代码打印
	 * (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	@Override
	public void printStackTrace(PrintWriter s) {
		synchronized (s) {
			s.println(message);
		}
		super.printStackTrace(s);
	}
	
	/**
	 * 断言打开，打印异常信息, 用于调试
	 *
	 * @param  @param e   
	 * @return void   
	 * @throws 
	 * @since  0.1
	 */
	public static void assertException(Exception e) {
		boolean isPrint = false;
		assert isPrint = true;
		if (isPrint)
			e.printStackTrace();
	}
}

