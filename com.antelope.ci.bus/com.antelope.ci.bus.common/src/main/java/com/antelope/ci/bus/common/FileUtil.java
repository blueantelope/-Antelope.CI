// com.antelope.ci.bus.common.FileUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;


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
	
	/**
	 * 递得取得目录下包含子目录的目录列表
	 * @param  @param rootDir
	 * @param  @param subDir
	 * @param  @return
	 * @return List<File>
	 * @throws
	 */
	public static List<File> getSubFolderList(String rootDir, String subDir) {
		List<File> subList = new ArrayList<File>();
		File d = new File(rootDir + File.separator + subDir);
		if (d.exists() && d.isDirectory()) {
			recurInsertFolder(subList, d);
		}
		
		return subList;
	}
	
	/**
	 * 新建文件，包括新文件的内容
	 * @param  @param filePathAndName
	 * @param  @param fileContent
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	public static void newFile(String filePathAndName, String fileContent) throws CIBusException {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			resultFile.close();
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	public static File genTempFolder(String name) throws CIBusException {
		String tmp = System.getProperty("java.io.tmpdir");
		File tmp_dir = new File(tmp);
		String tm = String.valueOf(System.currentTimeMillis());
		for (File fs : tmp_dir.listFiles()) {
			if (fs.isDirectory()) {
				if (fs.getName().equals(name)) {
					return createFolder(fs.getPath(), tm);
				}
			}
		}
		tmp_dir = new File(tmp, name);
		if (tmp_dir.mkdir()) {
			return createFolder(tmp_dir.getPath(), tm);
		}
		throw new CIBusException("", "can not create folder");
	}
	
	private static File createFolder(String parent, String tm) throws CIBusException {
		File tmp_dir = new File(parent, tm);
		if (tmp_dir.mkdir()) {
			return tmp_dir;
		}
		throw new CIBusException("", "can not create folder");
	}
	
	public static void delFolderWithDay(String name, String split, int days) {
		String tmp = System.getProperty("java.io.tmpdir");
		File tmp_dir = new File(tmp);
		Date now = new Date();
		for (File fs : tmp_dir.listFiles()) {
			if (fs.isDirectory()) {
				String dirname = fs.getName();
				if (dirname.equals(name)) {
					String[] ds = dirname.split("split");
					if (ds.length > 1) {
						try {
							Date d = DateUtil.toDate(ds[1]);
							if (DateUtil.differDay(now, d) >= days)
								delFolder(fs.getPath());
						} catch (CIBusException e) {
							
						}
					}
				}
			}
		}
	}
	
	public static File[] getChildFiles(File root, final String filename) {
		return root.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(filename);
			}
			
		});
	}
	
	public static String[] getChildFilenames(File root, final String filename) {
		return root.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(filename);
			}
			
		});
	}
	
	/*
	 * 递归插入目录下的所有子目录到文件列表中
	 */
	private static void recurInsertFolder(List<File> fileList, File root) {
		fileList.add(root);
		for (File f : root.listFiles()) {
			if (f.isDirectory()) {
				fileList.add(f);
				recurInsertFolder(fileList, f);
			}
		}
	}

}

