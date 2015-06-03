// com.antelope.ci.bus.engine.access.storage.target.CommonStorageTargetAcesss.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.storage.target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.PropertiesUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年6月2日		下午5:19:28 
 */
public abstract class CommonStorageTargetAcesss implements IStorageTargetAcesss {
	protected static final String URL_LIST = "target.url.list";
	
	protected Properties properties;
	@Override
	public void open(Properties properties) throws CIBusException {
		if (check(properties))
			this.properties = properties;
		else
			throw new CIBusException("", "can not pass validation.");
	}
	
	protected URL[] getUrlList() {
		return PropertiesUtil.getArray(properties, URL_LIST);
	}
	
	protected InputStream[] openUrl() {
		URL[] urls = getUrlList();
		if (urls == null)
			return new InputStream[] {};
		InputStream[] ins = new InputStream[urls.length];
		int n = 0;
		for (URL url : urls) {
			try {
				ins[n] = url.openStream();
				n++;
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return ins;
	}
	
	protected abstract boolean check(Properties properties);
}
