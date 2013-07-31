// com.antelope.ci.bus.CIBus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import com.antelope.ci.bus.common.Constants;


/**
 * CI BUS的程序入口
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		上午10:12:15 
 */
public class CIBus {
	/**
	 * 入口
	 * @param  @param args
	 * @return void
	 * @throws
	 */
	public static void main(String[] args) {
		CIBus bus = new CIBus();
		bus.opts(args);
	}
	
	
	/* 帮助信息 */
	private static final String HELP = "Usage: [OPTION] [VALUE]\n" +
									"\t-e, --etc\tetc direction path\n";
	/* 非法选项信息 */
	private static final String FATAL = "invalid options\n" +
									"\tTry '--help' for more information\n";
	
	private static String etc;								// etc目录
	
	/**
	 * 输入参数处理
	 * @param  @param args
	 * @return void
	 * @throws
	 */
	public void opts(String[] args) {
		int argLen = args.length;
		if (argLen > 0) {
			if ("--help".equalsIgnoreCase(args[0])) {	// 帮助
				System.out.print(HELP);
				System.exit(0);
			}
			// 选项处理
			if (argLen % 2 == 0) {			
				int n = 0;
				while (argLen > n) {
					String key = args[n];
					String value = args[n+1];
					if ("-e".equalsIgnoreCase(key) || 
							"-etc".equalsIgnoreCase(key)) {	// etc目录配置
						etc = value;
						System.setProperty(Constants.ETC_DIR, etc);
					} else {									// 非法选项
						System.err.print(FATAL);
						System.exit(-1);		
					}
					n += 2;
				}
			}
		}
		
		if (etc == null) {
			
		}
	}
	
	/**
	 * 启动
	 * @param  
	 * @return void
	 * @throws
	 */
	public void start() {
		
	}
}

