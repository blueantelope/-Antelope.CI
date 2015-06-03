// com.antelope.ci.bus.common.test.classfinder.TestFindXmlUrl.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.ClassFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.ClassFinder;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-12-17		下午5:50:17 
 */
public class TestFindXmlUrl extends TestCase {
	@Test
	public void test() throws CIBusException, IOException {
		List<URL> urlList = ClassFinder.findXmlUrl("com.antelope.ci.bus.common.test", this.getClass().getClassLoader());
		for (URL url : urlList) {
			System.out.println(url.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line=reader.readLine()) != null) {
				System.out.println(line);
			}
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestFindXmlUrl.class);
	}
}

