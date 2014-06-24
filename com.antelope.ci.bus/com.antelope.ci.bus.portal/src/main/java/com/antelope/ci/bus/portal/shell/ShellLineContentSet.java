// com.antelope.ci.bus.portal.shell.ShellLineContentSet.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-6-23		下午1:51:16 
 */
public class ShellLineContentSet {
	private List<List<String>> contentList;
	
	public ShellLineContentSet() {
		contentList = new ArrayList<List<String>>();
	}

	public List<List<String>> getContentList() {
		return contentList;
	}

	public void setContentList(List<List<String>> contentList) {
		this.contentList = contentList;
	}

	public void addLine(List<String> line) {
		contentList.add(line);
	}
}

