// com.antelope.ci.bus.JarFileReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * jar包中class读取器
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-8-22 下午11:10:52
 */
public class JarClassReader {
	private JarFile jarFile = null;
	private JarInputStream jarInput;
	private HashMap<String, String> packageMap;				// 类中所有的包
	private HashMap<String, ByteArrayOutputStream> classStreamMap;

	public JarClassReader(String jarPath) throws IOException {
		packageMap = new HashMap<String, String>();
		classStreamMap = new HashMap<String, ByteArrayOutputStream>();
		try {
			jarFile = new JarFile(jarPath);
			jarInput = new JarInputStream(new FileInputStream(jarPath));
			readEntries();
		} catch (FileNotFoundException e) {
			
		}
	}

	private void readEntries() throws IOException {
		Enumeration e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = (JarEntry) e.nextElement();
			if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
				continue;
			}
			String className = entry.getName().substring(0, entry.getName().length() - 6);
			className = className.replace('/', '.');
			addClassInputStream(jarInput, className);
			if (className.contains(".")) {
				String packageName = className.substring(0, className.lastIndexOf("."));
				packageMap.put(packageName, packageName);
			}
		}
	}

	private void addClassInputStream(InputStream in, String className) {
		if (!classStreamMap.containsKey(className)) {
			ByteArrayOutputStream _copy = null;
			try {
				_copy = new ByteArrayOutputStream();
				int read = 0;
				int chunk = 0;
				byte[] data = new byte[256];
				while ((chunk = in.read(data)) != -1) {
					read += data.length;
					_copy.write(data, 0, chunk);
				}
				classStreamMap.put(className, _copy);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (_copy != null) {
					try {
						_copy.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
	
	/**
	 * 取得所有的包下有类存在的包名
	 * @param  @return
	 * @return List<String>
	 * @throws
	 */
	public List<String> getPackageList() {
		List<String> pList = new ArrayList<String>();
		for (String p : packageMap.keySet()) {
			pList.add(p);
		}
		
		return pList;
	}
	
	/**
	 * 取得jar中包含包名的包列表
	 * @param  @param packageName
	 * @param  @return
	 * @return List<String>
	 * @throws
	 */
	public List<String> getContainsPackageList(String packageName) {
		List<String> pList = new ArrayList<String>();
		if (jarFile == null) {
			return pList;
		}
		String cpa = packageName.replace("/", ".");
		DebugUtil.assert_out(jarFile.getName());
		for (String pa : packageMap.keySet()) {
			if (pa.startsWith(cpa))
				pList.add(pa);
		}
		
		return pList;
	}
	
	/**
	 * jar中是否存在包
	 * @param  @param packageName
	 * @param  @return
	 * @return boolean
	 * @throws
	 */
	public boolean existPackage(String packageName) {
		String cpa = packageName.replace("/", ".");
		for (String pa : packageMap.keySet()) {
			if (pa.equals(cpa))
				return true;
		}
		
		return false;
	}
	
	/**
	 * 给出的包名是否在jar中存在子包
	 * @param  @param packageName
	 * @param  @return
	 * @return boolean
	 * @throws
	 */
	public boolean containPackage(String packageName) {
		String cpa = packageName.replace("/", ".");
		for (String pa : packageMap.keySet()) {
			if (cpa.startsWith(pa))
				return true;
		}
		
		return false;
	}
	
	/**
	 * jar中是否存在类
	 * @param  @param className
	 * @param  @return
	 * @return boolean
	 * @throws
	 */
	public boolean existClass(String className) {
		String cn = className.substring(0, className.length()-6);
		cn = cn.replace("/", ".");
		for (String c : classStreamMap.keySet()) {
			if (cn.equals(c))
				return true;
		}
		
		return false;
	}

	/**
	 * 取得jar中每个class的输入流
	 * 
	 * @param @param className
	 * @param @return
	 * @return InputStream
	 * @throws
	 */
	public InputStream getClassStream(String className) {
		ByteArrayOutputStream _copy = classStreamMap.get(className);
		byte[] bs = _copy.toByteArray();
		try {
			_copy.close();
		} catch (IOException e) {
		}
		return (InputStream) new ByteArrayInputStream(bs);
	}

	/**
	 * 取得jar中每个class的字节数组
	 * 
	 * @param @param className
	 * @param @return
	 * @return byte[]
	 * @throws
	 */
	public byte[] getClassBytes(String className) {
		ByteArrayOutputStream _copy = classStreamMap.get(className);
		byte[] bs = _copy.toByteArray();
		try {
			_copy.close();
		} catch (IOException e) {
		}
		return bs;
	}
	
	public void close() {
		if (jarInput != null) {
			try {
				jarInput.close();
			} catch (IOException e) {
			}
		}
		if (jarFile != null) {
			try {
				jarFile.close();
			} catch (IOException e) {
			}
		}
	}
}
