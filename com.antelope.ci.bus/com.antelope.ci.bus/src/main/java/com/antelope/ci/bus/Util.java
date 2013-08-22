// com.antelope.ci.bus.Utils.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.felix.framework.BundleWiringImpl.BundleClassLoader;
import org.osgi.framework.Bundle;


/**
 * 工具类
 * 只在bus主应用中使用
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-22		下午10:12:43 
 */
public class Util {
	/**
	 * 得到bundle的类加载器
	 * @param  @param bundle
	 * @param  @return
	 * @return ClassLoader
	 * @throws
	 */
	public static ClassLoader getBundleClassLoader(Bundle bundle) {
        // 搜索Bundle中所有的class文件
        Enumeration<URL> classFileEntries = bundle.findEntries("/", "*.class",
                true);
        if (classFileEntries == null || !classFileEntries.hasMoreElements()) {
            throw new RuntimeException(String.format("Bundle[%s]中没有一个Java类！",
                    bundle.getSymbolicName()));
        }
        // 得到其中的一个类文件的URL
        URL url = classFileEntries.nextElement();
        // 得到路径信息
        String bundleOneClassName = url.getPath();
        // 将"/"替换为"."，得到类名称
        bundleOneClassName = bundleOneClassName.replace("/", ".").substring(0,
                bundleOneClassName.lastIndexOf("."));
        // 如果类名以"."开头，则移除这个点
        while (bundleOneClassName.startsWith(".")) {
            bundleOneClassName = bundleOneClassName.substring(1);
        }
        Class<?> bundleOneClass = null;
        try {
            // 让Bundle加载这个类
            bundleOneClass = bundle.loadClass(bundleOneClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 得到Bundle的ClassLoader
        return bundleOneClass.getClassLoader();
    }
	
	/**
	 * 加载jar包到bundle中
	 * @param  @param bundle
	 * @param  @param jarUrlList
	 * @param  @throws Exception
	 * @return void
	 * @throws
	 */
	public static void loadJarToBundle(Bundle bundle, List<URL> jarUrlList) throws Exception {
		ClassLoader cl = getBundleClassLoader(bundle);
		for (URL jarUrl : jarUrlList) {
			JarFile jarFile = new JarFile(jarUrl.getFile());
	        Enumeration e = jarFile.entries();

	        while (e.hasMoreElements()) {
	            JarEntry je = (JarEntry) e.nextElement();
	            if(je.isDirectory() || !je.getName().endsWith(".class")){
	                continue;
	            }
	            String className = je.getName().substring(0,je.getName().length()-6);
	            
	            className = className.replace('/', '.');
	            Class c = cl.loadClass(className);
	        }
		}
	}
}

