// com.antelope.ci.bus.CIBus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.antelope.ci.bus.common.CIBusException;
import com.antelope.ci.bus.common.Constants;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.ResourceUtil;


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
		bus.opts(args);				// 参数处理
		bus.start();						// 启动
	}
	
	
	/* 帮助信息 */
	private static final String HELP = "Usage: [OPTION] [VALUE]\n" +
									"\t-e, --etc\tetc direction path\n";
	/* 非法选项信息 */
	private static final String FATAL = "invalid options\n" +
									"\tTry '--help' for more information\n";
	/* 根目录不存在 */
	private static final String HOME_ERROR = "home directory is not exist or not a direcotry";
	
	private static final Logger log = Logger.getLogger(CIBus.class);
	private static String bus_home;								// 根目录
	private static String etc_dir;										// 配置目录
	private static String bundle_dir;								// osgi包目录
	private static String lib_dir;										// 系统jar目录
	
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
					if ("-h".equalsIgnoreCase(key) || 
							"-home".equalsIgnoreCase(key)) {	// etc目录配置
						bus_home = value;
					} else {									// 非法选项
						System.err.print(FATAL);
						System.exit(-1);		
					}
					n += 2;
				}
			}
		}
		
		// 根目录是否设置，如果未设置就将上级目录做为根目录
		// 如果设置了根目录，判断此设置是否正确
		if (bus_home == null || "".equals(bus_home)) {
			try {
				bus_home = ResourceUtil.getJarParent().getPath();
			} catch (CIBusException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		} else {
			if (!FileUtil.existDir(bus_home)) {
				System.err.println(HOME_ERROR);
				System.exit(-1);
			}
		}
	}
	
	
	/**
	 * 启动
	 * @param  
	 * @return void
	 * @throws
	 */
	public void start() {
		init();			// 初始化
	}
	
	/*
	 *  各种初始化汇总
	 */
	private void init() {
		initPath();				// 目录
		initLog();				// 日志
	}
	
	/*
	 * 初始化目录
	 */
	private void initPath() {
		System.setProperty(Constants.BUS_HOME , bus_home);
		etc_dir = bus_home+File.separator + "etc";
		System.setProperty(Constants.ETC_DIR, etc_dir);
		bundle_dir = bus_home+File.separator + "bundle";
		System.setProperty(Constants.BUNDLE_DIR, bundle_dir);
		lib_dir = bus_home+File.separator + "lib";
		System.setProperty(Constants.LIB_DIR, lib_dir);
	}
	
	/*
	 * 初始化日志
	 */
	private void initLog() {
		String log_cnf = etc_dir + File.separator + "log.cnf";
		if (FileUtil.existFile(log_cnf)) {
			PropertyConfigurator.configure(log_cnf);
		} else {
			PropertyConfigurator.configure(Logger.class.getResource("/log4j.properties"));
		}
		log.info("Welcome to Logger World!");
	}
}

