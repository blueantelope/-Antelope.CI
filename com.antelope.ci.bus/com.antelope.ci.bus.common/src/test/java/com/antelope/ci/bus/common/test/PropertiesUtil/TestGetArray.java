// com.antelope.ci.bus.common.test.PropertiesUtil.TestGetArray.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.PropertiesUtil;

import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.PropertiesUtil;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年6月2日		下午5:52:57 
 */
public class TestGetArray extends TestCase {
	@Test public void test() {
		String key = "array";
		Properties props = new Properties();
		props.put(key, new Integer[]{1, 2, 3});
		Integer[] arr = PropertiesUtil.getArray(props, key);
		if (arr != null)
			for (Integer e : arr) System.out.println(e);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestGetArray.class);
	}
}

