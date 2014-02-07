// com.antelope.ci.bus.common.ClassFinder.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * find class tool
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-11		下午3:46:36 
 */
public class ClassFinder {
	private final static String DOT = ".";
	private final static String SLASH = "/";
    private final static String CLASS_SUFFIX = ".class";
    private final static String PROPERTIES_SUFFIX = ".properties";
    private final static String XML_SUFFIX = ".xml";
    
	/**
	 * 取得package下的所有类，并以类的url列表返回
	 * @throws CIBusException 
	 * @param @param packageName
	 * @param @return
	 * @return List<URL>
	 * @throws
	 */
	public static List<URL> findClassUrl(String packageName) throws CIBusException {
		List<URL> urlList = new ArrayList<URL>();
		for (String className : findClasspath(packageName)) {
			try {
				urlList.add(classNameToUrl(className, true));
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		return urlList;
	}
	
	public static List<URL> findResourceUrl(String packageName) throws CIBusException {
		List<URL> urlList = new ArrayList<URL>();
		for (String resourceName : findResource(packageName)) {
			try {
				urlList.add(classNameToUrl(resourceName, false));
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		return urlList;
	}
	
	public static List<URL> findResourceUrl(String packageName, ClassLoader clsLoader) throws CIBusException {
		List<URL> urlList = new ArrayList<URL>();
		for (String resourceName : findResource(packageName, clsLoader)) {
			try {
				urlList.add(classNameToUrl(resourceName, false));
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		return urlList;
	}
	
	public static List<URL> findXmlUrl(String packageName, ClassLoader clsLoader) throws CIBusException {
		List<URL> urlList = new ArrayList<URL>();
		for (String resourceName : findResource(packageName, clsLoader)) {
			try {
				if (resourceName.endsWith(XML_SUFFIX))
					urlList.add(classNameToUrl(resourceName, false));
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		return urlList;
	}
	
	public static List<String> getPropsResource(String packageName, ClassLoader clsLoader) throws CIBusException {
		List<String> propsList = new ArrayList<String>();
		for (String resourceName : findResource(packageName, clsLoader)) {
			if (resourceName.endsWith(PROPERTIES_SUFFIX)) {
				String props = resourceName.substring(0, resourceName.lastIndexOf(DOT));
				props = props.replace(File.separator, DOT);
				propsList.add(props);
			}
		}

		return propsList;
	}
	
	/**
	 * 获取某包下（包括该包的所有子包）所有类
	 * @throws CIBusException 
	 * @param @param packageName
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName) throws CIBusException {
		return findClasspath(packageName, true);
	}
	
	public static List<String> findResource(String packageName) throws CIBusException {
		return findResource(packageName, true);
	}

	/**
	 * 获取某包下所有类
	 * @throws CIBusException 
	 * @param @param packageName
	 * @param @param childPackage
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName, boolean childPackage) throws CIBusException {
		return findClasspath(packageName, Thread.currentThread().getContextClassLoader(), childPackage, true);
	}
	
	public static List<String> findResource(String packageName, boolean childPackage) throws CIBusException {
		return findClasspath(packageName, Thread.currentThread().getContextClassLoader(), childPackage, false);
	}
	
	/**
	 * 获取包下（包括该包的所有子包）所有类，可指定0到多个类加载容器
	 * @throws CIBusException 
	 * @param  @param packageName
	 * @param  @param clsLoaders
	 * @param  @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName,  ClassLoader... clsLoaders) throws CIBusException {
		if (clsLoaders.length == 0) {
			return findClasspath(packageName, true);
		}
		List<String> caList = new ArrayList<String>();
		for (ClassLoader clsLoader : clsLoaders) {
			caList.addAll(findClasspath(packageName, clsLoader, true, true));
		}
			
		return caList;
	}
	
	/**
	 * @throws CIBusException 
	 * 指定类加载器获取某包下（包括该包的所有子包）所有类
	 * @param  @param packageName
	 * @param  @param clsLoader
	 * @param  @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName, ClassLoader clsLoader) throws CIBusException {
		return findClasspath(packageName, clsLoader, true, true);
	}
	
	
	public static List<String> findResource(String packageName, ClassLoader clsLoader) throws CIBusException {
		return findClasspath(packageName, clsLoader, true, false);
	}

	
	/**
	 * @throws CIBusException 
	 * 获取指定类加载器某包下所有类
	 * @param  @param packageName
	 * @param  @param clsLoader
	 * @param  @param childPackage
	 * @param  @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName, ClassLoader clsLoader, 
			boolean childPackage, boolean searchClass) throws CIBusException {
		List<String> caList = new ArrayList<String>();
		Map<String, String> cMap = new HashMap<String, String>();
		String packagePath = packageName.replace(DOT, SLASH);
		try {
			Enumeration<URL> urls = clsLoader.getResources(packagePath);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				List<String> csList = searchClasspath(url, packageName, childPackage, searchClass);
				for (String cs : csList) {
					cMap.put(cs,  cs);
				}
			}
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
		// 多个url中查找包，包括包的下层子包，反加这些子包下所有class的列表
		List<URL> uList = searchPackageInJreEnv(packagePath);
		for (URL u : uList) {
			List<String> cnList = searchClassNameByJar(u.getPath(), childPackage, searchClass);
			List<String> csList = fetchAndTrimClassName(cnList, packageName);
			for (String cs : csList) {
				cMap.put(cs,  cs);
			}
		}
		for (String cname : cMap.keySet()) {
			caList.add(cname);
		}
		
		return caList;
	}
	
	/*
	 * 在一个url路径中找包下的所有类，返回这些类的url列表
	 */
	private static List<String> searchClasspath(URL url, String packageName, boolean childPackage, boolean searchClass) throws CIBusException {
		List<String> classNames = new ArrayList<String>();
		String type = url.getProtocol();
		if (type.equals("bundle")) {
			try {
				URLConnection conn = url.openConnection();
				url = (URL) ProxyUtil.invokeRet(conn, "getLocalURL");
				type = url.getProtocol();
			} catch (Exception e) {
				throw new CIBusException("", e);
			}
		} 
		if (type.equals("file")) {
			classNames = searchClassNameByFile(url.getPath(), packageName.replace(DOT, File.separator), childPackage, searchClass);
		} else if (type.equals("jar")) {
			List<String> cnList = searchClassNameByJar(url.getPath(), childPackage, searchClass);
			classNames = fetchAndTrimClassName(cnList, packageName);
		}
		return classNames;
	}
	
	private static List<String> fetchAndTrimClassName(List<String> cnList, String packageName) {
		List<String> classNames = new ArrayList<String>();
		for (String cn : cnList) {
			if (cn.startsWith(packageName)) {
				if (cn.endsWith(CLASS_SUFFIX)) {
					classNames.add(cn.substring(0, cn.length()-CLASS_SUFFIX.length()));
				}
			}
		}
		return classNames;
	}

	/*
	 * 文件中获取某包下所有类
	 */
	private static List<String> searchClassNameByFile(String filePath, String packagePath, boolean childPackage, boolean searchClass) {
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (childPackage) {
					myClassName.addAll(searchClassNameByFile(childFile.getPath(), packagePath, childPackage, searchClass));
				}
			} else {
				String childFilePath = childFile.getPath();
				if (!(childFilePath.endsWith(CLASS_SUFFIX) ^ searchClass)) {
					if (searchClass) {
						childFilePath = childFilePath.substring(
								childFilePath.lastIndexOf(packagePath),
								childFilePath.lastIndexOf(DOT));
						childFilePath = childFilePath.replace(File.separator, DOT);
					} else {
						childFilePath = childFilePath.substring(childFilePath.lastIndexOf(packagePath));
					}
					myClassName.add(childFilePath);
				}
			}
		}

		return myClassName;
	}

	/*
	 * 从jar获取某包下所有类
	 */
	private static List<String> searchClassNameByJar(String jarPath, boolean childPackage, boolean searchClass) {
		List<String> classNameList = new ArrayList<String>();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf(SLASH));
		String packagePath = jarInfo[1].substring(1);
		try {
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (!(entryName.endsWith(CLASS_SUFFIX) ^ searchClass)) {
					if (childPackage) {
						entryName = entryName.replace(SLASH, DOT);
						if (entryName.startsWith(packagePath) && searchClass) {
							entryName = entryName.substring(0, entryName.lastIndexOf(DOT));
						}
						classNameList.add(entryName);
					} else {
						int index = entryName.lastIndexOf(SLASH);
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						entryName = entryName.replace(SLASH, DOT);
						if (entryName.startsWith(packagePath) && searchClass) {
							entryName = entryName.substring(0, entryName.lastIndexOf(DOT));
						}
						classNameList.add(entryName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classNameList;
	}

	/*
	 * 从所有jar中搜索该包，并获取该包下所有类
	 */
	private static List<String> searchClassNameByJars(URL[] urls,
			String packagePath, boolean childPackage, boolean searchClass) {
		List<String> myClassName = new ArrayList<String>();
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				URL url = urls[i];
				String urlPath = url.getPath();
				// 不必搜索classes文件夹
				if (urlPath.endsWith("classes/") || urlPath.equals(SLASH)) {
					continue;
				}
				String jarPath = urlPath + "!/" + packagePath;
				myClassName.addAll(searchClassNameByJar(jarPath, childPackage, searchClass));
			}
		}
		return myClassName;
	}
	

	/*
	 * 在系统jar列表中查找包所对应的url
	 */
	private static List<URL> searchPackageInJreEnv(String name) {
		List<URL> urlList = new ArrayList<URL>();
		sun.misc.URLClassPath ucp = sun.misc.Launcher.getBootstrapClassPath();
		URL[] urls = ucp.getURLs();
		for (URL url : urls) {
			String proto = url.getProtocol();
			String path = url.getFile();
			if (path.endsWith(".jar")) {
				if (proto.equals("file") || proto.equals("jar")) {
					try {
						JarClassReader cReader = new JarClassReader(url.getFile());
						for (String p : cReader.getContainsPackageList(name)) {
							urlList.add(new URL("jar:file:/" + path + "!/" + p.replace(DOT, SLASH) + SLASH));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if (proto.equals("file")) {
					List<File> fList = FileUtil.getSubFolderList(url.getFile(), name);
					for (File f : fList) {
						try {
							urlList.add(f.toURI().toURL());
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return urlList;
	}
	
	/*
	 * class名称转换为所在URL表示
	 */
	private static URL classNameToUrl(ClassLoader loader, String className, boolean isClass) throws CIBusException {
		try {
			String class_name = className;
			if (isClass) {
				class_name = className.replace(".", "/") + ".class";
			}
			URL url = loader.getResource(class_name);
			if (url == null) {
				url = searchClassInJreEnv(class_name);
			}
			return url;
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	private static URL classNameToUrl(String className, boolean isClass) throws CIBusException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return classNameToUrl(loader, className, isClass);
	}
	
	/*
	 * 在系统jar列表中查找class所对应的url
	 */
	private static URL searchClassInJreEnv(String name) {
		sun.misc.URLClassPath ucp = sun.misc.Launcher.getBootstrapClassPath();
		URL[] urls = ucp.getURLs();
		for (URL url : urls) {
			String proto = url.getProtocol();
			String path = url.getFile();
			if (path.endsWith(".jar")) {
				if (proto.equals("file") || proto.equals("jar")) {
					try {
						JarClassReader cReader = new JarClassReader(url.getFile());
						if (name.endsWith(".class")) {
							if (cReader.existClass(name)) {
								return new URL("jar:file:/" + path + "!/" + name);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

	
}

