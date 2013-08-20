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
		if (null != files) {
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith("jar")) {
					try {
						urlList.add(file.toURI().toURL());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return urlList;
	}
	
	
	
	/**
	 * 递归删除目录下的所有子目录及文件 
	 * 只剩下一个空目录 
	 * @param  @param path
	 * @return void
	 * @throws
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}
	
	/**
	 * 删除目录下的所有文件, 并且将目录本身也删除
	 * @param  @param folderPath
	 * @return void
	 * @throws
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();
		}
	}

}

