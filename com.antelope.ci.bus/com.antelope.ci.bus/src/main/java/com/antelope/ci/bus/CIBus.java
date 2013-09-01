// com.antelope.ci.bus.CIBus.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.apache.felix.framework.FrameworkFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;
import org.osgi.service.startlevel.StartLevel;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.JarBusProperty;
import com.antelope.ci.bus.common.JarLoadMethod;
import com.antelope.ci.bus.common.ResourceUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


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
	 * @throws CIBusException 
	 */
	public static void main(String[] args) throws CIBusException {
		CIBus bus = new CIBus();
		bus.opts(args);					// 参数处理
		bus.start();						// 启动
	}
	
	// 运行模式
	public enum RUN_MODE {
		DEV("dev", "开发模式"),							// 开发中使用的运行模式，不会用到缓存
		APP("app", "应用模式");							// 实际的应用模式，拥有全部功能
		
		private String name;			// 表示名称
		private String value;			// 显示名称
		private RUN_MODE(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
		
		public String getValue() {
			return value;
		}
		
		/**
		 * 返回value
		 * (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return value;
		}
		
		/**
		 * 由给定的表示名称转换为运行模式
		 * @param  @param name
		 * @param  @return
		 * @return RUN_MODE
		 * @throws
		 */
		public static RUN_MODE toMode(String name) {
			if (name != null && !"".equals(name)) {
				for (RUN_MODE mode : RUN_MODE.values()) {
					if (name.trim().equalsIgnoreCase(mode.getName())) {
						return mode;
					}
				}
			}
			
			return null;
		}
	}
	
	/* 帮助信息 */
	private static final String HELP = "Usage: [OPTION] [VALUE]\n" +
									"\t-e, --etc\tetc direction path\n" +
									"\t-m, --mode\run mode(dev||app)\n";
	/* 非法选项信息 */
	private static final String FATAL = "invalid options\n" +
									"\tTry '--help' for more information\n";
	/* 根目录不存在 */
	private static final String HOME_ERROR = "home directory is not exist or not a direcotry";
	
	private static ClassLoader classloader;						// 系统classLoader
	private static FrameworkFactory factory;					// osgi factory
	private static Framework framework;							// osgi framework
	private static Map<String, String> parameters;			// 参数， 主要是针对osgi 
	private static List<URL> clsUrlList;							// classloader url
	
	private static String bus_home;									// 根目录
	private static RUN_MODE run_mode;							// 运行模式
	private static String etc_dir;										// 配置目录
	private static String system_dir;								// osgi系统包目录
	private static String system_ext_dir;							// osgi系统扩展包目录
	private static String lib_dir;										// 系统jar目录
	private static String lib_ext_dir;									// 系统扩展jar目录
	private static String cache_dir;									// 运行时缓存目录
	private static String plugin_dir;									// osgi plugin目录
	private static String etc_bus_cfg;								// bus.cfg路径
	
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
					} else if ("-m".equalsIgnoreCase(key) || 
							"-mode".equalsIgnoreCase(key)) {	// etc目录配置
						run_mode = RUN_MODE.toMode(value);
						if (run_mode == null) {
							System.err.print(FATAL);
							System.exit(-1);		
						}
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
	 * @throws CIBusException 
	 */
	public void start() throws CIBusException {
		init();			// 初始化
		createClassLoader();
		if (null == classloader) {
			throw new CIBusException("");
		}
		loadFrameworkFactory();
		if (null == factory) {
			throw new CIBusException("");
		}
		shutdownHook();			// 关闭钩子
		run();							// 运行osgi
	}
	
	/*
	 *  各种初始化汇总
	 */
	private void init() throws CIBusException {
		initPath();				// 目录
		initEtc();					// 初始化etc配置 
		initParameters();		// 初始化osgi
		// 初始化运行模式
		switch (run_mode) {
			case APP:
				initAppMode();
				break;
			case DEV:
				initDevMode();
				break;
		}
	}
	
	/*
	 * 初始化目录
	 */
	private void initPath() {
		System.setProperty(BusConstants.BUS_HOME , bus_home);
		etc_dir = bus_home + File.separator + "etc";
		System.setProperty(BusConstants.ETC_DIR, etc_dir);
		system_dir = bus_home +File.separator + "system";
		System.setProperty(BusConstants.SYSTEM_DIR, system_dir);
		system_ext_dir = system_dir +File.separator + "ext";
		System.setProperty(BusConstants.SYSTEM_EXT_DIR, system_ext_dir);
		plugin_dir = bus_home +File.separator + "plugin";
		System.setProperty(BusConstants.PLUGIN_DIR, plugin_dir);
		lib_dir = bus_home +File.separator + "lib";
		System.setProperty(BusConstants.LIB_DIR, lib_dir);
		lib_ext_dir = lib_dir + File.separator + "ext";
		System.setProperty(BusConstants.LOG_CNF, etc_dir + File.separator + "log.cfg"); 
		etc_bus_cfg = etc_dir + File.separator + "bus.cfg";
		cache_dir = bus_home + File.separator + ".cache";
		System.setProperty(BusConstants.CACHE_DIR, cache_dir);
	}
	
	/*
	 * 初始化开发模式
	 */
	private void initDevMode() {
		FileUtil.delFolder(cache_dir);
	}
	
	/*
	 * 初始化应用模式
	 */
	private void initAppMode() {
		
	}
	
	/*
	 * 清除开发模式
	 */
	private void destroyDevMode() {
		FileUtil.delFolder(cache_dir);
	}
	
	/*
	 * 清除应用模式
	 */
	private void destroyAppMode() {
		
	}
	
	/*
	 * 初始化etc下的配置
	 */
	private void initEtc() throws CIBusException {
		CfgReader reader = CfgReader.getCfg();
		reader.loadCnf(etc_bus_cfg);
	}
	
	/*
	 * 建立系统loader，做为其它osgi包的共用loader，加载进的jar对其它bundle可见
	 */
	private void createClassLoader() {
		clsUrlList = new ArrayList<URL>();
		clsUrlList.addAll(FileUtil.getAllJar(lib_dir));
		clsUrlList.addAll(FileUtil.getAllJar(lib_ext_dir));
		classloader = new URLClassLoader(clsUrlList.toArray(new URL[clsUrlList.size()]), CIBus.class.getClassLoader());
	}
	
	/*
	 * 加载osgi framework factory
	 */
	private void loadFrameworkFactory() throws CIBusException {
		if (null != classloader) {
			try {
				URL url = classloader.getResource( "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
				if (null != url) {
					 BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
					 String line = null;
					 while(null != (line=br.readLine())) {
						 line = line.trim();
						 if (!"".equals(line) && '#' != (line.charAt(0))) {
							 factory = (FrameworkFactory) Class.forName(line).newInstance();
							 break;
						 }
					 }
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new CIBusException("");
			}
		}
	}
	
	/*
	 * 关闭钩子
	 * jvm退出时，清除
	 */
	private void shutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread("Shutdwon Hook") {
            public void run() {
                try {
                    if (framework != null) {
	                    	framework.stop();
	                    	framework.waitForStop(0);
                    }
                    // 清除模式，运行模式遗留的资源
            		switch (run_mode) {
            			case APP:
            				destroyAppMode();
            				break;
            			case DEV:
            				destroyDevMode();
            				break;
            		}
                } catch (Exception e)  {
                    System.err.println("Error stopping framework: " + e);
                }
            }
        });
	}
	
	/*
	 * 初始化osgi felix
	 * 初始化felix的参数
	 */
	private void initParameters() {
		parameters = new HashMap<String, String>();
        parameters.put("felix.cache.profiledir", cache_dir);
        parameters.put("felix.cache.dir", cache_dir);
        parameters.put("org.osgi.framework.storage", cache_dir);
//        addSystemPackages();
	}
	
	/*
	 * 得到所有系统加载包，对所有其它bundle可调用的lib jar
	 */
	private void addSystemPackages() {
		if (parameters != null) {
			List<URL> ext_url_list = FileUtil.getAllJar(lib_ext_dir);
	     	StringBuffer spBuf = new StringBuffer();
	     	for (URL ext_url : ext_url_list) {
	      	JarInputStream jis = null;
	      	try {
	      		jis = new JarInputStream(ext_url.openStream());
	      		Manifest mf = jis.getManifest();
	      		String v = mf.getMainAttributes().getValue("Export-Package");
	      		spBuf.append(v).append(",");
	      	} catch (Exception e) {
	      		e.printStackTrace();
	      	} finally {
	      		if (jis != null) {
	      			try {
						jis.close();
					} catch (IOException e) { }
	      		}
	      	}
	      }
	      String packages = spBuf.toString();
	      if (packages.length() > 0) 
	      	parameters.put("org.osgi.framework.system.packages", packages.substring(0, packages.length()-1));
		}
	}
	
	/*
	 * 运行osgi felix
	 */
	private void run() throws CIBusException {
		if (null != factory) {
			framework =  factory.newFramework(parameters);
			try {
				framework.init();
				FrameworkEvent event;
				runSystem();			// 启动system bundle
				framework.start();
				runSystemExt();		// 启动system扩展bundle
//				do {
//					event = framework.waitForStop(0);
//				} while (event.getType() == FrameworkEvent.STOPPED_UPDATE); 
//				System.exit(0);
			} catch (Exception e) {
				throw new CIBusException("", e);
			}
		}
	}
	
	/*
	 * 运行系统osgi包
	 */
	private void runSystem() {
		runBundle(system_dir, 1);
	}
	
	/*
	 * 运行系统扩展bundle包
	 */
	private void runSystemExt() {
		runBundle(system_ext_dir, 2);			// 不带lib库
		runBundle(system_ext_dir, 3);			// 带lib库支持
	}
	
	/*
	 * 运行osgi bundle
	 * 分为系统包和系统扩展包
	 * 系统扩展包可指定classloader
	 */
	private void runBundle(String dir, int type) {
		if (null != framework) {
			BundleContext context = framework.getBundleContext();
			StartLevel startLevel = (StartLevel) context.getService(
	                context.getServiceReference(org.osgi.service.startlevel.StartLevel.class.getName()));
			int level = startLevel.getInitialBundleStartLevel();
			List<BundleLoader> loaderList = new ArrayList<BundleLoader>();
			// 读取系统扩展bundle包
			File[] files = new File(dir).listFiles();
			List<URL> urlList;
			if (files != null) {
				switch (type) {
					case 1:					// 系统包
						List<File> systemJarList = new ArrayList<File>();
						for (File systemFile : files) {
							if (systemFile.getName().endsWith(".jar")) {
								BundleLoader loader = new BundleLoader(context, systemFile, startLevel, level, JarLoadMethod.START);
								loaderList.add(loader);
							}
						}
						break;
					case 2:					// 系统扩展包下无依赖库bundle
						for (File systemExtFile : files) {
							if (systemExtFile.getName().endsWith(".jar")) {
								try {
									JarBusProperty busProperty = JarBusProperty.readJarBus(systemExtFile);
									urlList = FileUtil.getAllJar(lib_ext_dir);
									urlList.addAll(busProperty.getEnvUrl());
									BundleLoader loader = new BundleLoader(
											context, 
											systemExtFile, 
											startLevel, 
											busProperty.getStartLevel(), 
											busProperty.getLoad(),
											urlList
									);
									loaderList.add(loader);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						break;
					case 3:				// 系统扩展包下有依赖库，在系统扩展下以目录存在
						for (File systemExtDir : files) {
							urlList = FileUtil.getAllJar(lib_ext_dir);
							try {
								JarBusProperty busProperty = null;
								File extBundleFile = null;
								if (systemExtDir.isDirectory()) {
									for (File extBundle : systemExtDir.listFiles()) {
										if (extBundle.isFile() && extBundle.getName().endsWith(".jar")) {
											extBundleFile = extBundle;
											busProperty = JarBusProperty.readJarBus(extBundle);
											continue;
										}
										if (extBundle.isDirectory() && "lib".equalsIgnoreCase(extBundle.getName())) {
											urlList.addAll(FileUtil.getAllJar(extBundle.getPath()));
											continue;
										}
									}
									if (extBundleFile != null && busProperty != null) {
										urlList.addAll(busProperty.getEnvUrl());
										BundleLoader loader = new BundleLoader(
												context, 
												extBundleFile, 
												startLevel, 
												busProperty.getStartLevel(), 
												busProperty.getLoad(),
												urlList
										);
										loaderList.add(loader);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
				}
			}
			// 加载bundle包
			for (BundleLoader loader : loaderList) {
				new BundleExecutor(loader).execute();
			}
		}
	}
}

