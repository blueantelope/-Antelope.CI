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


/**
 * 资源类工具类
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午12:39:34 
 */
public class ResourceUtil {
	
	/**
	 * 取得jar所在的上级目录
	 * @param  @return
	 * @param  @throws MalformedURLException
	 * @return URL
	 * @throws
	 */
	public static URL getJarParent() throws MalformedURLException {
		return urlParent(getJarDir());
	}
	
	/**
	 * 取得jar所在的目录
	 * @param  @return
	 * @param  @throws MalformedURLException
	 * @return URL
	 * @throws
	 */
	public static URL getJarDir() throws MalformedURLException {
		URL jarUrl = ResourceUtil.class.getProtectionDomain().getCodeSource().getLocation();
		return urlParent(jarUrl);
	}
	
	/*
	 * 取得url指向文件的上级目录
	 */
	private static URL urlParent(URL url) throws MalformedURLException {
		return new File(url.getFile()).getParentFile().toURI().toURL();
	}
	
	/**
	 * 取得class对应的路径
	 * @param  @return
	 * @return String
	 * @throws
	 */
    public static String getClassPath() {
        try {
            return java.net.URLDecoder.decode(getClassPathFile(ResourceUtil.class)
                    .getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
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
}

