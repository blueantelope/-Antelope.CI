// com.antelope.ci.bus.common.ResourceUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * 资源类工具类
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-7-31 下午12:39:34
 */
public class ResourceUtil {
	private final static String DOT = ".";
	private final static String SLASH = "/";
    private final static String CLASS_SUFFIX = ".class";
	
	/**
	 * 取得jar所在的上级目录
	 * 
	 * @param @return
	 * @param @throws CIBusException
	 * @return URL
	 * @throws
	 */
	public static URL getJarParent() throws CIBusException {
		return urlParent(getJarDir());
	}

	/**
	 * 取得jar所在的目录
	 * 
	 * @param @return
	 * @param @throws CIBusException
	 * @return URL
	 * @throws
	 */
	public static URL getJarDir() throws CIBusException {
		URL jarUrl = ResourceUtil.class.getProtectionDomain().getCodeSource()
				.getLocation();
		return urlParent(jarUrl);
	}

	/*
	 * 取得url指向文件的上级目录
	 */
	private static URL urlParent(URL url) throws CIBusException {
		try {
			return new File(url.getFile()).getParentFile().toURI().toURL();
		} catch (MalformedURLException e) {
			throw new CIBusException("00002", e);
		}
	}

	/**
	 * 取得class对应的路径
	 * 
	 * @param @return
	 * @param @throws CIBusException
	 * @return String
	 * @throws
	 */
	public static String getClassPath() throws CIBusException {
		try {
			return java.net.URLDecoder.decode(
					getClassPathFile(ResourceUtil.class).getAbsolutePath(),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new CIBusException("00001", e);
		}
	}

	private static File getClassFile(Class clazz) {
		URL path = clazz.getResource(clazz.getName().substring(
				clazz.getName().lastIndexOf(DOT) + 1)
				+ "CLASS_SUFFIX");
		if (path == null) {
			String name = clazz.getName().replaceAll("[.]", DOT);
			path = clazz.getResource(SLASH + name + "CLASS_SUFFIX");
		}
		return new File(path.getFile());
	}

	private static File getClassPathFile(Class clazz) {
		File file = getClassFile(clazz);
		for (int i = 0, count = clazz.getName().split("[.]").length; i < count; i++)
			file = file.getParentFile();
		if (file.getName().toUpperCase().endsWith(".JAR!")) {
			file = file.getParentFile();
		}
		return file;
	}

	/**
	 * class名称转换为所在URL表示
	 * 
	 * @param @param className
	 * @param @return
	 * @param @throws CIBusException
	 * @return URL
	 * @throws
	 */
	public static URL classNameToUrl(String className) throws CIBusException {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			String class_name = className.replace(DOT, SLASH) + "CLASS_SUFFIX";
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
						if (name.endsWith("CLASS_SUFFIX")) {
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

	/**
	 * 取得className最后的名称，带class后缀
	 * @param @param className
	 * @param @return
	 * @return String
	 * @throws
	 */
	private static String getLastClassName(String className) {
		String[] cp = className.split("\\.");
		return cp[cp.length - 1] + "CLASS_SUFFIX";
	}

	/**
	 * 取得package下的所有类，并以类的url列表返回
	 * @param @param packageName
	 * @param @return
	 * @return List<URL>
	 * @throws
	 */
	public static List<URL> findClassUrl(String packageName) {
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
	 * 获取某包下（包括该包的所有子包）所有类
	 * @param @param packageName
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName) {
		return findClasspath(packageName, true);
	}

	/**
	 * 获取某包下所有类
	 * @param @param packageName
	 * @param @param childPackage
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName, boolean childPackage) {
		return findClasspath(packageName, Thread.currentThread().getContextClassLoader(), childPackage);
	}
	
	/**
	 * 获取包下（包括该包的所有子包）所有类，可指定0到多个类加载容器
	 * @param  @param packageName
	 * @param  @param clsLoaders
	 * @param  @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName,  ClassLoader... clsLoaders) {
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
	 * 指定类加载器获取某包下（包括该包的所有子包）所有类
	 * @param  @param packageName
	 * @param  @param clsLoader
	 * @param  @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName,  ClassLoader clsLoader) {
		return findClasspath(packageName, clsLoader, true);
	}
	
	
	/**
	 * 获取指定类加载器某包下所有类
	 * @param  @param packageName
	 * @param  @param clsLoader
	 * @param  @param childPackage
	 * @param  @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> findClasspath(String packageName, ClassLoader clsLoader, boolean childPackage) {
		List<String> caList = new ArrayList<String>();
		Map<String, String> cMap = new HashMap<String, String>();
		String packagePath = packageName.replace(DOT, SLASH);
		URL url = clsLoader.getResource(packagePath);
		if (url != null) {
			for (String cs : searchClasspath(url, clsLoader, packageName, childPackage)) {
				cMap.put(cs,  cs);
			}
		}
		// 多个url中查找包，包括包的下层子包，反加这些子包下所有class的列表
		List<URL> uList = searchPackageInJreEnv(packagePath);
		for (URL u : uList) {
			for (String cs : searchClasspath(u, clsLoader, packageName, childPackage)) {
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
	private static List<String> searchClasspath(URL url, ClassLoader loader, String packageName, boolean childPackage) {
		String packagePath = packageName.replace(".",  "/");
		List<String> fileNames = null;
		if (url != null) {
			String type = url.getProtocol();
			if (type.equals("file")) {
				fileNames = searchClassNameByFile(url.getPath(), packagePath, childPackage);
			} else if (type.equals("jar")) {
				fileNames = searchClassNameByJar(url.getPath(), childPackage);
			}
		} else {
			fileNames = searchClassNameByJars(((URLClassLoader) loader).getURLs(),
					packagePath, childPackage);
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
					childFilePath = childFilePath.replace(SLASH, DOT);
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
//			DebugUtil.assert_out("jar路径 = " + jarPath + ", jar文件路径 = " + jarFilePath);
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith("CLASS_SUFFIX")) {
					//DebugUtil.assert_out(entryName);
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace(SLASH, DOT).substring(0, entryName.lastIndexOf(SLASH));
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
}
