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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

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
	private static String system_engine_service_dir;
	/* osgi系统扩展包目录 */
	private static String system_ext_dir;
	private static String system_ext_server_dir;
	private static String system_ext_service_dir;
	private static String system_ext_protal_dir;
	/* 系统jar目录 */
	private static String lib_dir;
	/* 系统扩展jar目录 */
	private static String lib_ext_dir;
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
	 * 初始化目录
	 */
	private void initPath() {
		System.setProperty(BusConstants.BUS_HOME, bus_home);
		etc_dir = bus_home + File.separator + "etc";
		System.setProperty(BusConstants.ETC_DIR, etc_dir);
		log_dir = bus_home + File.separator + "log";
		System.setProperty(BusConstants.LOG_DIR, log_dir);
		system_dir = bus_home + File.separator + "system";
		System.setProperty(BusConstants.SYSTEM_DIR, system_dir);
		system_lib_dir = system_dir + File.separator + "lib";
		System.setProperty(BusConstants.SYSTEM_LIB_DIR, system_lib_dir);
		system_ext_dir = system_dir + File.separator + "ext";
		System.setProperty(BusConstants.SYSTEM_EXT_DIR, system_ext_dir);
		system_ext_server_dir = system_ext_dir + File.separator + BusConstants.SYSTEM_EXT_SERVER_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_EXT_SERVER_DIR, system_ext_server_dir);
		system_ext_service_dir = system_ext_dir + File.separator + BusConstants.SYSTEM_EXT_SERVICE_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_EXT_SERVICE_DIR, system_ext_service_dir);
		system_ext_protal_dir = system_ext_dir + File.separator + BusConstants.SYSTEM_EXT_PORTAL_DIRNAME;
		System.setProperty(BusConstants.SYSTEM_EXT_PORTAL_DIR, system_ext_protal_dir);
		plugin_dir = bus_home + File.separator + "plugin";
		System.setProperty(BusConstants.PLUGIN_DIR, plugin_dir);
		lib_dir = bus_home + File.separator + "lib";
		System.setProperty(BusConstants.LIB_DIR, lib_dir);
		lib_ext_dir = lib_dir + File.separator + "ext";
		System.setProperty(BusConstants.LOG_CNF, etc_dir + File.separator + "log.cfg");
		etc_bus_cfg = etc_dir + File.separator + "bus.cfg";
		etc_custom_cfg = etc_dir + File.separator + "custom.cfg";
		etc_environment_cfg = etc_dir + File.separator + "environment.cfg";
		etc_system_cfg = etc_dir + File.separator + "system.cfg";
		cache_dir = bus_home + File.separator + ".cache";
		System.setProperty(BusConstants.CACHE_DIR, cache_dir);
	}

	/*
	 * 初始化开发模式
	 */
	private void initDevMode() {
		// 配置storage目录为onFirstInit，当程序一运行即清空cache storage
		parameters.put(Constants.FRAMEWORK_STORAGE_CLEAN,
				Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
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
		clsUrlList.addAll(FileUtil.getAllJar(lib_ext_dir));
		classloader = new URLClassLoader(clsUrlList.toArray(new URL[clsUrlList
				.size()]), CIBus.class.getClassLoader());
	}

	/*
	 * 加载osgi framework factory
	 */
	private void loadFrameworkFactory() throws CIBusException {
		if (null != classloader) {
			try {
				URL url = classloader
						.getResource("META-INF/services/org.osgi.framework.launch.FrameworkFactory");
				if (null != url) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(url.openStream()));
					String line = null;
					while (null != (line = br.readLine())) {
						line = line.trim();
						if (!"".equals(line) && '#' != (line.charAt(0))) {
							factory = (FrameworkFactory) Class.forName(line)
									.newInstance();
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
		String boot_envs = configration.getString(BOOT_ENVIRONMENT,
				BOOT_ENVIRONMENT_DEFAULT);
		bundle_init_startlevel = configration.getInt(INIT_STARTLEVEL, 
				bundle_init_startlevel);
		startlevel = configration.getInt(STARTLEVEL,
				startlevel);
		if (boot_envs.equals(BOOT_ENVIRONMENT_DEFAULT)) {
			bootdelegation = configration.getString(BOOT_ENVIRONMENT_DEFAULT, "");
		} else {
			for (String boot_env : boot_envs.split(",")) {
				if (configration.getString(boot_env.trim()) != null) {
					bootdelegation += configration.getString(boot_env.trim())
							+ ", ";
				}
			}
			if (bootdelegation.endsWith(", "))
				bootdelegation = bootdelegation.substring(0,
						bootdelegation.length() - 2);
		}
		if (bootdelegation.length() > 0) {
			parameters.put(Constants.FRAMEWORK_BOOTDELEGATION, bootdelegation);
			parameters.put(Constants.FRAMEWORK_BUNDLE_PARENT,
					Constants.FRAMEWORK_BUNDLE_PARENT_APP);
		}
		parameters.put(Constants.FRAMEWORK_BEGINNING_STARTLEVEL, String.valueOf(startlevel));
		// genBundleClassPath();
		// addSystemPackages();
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
					String v = mf.getMainAttributes()
							.getValue("Export-Package");
					spBuf.append(v).append(",");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (jis != null) {
						try {
							jis.close();
						} catch (IOException e) {
						}
					}
				}
			}
			String packages = spBuf.toString();
			if (packages.length() > 0)
				parameters.put("org.osgi.framework.system.packages",
						packages.substring(0, packages.length() - 1));
		}
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
				runSystemExt(); // 启动system扩展bundle
				// do {
				// event = framework.waitForStop(0);
				// } while (event.getType() == FrameworkEvent.STOPPED_UPDATE);
				// System.exit(0);
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

	/*
	 * 运行系统扩展bundle包
	 */
	private void runSystemExt() {
		List<BundleLoader> loaderList = new ArrayList<BundleLoader>();
		loaderList.addAll(
				loadSystemBundle(system_ext_dir, 2)); // 不带lib库
		loaderList.addAll(
				loadSystemBundle(system_ext_dir, 3)); // 带lib库支持
		loaderList.addAll(
				loadSystemBundle(system_ext_dir, 4)); // com.antelope.ci.bus.server支持
		loaderList.addAll(
				loadSystemBundle(system_ext_dir, 5)); // com.antelope.ci.bus.service支持
		loaderList.addAll(
				loadSystemBundle(system_ext_dir, 6)); // com.antelope.ci.bus.portal支持
		Collections.sort(loaderList, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				BundleLoader loader1 = (BundleLoader) o1;
				BundleLoader loader2 = (BundleLoader) o2;
				return loader1.level - loader2.level;
			}
		});
		// 加载bundle包
		for (BundleLoader loader : loaderList) {
			new BundleExecutor(loader).execute();
		}
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
								BundleLoader loader = new BundleLoader(context,
										systemFile, startLevel, level,
										JarLoadMethod.START);
								loaderList.add(loader);
							}
						}
						break;
						
					case 2: // 系统扩展包下无依赖库bundle
						for (File systemExtFile : files) {
							if (systemExtFile.getName().endsWith(".jar")) {
								try {
									JarBusProperty busProperty = JarBusProperty
											.readJarBus(systemExtFile);
									lib_urlList = FileUtil.getAllJar(lib_ext_dir);
									lib_urlList.addAll(busProperty.getLoaderUrlList());
									attatchSysLibUrls(systemExtFile.getName(), lib_urlList);
									BundleLoader loader = new BundleLoader(context,
											systemExtFile, startLevel,
											busProperty.getStartLevel(),
											busProperty.getLoad(), lib_urlList);
									loaderList.add(loader);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						break;
						
					case 3: // 系统扩展包下有依赖库，在系统扩展下以目录存在
						for (File systemExtDir : files) {
							lib_urlList = FileUtil.getAllJar(lib_ext_dir);
							try {
								JarBusProperty busProperty = null;
								File extBundleFile = null;
								if (systemExtDir.isDirectory() 
										&& !BusConstants.SYSTEM_EXT_SERVER_DIRNAME.equalsIgnoreCase(systemExtDir.getName())
										&& !BusConstants.SYSTEM_EXT_SERVICE_DIRNAME.equalsIgnoreCase(systemExtDir.getName())
										&& !BusConstants.SYSTEM_EXT_PORTAL_DIRNAME.equalsIgnoreCase(systemExtDir.getName())
										) {
									for (File extBundle : systemExtDir.listFiles()) {
										if (extBundle.isFile()
												&& extBundle.getName().endsWith(
														".jar")) {
											extBundleFile = extBundle;
											busProperty = JarBusProperty
													.readJarBus(extBundle);
											continue;
										}
										if (extBundle.isDirectory()
												&& "lib".equalsIgnoreCase(extBundle
														.getName())) {
											lib_urlList.addAll(FileUtil
													.getAllJar(extBundle.getPath()));
											continue;
										}
									}
									if (extBundleFile != null
											&& busProperty != null) {
										lib_urlList.addAll(busProperty
												.getLoaderUrlList());
										attatchSysLibUrls(systemExtDir.getName(), lib_urlList);
										BundleLoader loader = new BundleLoader(
												context, extBundleFile, startLevel,
												busProperty.getStartLevel(),
												busProperty.getLoad(), lib_urlList);
										loaderList.add(loader);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						break;
						
					case 4:		// com.antelope.ci.bus.server
						loadContaintFragments(context, startLevel, loaderList, 
								root, BusConstants.SYSTEM_EXT_SERVER_DIRNAME, "service", null);
						break;
						
					case 5:		// com.antelope.ci.bus.service
						
						break;
					case 6:		// com.antelope.ci.bus.portal
						loadContaintFragments(context, startLevel, loaderList, 
								root, BusConstants.SYSTEM_EXT_PORTAL_DIRNAME, "part", "service");
						break;
					}
			}
		}
		
		return loaderList;
	}
	
	private void loadContaintFragments(BundleContext context, StartLevel startLevel, 
			List<BundleLoader> loaderList, File root, String dirname, String fragmentdir, String servicedir) {
		File[] bundle_files = FileUtil.getChildFiles(root, dirname);
		if (bundle_files != null && bundle_files.length == 1) {
			try {
				File bundle_dir = bundle_files[0];
				File[] bundle_jar_files = FileUtil.getChildFiles(bundle_dir, ".jar");
				if (bundle_jar_files != null && bundle_jar_files.length == 1) {
					File bundle_jar_file = bundle_jar_files[0];
					File[] bundle_lib_files = FileUtil.getChildFiles(bundle_dir, "lib");
					List<URL> lib_urlList = FileUtil.getAllJar(lib_ext_dir);
					if (bundle_lib_files != null && bundle_lib_files.length == 1) {
						lib_urlList.addAll(FileUtil.getAllJar(bundle_lib_files[0].getPath()));
					}
					attatchSysLibUrls(bundle_dir.getName(), lib_urlList);
					
					File[] fragment_files = FileUtil.getChildFiles(bundle_dir, fragmentdir);
					if (fragment_files != null && fragment_files.length == 1) {
						for (File fragment_file : fragment_files[0].listFiles()) {
							 if (fragment_file.isDirectory()) {
								 for (File fragment : fragment_file.listFiles()) {
									 if (fragment.isDirectory() && "lib".equalsIgnoreCase(fragment.getName())) {
										 lib_urlList.addAll(FileUtil.getAllJar(fragment.getPath()));
									 } else if (fragment.getName().endsWith(".jar")) {
										 	JarBusProperty busProperty = JarBusProperty.readJarBus(fragment);
											BundleLoader loader = new BundleLoader(
													context, fragment, startLevel,
													busProperty.getStartLevel(),
													busProperty.getLoad(), new ArrayList<URL>());
											loaderList.add(loader);
									}
								 }
							} else if (fragment_file.getName().endsWith(".jar")) {
								JarBusProperty busProperty = JarBusProperty.readJarBus(fragment_file);
								BundleLoader loader = new BundleLoader(
										context, fragment_file, startLevel,
										busProperty.getStartLevel(),
										busProperty.getLoad(), new ArrayList<URL>());
								loaderList.add(loader);
							}
						}
					}
					
					JarBusProperty busProperty = JarBusProperty.readJarBus(bundle_jar_file);
					BundleLoader loader = new BundleLoader(
							context, bundle_jar_file, startLevel,
							busProperty.getStartLevel(),
							busProperty.getLoad(), lib_urlList);
					loaderList.add(loader);
					
					// add service to loader
					if (!StringUtil.empty(servicedir)) {
						File[] service_files = FileUtil.getChildFiles(bundle_dir, servicedir);
						if (service_files != null && service_files.length == 1) {
							for (File service_file : service_files[0].listFiles()) {
								if (service_file.getName().endsWith(".jar")) {
									busProperty = JarBusProperty.readJarBus(service_file);
									BundleLoader service_loader = new BundleLoader(
											context, service_file, startLevel,
											busProperty.getStartLevel(),
											busProperty.getLoad(), lib_urlList);
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
					boolean isAdd = true;
					for (URL url : urlList) {
						if (sysLibUrl.equals(url)) {
							isAdd = false;
							break;
						}
					}
					if (isAdd) {
						urlList.add(sysLibUrl);
					}
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

	/*
	 * 产生Bundle-ClassPath参数
	 */
	private void genBundleClassPath() {
		parameters
				.put(Constants.BUNDLE_CLASSPATH,
						StringUtil.convertUrlList(
								FileUtil.getAllJar(lib_ext_dir), ","));
	}
}