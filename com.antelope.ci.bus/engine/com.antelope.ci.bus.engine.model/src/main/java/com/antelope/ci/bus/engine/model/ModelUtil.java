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
import java.util.ArrayList;
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
import com.antelope.ci.bus.common.aql.AqlEntry;
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
				throw new CIBusException("", "unkown bt");
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
		List<ModelStatement> statementList = fetchStatement(model);
		
		Map<String, Method> setterMap = new HashMap<String, Method>();
		for (Method method : model.getClass().getMethods()) {
			String mname = method.getName();
			if (method.getParameterTypes().length == 1) {
				for (ModelStatement statement : statementList) {
					if (statement.setter.equals(mname))
						setterMap.put(statement.name, method);
				}
			}
		}

		return setterMap;
	}
	
	public static List<AqlEntry> toAqlEntries(IModel model, Map<String, String> conditionMap) {
		List<ModelStatement> statementList = fetchStatement(model);
		
		List<AqlEntry> entries = new ArrayList<AqlEntry>();
		for (Method method : model.getClass().getMethods()) {
			String mname = method.getName();
			if (method.getParameterTypes() == null || method.getParameterTypes().length < 1) {
				for (ModelStatement statement : statementList) {
					if (statement.getter.equals(mname)) {
						try {
							String s_name = statement.name;
							String s_value = ProxyUtil.invokeRet(model, method).toString();
							AqlEntry entry = new AqlEntry(s_name, s_value);
							if (conditionMap != null && conditionMap.containsKey(s_name)) {
								try {
									entry.codeToKeyword(conditionMap.get(s_name));
								} catch (CIBusException e) {
									log.warn("set AQL Logic From Name : " + e);
								}
							}
						} catch (CIBusException e) {
							log.warn("create AQL Entry : \n" + e);
						}
					}
						
				}
			}
		}
		
		return entries;
	}
	
	private static List<ModelStatement> fetchStatement(IModel model) {
		List<ModelStatement> statementList = new ArrayList<ModelStatement>();
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
					String getter = mdata.getter();
					statementList.add(new ModelStatement(name, getter, setter));
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		return statementList;
	}
	
	private static class ModelStatement {
		String name;
		String getter;
		String setter;
		
		public ModelStatement() {
			super();
		}
		
		public ModelStatement(String name, String getter, String setter) {
			super();
			this.name = name;
			this.getter = getter;
			this.setter = setter;
		}
	}
}

