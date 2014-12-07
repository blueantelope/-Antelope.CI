// com.antelope.ci.bus.common.xml.test.TestParseXml.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test.xml;

import java.io.InputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-11-15		下午5:44:34 
 */
public class TestParseXml extends TestCase {
	private static final String xml_name = "test.xml";
	private InputStream xml_in;
	
	@Before
	protected void setUp() {
		xml_in = this.getClass().getResourceAsStream(xml_name);
	}
	
	@Test
	public void test() {
		try {
			PortalTerminal pt = (PortalTerminal) BusXmlHelper.parse(PortalTerminal.class, xml_in);
			System.out.println(pt.toString());
		} catch (CIBusException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestParseXml.class);
	}
}