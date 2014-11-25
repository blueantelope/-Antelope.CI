// com.antelope.ci.bus.portal.configuration.xo.Margin.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.meta;

import java.io.Serializable;

import com.antelope.ci.bus.common.StringUtil;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-11		下午5:35:09 
 */
public class Margin implements Serializable {
	private int before;
	private int after;
	
	public int getBefore() {
		return before;
	}
	public void setBefore(int before) {
		this.before = before;
	}
	public int getAfter() {
		return after;
	}
	public void setAfter(int after) {
		this.after = after;
	}
	
	public static Margin parse(String value) {
		Margin margin = new Margin();
		if (!StringUtil.empty(value)) {
			String[] vs = value.trim().split(",");
			for (String v : vs) {
				if (!StringUtil.empty(v)) {
					String[] ivs = v.trim().split(":");
					if (ivs != null && ivs.length != 2)
						continue;
					String ivs1 = ivs[0];
					try {
						int ivs2 = Integer.valueOf(ivs[1]);
						EU_Margin em = EU_Margin.toMargin(ivs1);
						switch (em) {
							case BEFORE:
								margin.setBefore(ivs2);
								break;
							case AFTER:
								margin.setAfter(ivs2);
								break;
						}
					} catch (Exception e) {
						
					}
				}
			}
		}
		
		return margin;
	}
}