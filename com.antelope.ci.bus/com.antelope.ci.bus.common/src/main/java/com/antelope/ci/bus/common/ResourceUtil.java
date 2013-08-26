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

import com.antelope.ci.bus.common.configration.JarResourceReader;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 资源类工具类
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午12:39:34 
 */
public class ResourceUtil {
	private static final String BUS_PROPS = "META-INF/bus.properties";
	
	/**
	 * 取得jar所在的上级目录
	 * @param  @return
	 * @param  @throws  CIBusException
	 * @return URL
	 * @throws
	 */
	public static URL getJarParent() throws  CIBusException {
		return urlParent(getJarDir());
	}
	
	/**
	 * 取得jar所在的目录
	 * @param  @return
	 * @param  @throws  CIBusException
	 * @return URL
	 * @throws
	 */
	public static URL getJarDir() throws  CIBusException {
		URL jarUrl = ResourceUtil.class.getProtectionDomain().getCodeSource().getLocation();
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
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return String
	 * @throws
	 */
    public static String getClassPath() throws CIBusException {
        try {
            return java.net.URLDecoder.decode(getClassPathFile(ResourceUtil.class)
                    .getAbsolutePath(), "UTF-8");
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
     * 读取jar中配置文件
     * jar文件路径
     * @param  @param path
     * @param  @return
     * @return Properties
     * @throws CIBusException
     */
    public static JarBusProperty readJarBus(String path) throws CIBusException {
    		return readJarBus(new JarResourceReader(path));
    }
    
    /**
     * 读取jar中配置文件
     * jar文件实体
     * @param  @param jarFile
     * @param  @return
     * @param  @throws CIBusException
     * @return JarBusProperty
     * @throws
     */
    public static JarBusProperty readJarBus(File jarFile) throws CIBusException {
		return readJarBus(new JarResourceReader(jarFile));
    }
    
    /*
     * 由jar资源reader读取资源文件
     */
    private static JarBusProperty readJarBus(JarResourceReader reader) throws CIBusException {
    	reader.addResource(BUS_PROPS);
		JarBusProperty busProperty = new JarBusProperty();
		busProperty.setLoad(reader.getString(BusConstants.JAR_LOAD));
		busProperty.setStartLevel(reader.getInt(BusConstants.JAR_START_LEVEL));
		return busProperty;
    }
}

