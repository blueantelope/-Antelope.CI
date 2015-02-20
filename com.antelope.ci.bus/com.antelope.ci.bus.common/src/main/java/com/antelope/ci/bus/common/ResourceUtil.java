// com.antelope.ci.bus.common.ResourceUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.antelope.ci.bus.common.configration.BasicConfigrationReader;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * 资源类工具类
 * @author blueantelope
 * @version 0.1
 * @Date 2013-7-31 下午12:39:34
 */
public class ResourceUtil {
	public static InputStream getXmlStream(Class cls, String xpath) throws CIBusException {
		try {
			if (!StringUtil.endsWithIgnoreCase(xpath, ".xml"))
				xpath += ".xml";
			if (xpath.startsWith(BusConstants.CP_SUFFIX)) {
				String n_xpath = xpath.substring(BusConstants.CP_SUFFIX.length());
				return cls.getResourceAsStream(n_xpath);
			}
			
			if (xpath.startsWith(BusConstants.FILE_SUFFIX)) {
				String n_xpath = xpath.substring(BusConstants.FILE_SUFFIX.length());
				return new FileInputStream(n_xpath);
			}
			
			return new URL(xpath).openStream();
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	public static String replaceLableForProperties(String value, Properties props) {
		return replaceLable(value, props);
	}
	
	public static String replaceLableForReader(String value, BasicConfigrationReader r) {
		return replaceLable(value, r);
	}
	
	private static <T> String replaceLable(String value, T params) {
		StringBuffer buf = new StringBuffer();
		int len = value.length();
		int index = 0;
		while (index < len) {
			int start = value.indexOf(BusConstants.LABLE_START, index);
			if (start == -1)
				break;
			int end = value.indexOf(BusConstants.LABLE_END, start);
			String key = value.substring(start+2, end);
			String v;
			if (params instanceof BasicConfigrationReader)
				v = ((BasicConfigrationReader) params).getString(key);
			else
				v = ((Properties) params).getProperty(key);
			v = (v == null) ? "" : v;
			buf.append(value.substring(index, start)).append(v);
			index = end + 1;
		}
		
		if (index < len)
			buf.append(value.substring(index));
		
		return buf.toString();
	}
	
	public static boolean needReplace(String value) {
		String prefix = "\\$\\{";
		String suffix = "\\}";
		return StringUtil.contain(value, prefix, suffix);
	}
	
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
				clazz.getName().lastIndexOf(".") + 1)
				+ ".class");
		if (path == null) {
			String name = clazz.getName().replaceAll("[.]", "/");
			path = clazz.getResource("/" + name + ".class");
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
	 * 取得className最后的名称，带class后缀
	 * 
	 * @param @param className
	 * @param @return
	 * @return String
	 * @throws
	 */
	private static String getLastClassName(String className) {
		String[] cp = className.split("\\.");
		return cp[cp.length - 1] + ".class";
	}
}
