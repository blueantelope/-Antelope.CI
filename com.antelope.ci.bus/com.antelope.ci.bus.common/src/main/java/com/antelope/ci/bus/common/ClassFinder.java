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
    
	/**
	 * @throws CIBusException 
	 * 取得package下的所有类，并以类的url列表返回
	 * @param @param packageName
	 * @param @return
	 * @return List<URL>
	 * @throws
	 */
	public static List<URL> findClassUrl(String packageName) throws CIBusException {
		List<URL> urlList = new ArrayList<URL>();
		for (String className : findClasspath(packageName)) {
			try {
				urlList.add(classNameToUrl(className));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return urlList;
	}

	/**
	 * @throws CIBusException 
	 * 获取某包下（包括该包的所有子包）所有类
	 * @param @param packageName
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName) throws CIBusException {
		return findClasspath(packageName, true);
	}

	/**
	 * @throws CIBusException 
	 * 获取某包下所有类
	 * @param @param packageName
	 * @param @param childPackage
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName, boolean childPackage) throws CIBusException {
		return findClasspath(packageName, Thread.currentThread().getContextClassLoader(), childPackage);
	}
	
	/**
	 * @throws CIBusException 
	 * 获取包下（包括该包的所有子包）所有类，可指定0到多个类加载容器
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
			caList.addAll(findClasspath(packageName, clsLoader, true));
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
	public static List<String> findClasspath(String packageName,  ClassLoader clsLoader) throws CIBusException {
		return findClasspath(packageName, clsLoader, true);
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
	public static List<String> findClasspath(String packageName, ClassLoader clsLoader, boolean childPackage) throws CIBusException {
		List<String> caList = new ArrayList<String>();
		Map<String, String> cMap = new HashMap<String, String>();
		String packagePath = packageName.replace(DOT, SLASH);
		try {
			Enumeration<URL> urls = clsLoader.getResources(packagePath);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				for (String cs : searchClasspath(url, packageName, childPackage)) {
					cMap.put(cs,  cs);
				}
			}
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
		// 多个url中查找包，包括包的下层子包，反加这些子包下所有class的列表
		List<URL> uList = searchPackageInJreEnv(packagePath);
		for (URL u : uList) {
			for (String cs : searchClassNameByJar(u.getPath(), childPackage)) {
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
	private static List<String> searchClasspath(URL url, String packageName, boolean childPackage) throws CIBusException {
		List<String> fileNames = new ArrayList<String>();
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
			fileNames = searchClassNameByFile(url.getPath(), packageName.replace(DOT, File.separator), childPackage);
		} else if (type.equals("jar")) {
			fileNames = searchClassNameByJar(url.getPath(), childPackage);
		}
		return fileNames;
	}

	/*
	 * 文件中获取某包下所有类
	 */
	private static List<String> searchClassNameByFile(String filePath, String packagePath, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (childPackage) {
					myClassName.addAll(searchClassNameByFile(childFile.getPath(), packagePath, childPackage));
				}
			} else {
				String childFilePath = childFile.getPath();
				if (childFilePath.endsWith(CLASS_SUFFIX)) {
					childFilePath = childFilePath.substring(
							childFilePath.lastIndexOf(packagePath),
							childFilePath.lastIndexOf(DOT));
					childFilePath = childFilePath.replace(File.separator, DOT);
					myClassName.add(childFilePath);
				}
			}
		}

		return myClassName;
	}

	/*
	 * 从jar获取某包下所有类
	 */
	private static List<String> searchClassNameByJar(String jarPath,
			boolean childPackage) {
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
				if (entryName.endsWith(CLASS_SUFFIX)) {
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace(SLASH, DOT).substring(0, entryName.lastIndexOf(DOT));
							classNameList.add(entryName);
						}
					} else {
						int index = entryName.lastIndexOf(SLASH);
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						if (myPackagePath.equals(packagePath)) {
							entryName = entryName.replace(SLASH, DOT).substring(
									0, entryName.lastIndexOf(DOT));
							classNameList.add(entryName);
						}
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
			String packagePath, boolean childPackage) {
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
				myClassName.addAll(searchClassNameByJar(jarPath, childPackage));
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
	private static URL classNameToUrl(String className) throws CIBusException {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			String class_name = className.replace(".", "/") + ".class";
			URL url = loader.getResource(class_name);
			if (url == null) {
				url = searchClassInJreEnv(class_name);
			}
			return url;
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
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
