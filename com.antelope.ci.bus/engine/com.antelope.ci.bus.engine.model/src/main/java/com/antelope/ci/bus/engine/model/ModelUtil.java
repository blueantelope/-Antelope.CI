// com.antelope.ci.bus.engine.model.ModelUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.api.ApiMessage;
import com.antelope.ci.bus.common.api.ApiUtil;
import com.antelope.ci.bus.common.api.BT;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月9日		下午3:48:52 
 */
public class ModelUtil {
	private static final Logger log = Logger.getLogger(ModelUtil.class);
	
	public static List<Map<String, String>> parseBody(ApiMessage message) throws CIBusException {
		List<Map<String, String>> mList = null;
		switch (message.getBt()) {
			case BT._binary:
				log.warn("no support.");
				break;
			case BT._json:
				String bstr;
				try {
					bstr = new String(message.getBody(), "utf-8");
				} catch (IOException e) {
					throw new CIBusException("convert body to string", e); 
				}
				mList = ApiUtil.fromJson(bstr);
				break;
			default:
				throw new CIBusException("", "");
		}
		
		return mList;
	}
	
	public static Map<String, String> makeValueMap(List<Map<String, String>> mList) {
		Map<String, String> vmap = new HashMap<String, String>();
		for (Map<String, String> m : mList) {
			if (m.containsKey("name") && m.containsKey("value"))
				vmap.put(m.get("name"), m.get("value"));
		}
		
		return vmap;
	}
	
	public static Map<String, String> makeCoditionMap(List<Map<String, String>> mList) {
		Map<String, String> cmap = new HashMap<String, String>();
		for (Map<String, String> m : mList) {
			if (m.containsKey("name") && m.containsKey("condition"))
				cmap.put(m.get("name"), m.get("condition"));
		}
		
		return cmap;
	}
	
	public static Map<String, Method> fetchSetter(IModel model) {
		Map<String, String> tempMap = new HashMap<String, String>();
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				if (field.isAnnotationPresent(ModelData.class)) {
					ModelData mdata = field.getAnnotation(ModelData.class);
					String name = mdata.name();
					if (StringUtil.empty(name))
						name = field.getName();
					String setter = mdata.setter();
					tempMap.put(setter, name);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		Map<String, Method> setterMap = new HashMap<String, Method>();
		for (Method method : model.getClass().getMethods()) {
			String mname = method.getName();
			if (tempMap.containsKey(mname) && method.getParameterTypes().length == 1)
				setterMap.put(tempMap.get(mname), method);
		}

		return setterMap;
	}
}

