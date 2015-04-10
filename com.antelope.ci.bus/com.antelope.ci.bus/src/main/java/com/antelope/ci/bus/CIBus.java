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
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.framework.FrameworkFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.service.startlevel.StartLevel;

import com.antelope.ci.bus.common.BusConstants;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.JarBusProperty;
import com.antelope.ci.bus.common.JarLoadMethod;
import com.antelope.ci.bus.common.RUN_MODE;
import com.antelope.ci.bus.common.ResourceUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * CI BUS的程序入口
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-7-31 上午10:12:15
 */
public class CIBus {
	/**
	 * 入口
	 * 
	 * @param @param args
	 * @return void
	 * @throws CIBusException
	 */
	public static void main(String[] args) throws CIBusException {
		CIBus bus = new CIBus();
		bus.opts(args); // 参数处理
		bus.start(); // 启动
	}

	/* 帮助信息 */
	private static final String HELP = "Usage: [OPTION] [VALUE]\n"
			+ "\t-h, --home\thome directory path\n"
			+ "\t-m, --mode\trun mode(dev | app)\n";
	/* 非法选项信息 */
	private static final String FATAL = "invalid options\n"
			+ "\tTry '--help' for more information\n";
	/* 根目录不存在 */
	private static final String HOME_ERROR = "home directory is not exist or not a direcotry";

	private static ClassLoader classloader; // 系统classLoader
	private static FrameworkFactory factory; // osgi factory
	private static Framework framework; // osgi framework
	private static Map<String, String> parameters; // 参数， 主要是针对osgi
	private static List<URL> clsUrlList; // classloader url

	/* 根目录 */
	private static String bus_home;
	/* 运行模式 */
	private static RUN_MODE run_mode;
	/* 配置目录 */
	private static String etc_dir;
	/* 日志目录 */
	private static String log_dir;
	/* osgi系统包目录 */
	private static String system_dir;
	/* osgi system library */
	private static String system_lib_dir;
	/* osgi engine扩展包目录 */
	private static String system_engine_dir;
	private static String system_engine_model_dir;
	private static String system_engine_access_dir;
	private static String system_engine_manager_dir;
	private static String system_engine_lib_dir;
	/* osgi系统扩展包目录 */
	private static String system_ext_dir;
	private static String system_ext_server_dir;
	private static String system_ext_service_dir;
	private static String system_ext_portal_dir;
	private static String system_ext_gate_dir;
	private static String system_ext_lib_dir;
	/* 系统jar目录 */
	private static String lib_dir;
	/* 运行时缓存目录 */
	private static String cache_dir;
	/* osgi plugin目录 */
	private static String plugin_dir;
	/* bus.cfg路径 */
	private static String etc_bus_cfg;
	/* custom.cfg路径 */
	private static String etc_custom_cfg;
	/* environment.cfg路径 */
	private static String etc_environment_cfg;
	/* system.cfg路径 */
	private static String etc_system_cfg;
	
	/* ect下配置文件参数集合 */
	private static BasicConfigrationReader configration;
	private static Map<String, List<URL>> sysLibMap;
	private static Map<String, List<URL>> sysLibEngineMap;
	private static Map<String, List<URL>> sysLibExtMap;

	// 参数属性名
	private static String BOOT_ENVIRONMENT = "bus.boot.environment";
	private static String INIT_STARTLEVEL = "bus.bundle.init.startlevel";
	private static String STARTLEVEL = "bus.startlevel";
	private static String BOOT_ENVIRONMENT_DEFAULT = "jre-1.6";
	
	private int bundle_init_startlevel = 50;
	private int startlevel = 100;

	/**
	 * 输入参数处理
	 * 
	 * @param @param args
	 * @return void
	 * @throws
	 */
	public void opts(String[] args) {
		int argLen = args.length;
		if (argLen > 0) {
			if ("--help".equalsIgnoreCase(args[0])) { // 帮助
				System.out.print(HELP);
				System.exit(0);
			}
			// 选项处理
			if (argLen % 2 == 0) {
				int n = 0;
				while (argLen > n) {
					String key = args[n];
					String value = args[n + 1];
					if ("-h".equalsIgnoreCase(key)
							|| "--home".equalsIgnoreCase(key)) { // home目录配置
						bus_home = value;
					} else if ("-m".equalsIgnoreCase(key)
							|| "--mode".equalsIgnoreCase(key)) { // 运行模式
						run_mode = RUN_MODE.toMode(value);
						if (run_mode == null) {
							System.err.print(FATAL);
							System.exit(-1);
						}
						System.setProperty(BusConstants.BUS_RUN_MODE, run_mode.getName());
					} else { // 非法选项
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
	 * 
	 * @param
	 * @return void
	 * @throws CIBusException
	 */
	public void start() throws CIBusException {
		init(); // 初始化
		createClassLoader();
		if (null == classloader) {
			throw new CIBusException("");
		}
		loadFrameworkFactory();
		if (null == factory) {
			throw new CIBusException("");
		}
		shutdownHook(); // 关闭钩子
		run(); // 运行osgi
	}

	/*
	 * 各种初始化汇总
	 */
	private void init() throws CIBusException {
		initPath(); // 目录
		initEtc(); // 初始化etc配置
		initParameters(); // 初始化osgi
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
	 * initialize directories
	 * com.antelope.ci.bus
	 * *  * *  * *  * *  * *  * *  * *  * *  * *  * *  * *  * *  * *  *  * *  * *  * *
			├── bin
			│   ├── start.bat
			│   ├── start_debug.bat
			│   ├── start_debug.sh
			│   └── start.sh
			├── etc
			│   ├── bus.cfg
			│   ├── custom.cfg
			│   ├── environment.cfg
			│   ├── log.cfg
			│   └── system.cfg
			├── lib
			├── log
			│   ├── bus_debug.log
			│   ├── bus_error.log
			│   └── bus.log
			├── plugin
			└── system
				├── engine
			    │   ├── com.antelope.ci.bus.engine.model
			    │   │   ├── lib
			    │   │   ├── part
			    │   │   └── service
			    │   ├── com.antelope.ci.bus.engine.access
			    │   │   ├── lib
			    │	 │   ├── part
			    │   │   └── service
			    │   ├── com.antelope.ci.bus.engine.manager
			    │   │   ├── lib
			    │   │   ├── part
			    │   │   └── service
			    │   ├── lib
			    ├── ext
			    │   ├── com.antelope.ci.bus.portal
			    │   │   ├── lib
			    │   │   ├── part
			    │   │   └── service
			    │   ├── com.antelope.ci.bus.gate
			    │   │   ├── lib
			    │   │   └── service
			    │   ├── com.antelope.ci.bus.server
			    │   │   ├── lib
			    │   │   └── service
			    │   ├── com.antelope.ci.bus.service
			    │   │   ├── lib
			    │   │   └── service
			    │   ├── lib
			    └── lib
	* *  * *  * *  * *  * *  * *  * *  * *  * *  * *  * *  * *  * *  *  * *  * *  * *
	 */
	private void initPath() {
		System.setProperty(BusConstants.BUS_HOME, bus_home);
		/* etc and configuration files */
		etc_dir = bus_home + File.separator + "etc";
		System.setProperty(BusConstants.ETC_DIR, etc_dir);
		System.setProperty(BusConstants.LOG_CNF, etc_dir + File.separator + "log.cfg");
		etc_bus_cfg = etc_dir + File.separator + "bus.cfg";
		etc_custom_cfg = etc_dir + File.separator + "custom.cfg";
		etc_environment_cfg = etc_dir + File.separator + "environment.cfg";
		etc_system_cfg = etc_dir + File.separator + "system.cfg";
		
		/* log */
		log_dir = bus_home + File.separator + "log";
		System.setProperty(BusConstants.LOG_DIR, log_dir);
		
		/* cache */
		cache_dir = bus_home + File.separator + ".cache";
		System.setProperty(BusConstants.CACHE_DIR, cache_dir);
		
		/* plugin */
		plugin_dir = bus_home + File.separator + "plugin";
		System.setProperty(BusConstants.PLUGIN_DIR, plugin_dir);
		/* lib */
		lib_dir = bus_home + File.separator + "lib";
		System.setProperty(BusConstants.LIB_DIR, lib_dir);
		
		/* system */
		system_dir = bus_home + File.separator + "system";
		System.setProperty(BusConstants.SYSTEM_DIR, system_dir);
		/* system/engine */
		system_engine_dir = system_dir + File.separator + "engine";
		System.setProperty(BusConstants.SYSTEM_ENGINE_DIR, system_engine_dir);
		// system/engine/com.antelope.ci.bus.engine.model
		system_engine_model_dir = system_engine_dir + File.separator + BusConstants.SYSTEM_ENGINE_MODEL_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_ENGINE_MODEL_DIR, system_engine_model_dir);
		// system/engine/com.antelope.ci.bus.engine.access
		system_engine_access_dir = system_engine_dir + File.separator + BusConstants.SYSTEM_ENGINE_ACCESS_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_ENGINE_ACCESS_DIR, system_engine_access_dir);
		// system/engine/com.antelope.ci.bus.engine.manger
		system_engine_manager_dir = system_engine_dir + File.separator + BusConstants.SYSTEM_ENGINE_MANAGER_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_ENGINE_MANAGER_DIR, system_engine_manager_dir);
		// system/engine/lib
		system_engine_lib_dir = system_engine_dir + File.separator + BusConstants.SYSTEM_ENGINE_LIB_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_ENGINE_LIB_DIR, system_engine_lib_dir);
		/* system/ext */
		system_ext_dir = system_dir + File.separator + "ext";
		System.setProperty(BusConstants.SYSTEM_EXT_DIR, system_ext_dir);
		// system/ext/com.antelope.ci.bus.server
		system_ext_server_dir = system_ext_dir + File.separator + BusConstants.SYSTEM_EXT_SERVER_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_EXT_SERVER_DIR, system_ext_server_dir);
		// system/ext/com.antelope.ci.bus.service
		system_ext_service_dir = system_ext_dir + File.separator + BusConstants.SYSTEM_EXT_SERVICE_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_EXT_SERVICE_DIR, system_ext_service_dir);
		// system/ext/com.antelope.ci.bus.portal
		system_ext_portal_dir = system_ext_dir + File.separator + BusConstants.SYSTEM_EXT_PORTAL_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_EXT_PORTAL_DIR, system_ext_portal_dir);
		// system/ext/com.antelope.ci.bus.gate
		system_ext_gate_dir = system_ext_dir + File.separator + BusConstants.SYSTEM_EXT_GATE_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_EXT_GATE_DIR, system_ext_gate_dir);
		// system/ext/lib
		system_ext_lib_dir = system_ext_dir + File.separator + BusConstants.SYSTEM_EXT_LIB_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_EXT_LIB_DIR, system_ext_lib_dir);
		/* system/lib */
		system_lib_dir = system_dir + File.separator + "lib";
		System.setProperty(BusConstants.SYSTEM_LIB_DIR, system_lib_dir);
	}

	/*
	 * 初始化开发模式
	 */
	private void initDevMode() {
		// 配置storage目录为onFirstInit，当程序一运行即清空cache storage
		parameters.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
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
		reader.loadCnf(etc_custom_cfg);
		reader.loadCnf(etc_environment_cfg);
		reader.loadCnf(etc_system_cfg);
		configration = reader.getConfigration();
	}

	/*
	 * 建立系统loader，做为其它osgi包的共用loader，加载进的jar对其它bundle可见
	 */
	private void createClassLoader() {
		clsUrlList = new ArrayList<URL>();
		clsUrlList.addAll(FileUtil.getAllJar(lib_dir));
		classloader = new URLClassLoader(clsUrlList.toArray(new URL[clsUrlList.size()]), CIBus.class.getClassLoader());
	}

	/*
	 * 加载osgi framework factory
	 */
	private void loadFrameworkFactory() throws CIBusException {
		if (null != classloader) {
			try {
				URL url = classloader.getResource("META-INF/services/org.osgi.framework.launch.FrameworkFactory");
				if (null != url) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(url.openStream()));
					String line = null;
					while (null != (line = br.readLine())) {
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
	 * 关闭钩子 jvm退出时，清除
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
				} catch (Exception e) {
					System.err.println("Error stopping framework: " + e);
				}
			}
		});
	}

	/*
	 * 初始化osgi felix 初始化felix的参数
	 */
	private void initParameters() {
		parameters = new HashMap<String, String>();
		parameters.put("felix.cache.profiledir", cache_dir);
		parameters.put("felix.cache.dir", cache_dir);
		parameters.put(Constants.FRAMEWORK_STORAGE, cache_dir);
		// boot delegation of osgi
		String bootdelegation = "";
		String boot_envs = configration.getString(BOOT_ENVIRONMENT, BOOT_ENVIRONMENT_DEFAULT);
		bundle_init_startlevel = configration.getInt(INIT_STARTLEVEL, bundle_init_startlevel);
		startlevel = configration.getInt(STARTLEVEL, startlevel);
		if (boot_envs.equals(BOOT_ENVIRONMENT_DEFAULT)) {
			bootdelegation = configration.getString(BOOT_ENVIRONMENT_DEFAULT, "");
		} else {
			for (String boot_env : boot_envs.split(",")) {
				if (configration.getString(boot_env.trim()) != null)
					bootdelegation += configration.getString(boot_env.trim()) + ", ";
			}
			if (bootdelegation.endsWith(", "))
				bootdelegation = bootdelegation.substring(0, bootdelegation.length() - 2);
		}
		if (bootdelegation.length() > 0) {
			parameters.put(Constants.FRAMEWORK_BOOTDELEGATION, bootdelegation);
			parameters.put(Constants.FRAMEWORK_BUNDLE_PARENT, Constants.FRAMEWORK_BUNDLE_PARENT_APP);
		}
		parameters.put(Constants.FRAMEWORK_BEGINNING_STARTLEVEL, String.valueOf(startlevel));
	}

	/*
	 * 运行osgi felix
	 */
	private void run() throws CIBusException {
		if (null != factory) {
			framework = factory.newFramework(parameters);
			try {
				framework.init();
				parseSyslibs();
				runSystem(); // 启动system bundle
				framework.start();
				FrameworkStartLevel sl = framework.adapt(FrameworkStartLevel.class);
		        sl.setInitialBundleStartLevel(bundle_init_startlevel);
		        runSystemEngine(); // 启动system引擎bundle
				runSystemExt(); // 启动system扩展bundle
				runPlugin(); // 启动plugin
			} catch (Exception e) {
				throw new CIBusException("", e);
			}
		}
	}

	/*
	 * 运行系统osgi包
	 */
	private void runSystem() {
		List<BundleLoader> loaderList = loadSystemBundle(system_dir, 1);
		// 加载bundle包
		for (BundleLoader loader : loaderList) {
			new BundleExecutor(loader).execute();
		}
	}
	
	private void runSystemEngine() {
		List<BundleLoader> loaderList = new ArrayList<BundleLoader>();
		loaderList.addAll(loadSystemBundle(system_engine_dir, 7)); // 不带lib库
		loaderList.addAll(loadSystemBundle(system_engine_dir, 8)); // 带lib库支持
		loaderList.addAll(loadSystemBundle(system_engine_dir, 9)); // load to container except lib
		Collections.sort(loaderList, new BundleLoaderComparator());
		// 加载bundle包
		for (BundleLoader loader : loaderList) {
			new BundleExecutor(loader).execute();
		}
	}

	/*
	 * 运行系统扩展bundle包
	 */
	private void runSystemExt() {
		List<BundleLoader> loaderList = new ArrayList<BundleLoader>();
		loaderList.addAll(loadSystemBundle(system_ext_dir, 2)); // 不带lib库
		loaderList.addAll(loadSystemBundle(system_ext_dir, 3)); // 带lib库支持
		loaderList.addAll(loadSystemBundle(system_ext_dir, 4)); // com.antelope.ci.bus.server支持
		loaderList.addAll(loadSystemBundle(system_ext_dir, 5)); // com.antelope.ci.bus.service支持
		loaderList.addAll(loadSystemBundle(system_ext_dir, 6)); // com.antelope.ci.bus.portal支持
		loaderList.addAll(loadSystemBundle(system_ext_dir, 10)); // com.antelope.ci.bus.gate支持
		Collections.sort(loaderList, new BundleLoaderComparator());
		// 加载bundle包
		for (BundleLoader loader : loaderList) {
			new BundleExecutor(loader).execute();
		}
	}
	
	/*
	 * plugin包, 支持plugin规范及CIBus自定义格式
	 */
	private void runPlugin() {
		// TODO: 
	}

	/*
	 * 运行osgi bundle 分为系统包和系统扩展包 系统扩展包可指定classloader
	 */
	private List<BundleLoader> loadSystemBundle(String dir, int type) {
		List<BundleLoader> loaderList = new ArrayList<BundleLoader>();
		if (null != framework) {
			BundleContext context = framework.getBundleContext();
			StartLevel startLevel = (StartLevel) context
					.getService(context
							.getServiceReference(org.osgi.service.startlevel.StartLevel.class
									.getName()));
			int level = startLevel.getInitialBundleStartLevel();
			File root = new File(dir);
			// 读取系统扩展bundle包
			File[] files = root.listFiles();
			List<URL> lib_urlList;
			if (files != null) {
				switch (type) {
					case 1: // 系统包
						List<File> systemJarList = new ArrayList<File>();
						for (File systemFile : files) {
							if (systemFile.getName().endsWith(".jar")) {
								BundleLoader loader = new BundleLoader(
										context, systemFile, startLevel, level, level, JarLoadMethod.START);
								loaderList.add(loader);
							}
						}
						break;
						
					case 2: // 系统扩展包下无依赖库bundle
						loadSingleBundle(loaderList, files, loadSystemExtLib(), context, startLevel);
						break;
						
					case 3: // 系统扩展包下有依赖库，在系统扩展下以目录存在
						loadDependentBundle(
								loaderList, 
								files, 
								loadSystemExtLib(), 
								context, startLevel, 
								new String[] {
									BusConstants.SYSTEM_EXT_SERVER_DIRNAME,
									BusConstants.SYSTEM_EXT_SERVICE_DIRNAME,
									BusConstants.SYSTEM_EXT_PORTAL_DIRNAME,
									BusConstants.SYSTEM_EXT_LIB_DIRNAME}
						);
						break;
						
					case 4: // com.antelope.ci.bus.server
						loadContaintFragments(context, startLevel, loaderList, loadSystemExtLib(),
								root, BusConstants.SYSTEM_EXT_SERVER_DIRNAME, "part", "service");
						break;
						
					case 5: // com.antelope.ci.bus.service
						
						break;
						
					case 6: // com.antelope.ci.bus.portal
						loadContaintFragments(context, startLevel, loaderList, loadSystemExtLib(),
								root, BusConstants.SYSTEM_EXT_PORTAL_DIRNAME, "part", "service");
						break;
						
					case 7: // 系统引擎包下无依赖库bundle
						loadSingleBundle(loaderList, files, loadSystemEngineLib(), context, startLevel);
						break;
						
					case 8: // 系统扩展包下有依赖库，在系统扩展下以目录存在
						loadDependentBundle(
								loaderList, 
								files, 
								loadSystemEngineLib(), 
								context, startLevel, 
								new String[] {
									BusConstants.SYSTEM_ENGINE_MODEL_DIRNAME,
									BusConstants.SYSTEM_ENGINE_ACCESS_DIRNAME,
									BusConstants.SYSTEM_ENGINE_MANAGER_DIRNAME,
									BusConstants.SYSTEM_ENGINE_LIB_DIRNAME}
						);
						break;
						
					case 9: // com.antelope.ci.bus.engine
						// com.antelope.ci.bus.engine.model
						loadContaintFragments(context, startLevel, loaderList, loadSystemEngineLib(),
								root, BusConstants.SYSTEM_ENGINE_MODEL_DIRNAME, "part", "service");
						// com.antelope.ci.bus.engine.access
						loadContaintFragments(context, startLevel, loaderList, loadSystemEngineLib(),
								root, BusConstants.SYSTEM_ENGINE_ACCESS_DIRNAME, "part", "service");
						// com.antelope.ci.bus.engine.manager
						loadContaintFragments(context, startLevel, loaderList, loadSystemEngineLib(),
								root, BusConstants.SYSTEM_ENGINE_MANAGER_DIRNAME, "part", "service");
						break;
						
					case 10: // com.antelope.ci.bus.gate
						loadContaintFragments(context, startLevel, loaderList, loadSystemExtLib(),
								root, BusConstants.SYSTEM_EXT_GATE_DIRNAME, "part", "service");
						break;
				}
			}
		}
		
		return loaderList;
	}
	
	private List<URL> loadSystemEngineLib() {
		List<URL> libUrlList = new ArrayList<URL>();
		libUrlList.addAll(FileUtil.getAllJar(system_lib_dir));
		libUrlList.addAll(FileUtil.getAllJar(system_engine_lib_dir));
		return libUrlList;
	}
	
	private List<URL> loadSystemExtLib() {
		List<URL> libUrlList = new ArrayList<URL>();
		libUrlList.addAll(FileUtil.getAllJar(system_lib_dir));
		libUrlList.addAll(FileUtil.getAllJar(system_engine_lib_dir));
		libUrlList.addAll(FileUtil.getAllJar(system_ext_lib_dir));
		return libUrlList;
	}
	
	private void loadSingleBundle(List<BundleLoader> loaderList, File[] files, List<URL> libUrlList, BundleContext context, StartLevel startLevel) {
		for (File file : files) {
			if (file.getName().endsWith(".jar")) {
				try {
					JarBusProperty busProperty = JarBusProperty.readJarBus(file);
					libUrlList.addAll(busProperty.getLoaderUrlList());
					attatchSysLibUrls(file.getName(), libUrlList);
					BundleLoader loader = new BundleLoader(context,
							file, startLevel,
							busProperty.getBundleLevel(),
							busProperty.getLoadLevel(),
							busProperty.getLoad(), libUrlList);
					loaderList.add(loader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadDependentBundle(List<BundleLoader> loaderList, File[] files, List<URL> libUrlList, BundleContext context, StartLevel startLevel, String[] ignoreFilenames) {
		for (File file : files) {
			try {
				JarBusProperty busProperty = null;
				File bundleFile = null;
				if (file.isDirectory() && !matchFile(file, ignoreFilenames)) {
					for (File bundle : file.listFiles()) {
						if (bundle.isFile() && bundle.getName().endsWith(".jar")) {
							bundleFile = bundle;
							busProperty = JarBusProperty.readJarBus(bundle);
							continue;
						}
						if (bundle.isDirectory() && "lib".equalsIgnoreCase(bundle.getName())) {
							libUrlList.addAll(FileUtil.getAllJar(bundle.getPath()));
							continue;
						}
					}
					if (bundleFile != null && busProperty != null) {
						libUrlList.addAll(busProperty.getLoaderUrlList());
						attatchSysLibUrls(file.getName(), libUrlList);
						BundleLoader loader = new BundleLoader(
								context, bundleFile, startLevel,
								busProperty.getBundleLevel(),
								busProperty.getLoadLevel(),
								busProperty.getLoad(), libUrlList);
						loaderList.add(loader);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean matchFile(File file, String[] filenames) {
		for (String filename : filenames) {
			if (filename.equalsIgnoreCase(file.getName()))
				return true;
		}
		
		return false;
	}
	
	private void loadContaintFragments(BundleContext context, StartLevel startLevel, 
			List<BundleLoader> loaderList, List<URL> libUrlList, File root, 
			String dirName, String fragmentDir, String serviceDir) {
		File[] bundle_files = FileUtil.getChildFiles(root, dirName);
		if (bundle_files != null && bundle_files.length == 1) {
			try {
				File bundle_dir = bundle_files[0];
				File[] bundle_jar_files = FileUtil.getChildFiles(bundle_dir, ".jar");
//				if (bundle_jar_files != null && bundle_jar_files.length == 1) {
//					File bundle_jar_file = bundle_jar_files[0];
				for (File bundle_jar_file : bundle_jar_files) {
					File[] bundle_lib_files = FileUtil.getChildFiles(bundle_dir, "lib");
					if (bundle_lib_files != null && bundle_lib_files.length == 1) {
						libUrlList.addAll(FileUtil.getAllJar(bundle_lib_files[0].getPath()));
					}
					attatchSysLibUrls(bundle_dir.getName(), libUrlList);
					
					File[] fragment_files = FileUtil.getChildFiles(bundle_dir, fragmentDir);
					if (fragment_files != null && fragment_files.length == 1) {
						for (File fragment_file : fragment_files[0].listFiles()) {
							 if (fragment_file.isDirectory()) {
								 for (File fragment : fragment_file.listFiles()) {
									 if (fragment.isDirectory() && "lib".equalsIgnoreCase(fragment.getName())) {
										 libUrlList.addAll(FileUtil.getAllJar(fragment.getPath()));
									 } else if (fragment.getName().endsWith(".jar")) {
										 	JarBusProperty busProperty = JarBusProperty.readJarBus(fragment);
											BundleLoader loader = new BundleLoader(
													context, fragment, startLevel,
													busProperty.getBundleLevel(),
													busProperty.getLoadLevel(),
													busProperty.getLoad(), new ArrayList<URL>());
											loaderList.add(loader);
									}
								 }
							} else if (fragment_file.getName().endsWith(".jar")) {
								JarBusProperty busProperty = JarBusProperty.readJarBus(fragment_file);
								BundleLoader loader = new BundleLoader(
										context, fragment_file, startLevel,
										busProperty.getBundleLevel(),
										busProperty.getLoadLevel(),
										busProperty.getLoad(), new ArrayList<URL>());
								loaderList.add(loader);
							}
						}
					}
					
					JarBusProperty busProperty = JarBusProperty.readJarBus(bundle_jar_file);
					BundleLoader loader = new BundleLoader(
							context, bundle_jar_file, startLevel,
							busProperty.getBundleLevel(),
							busProperty.getLoadLevel(),
							busProperty.getLoad(), libUrlList);
					loaderList.add(loader);
					
					// add service to loader
					if (!StringUtil.empty(serviceDir)) {
						File[] service_files = FileUtil.getChildFiles(bundle_dir, serviceDir);
						if (service_files != null && service_files.length == 1) {
							for (File service_file : service_files[0].listFiles()) {
								if (service_file.getName().endsWith(".jar")) {
									busProperty = JarBusProperty.readJarBus(service_file);
									BundleLoader service_loader = new BundleLoader(
											context, service_file, startLevel,
											busProperty.getBundleLevel(),
											busProperty.getLoadLevel(),
											busProperty.getLoad(), libUrlList);
									loaderList.add(service_loader);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void attatchSysLibUrls(String filename, List<URL> urlList) {
		for (String k : sysLibMap.keySet()) {
			if (filename.startsWith(k)) {
				List<URL> sysLibUrls = sysLibMap.get(k);
				for (URL sysLibUrl : sysLibUrls) {
					boolean added = false;
					for (URL url : urlList) {
						if (sysLibUrl.equals(url)) {
							added = true;
							break;
						}
					}
					if (!added)
						urlList.add(sysLibUrl);
				}
				break;
			}
		}
	}
	
	private void parseSyslibs() {
		String env_suffix = ".env";
		sysLibMap = new HashMap<String, List<URL>>();
		List<URL> uList = FileUtil.getAllJar(system_lib_dir);
		for (Object k : configration.getProps().keySet()) {
			String key = (String) k;
			if (key.endsWith(env_suffix)) {
				String v = configration.getString(key);
				if (v != null) {
					List<URL> urlList = new ArrayList<URL>();
					String[] vs = v.split(",");
					for (String value : vs) {
						for (URL u : uList) {
							if (u.getFile().endsWith(value.trim())) {
								urlList.add(u);
								break;
							}
						}
					}
					if (!urlList.isEmpty()) {
						sysLibMap.put(key.substring(0, key.lastIndexOf(env_suffix)), urlList);
					}
				}
			}
		}
	}
	
	private static class BundleLoaderComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			BundleLoader loader1 = (BundleLoader) o1;
			BundleLoader loader2 = (BundleLoader) o2;
			int ret = loader1.bundleLevel - loader2.bundleLevel;
			if (ret == 0)
				ret = loader1.loadLevel - loader2.loadLevel;
			return ret;
		}
	}
}