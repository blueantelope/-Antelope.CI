// com.antelope.ci.bus.common.FileUtil.java
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
import java.util.ArrayList;
import java.util.List;


/**
 * 文件类工具
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午9:46:45 
 */
public class FileUtil {
	/**
	 * 判断路径是否为目录
	 * @param  @param path
	 * @param  @return
	 * @return boolean
	 * @throws
	 */
	public static boolean existDir(String path) {
		if (new File(path).isDirectory())
			return true;
		return false;
	}
	
	/**
	 * 判断路径是否为文件
	 * @param  @param path
	 * @param  @return
	 * @return boolean
	 * @throws
	 */
	public static boolean existFile(String path) {
		if (new File(path).isFile())
			return true;
		return false;
	}
	
	/**
	 * 取得目录下所有的jar文件，
	 * 得到其url后，组装成url列表返回
	 * @param  @param path
	 * @param  @return
	 * @return List<URL>
	 * @throws
	 */
	public static List<URL> getAllJar(String path) {
		List<URL> urlList = new ArrayList<URL>();
		File[] files = new File(path).listFiles();
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith("jar")) {
				try {
					urlList.add(file.toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return urlList;
	}
}

