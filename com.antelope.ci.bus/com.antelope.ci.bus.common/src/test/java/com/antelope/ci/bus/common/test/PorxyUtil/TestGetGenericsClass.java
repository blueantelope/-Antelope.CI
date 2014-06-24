// com.antelope.ci.bus.common.test.PorxyUtil.TestGetGenericsClass.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common.test.PorxyUtil;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import com.antelope.ci.bus.common.ProxyUtil;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2014-6-23 上午11:53:42
 */
public class TestGetGenericsClass extends TestCase {
	@Test public void test() {
		List<List<String>> o1 = new ArrayList<List<String>>();
		ParameterizedType type = (ParameterizedType) o1.getClass().getGenericSuperclass();
		TypeVariableImpl trueType = (TypeVariableImpl) type.getActualTypeArguments()[0];
		System.out.println(trueType.getGenericDeclaration());
		
		trueType =  (TypeVariableImpl) trueType.getBounds()[0];
		System.out.println(trueType.getGenericDeclaration());
		
		trueType =  (TypeVariableImpl) trueType.getGenericDeclaration().getTypeParameters()[0];
		System.out.println(trueType.getGenericDeclaration());
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestGetGenericsClass.class);
	}
}
