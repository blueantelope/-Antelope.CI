// org.apache.felix.framework.cache.test.TestURLContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package org.apache.felix.framework.cache.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-3		下午9:29:57 
 */
public class TestURLContent extends TestCase {
	
	public void testUrlToClass() throws MalformedURLException {
		URL url = new URL("jar:file:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/jce.jar!/javax/crypto/Cipher.class");
		boolean isFind = isClassname(url, "javax.crypto.Cipher");
		System.out.println(isFind);
	}

	
	private boolean isClassname(URL url, String class_name) {
		String url_str = url.toString();
		if (url_str.endsWith(".class")) {
			url_str = url_str.substring(0, url_str.length()-".class".length());
			url_str = url_str.replace(File.separator, ".");
			if (url_str.endsWith(class_name))
				return true;
			
			return false;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestURLContent.class);
	}
}

