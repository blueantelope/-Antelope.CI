// com.antelope.ci.bus.common.JsonUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月10日		上午11:29:33 
 */
public class JsonUtil {
	public static List<Map<String, String>> toMapList(String json, String[] keys) {
		List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
		JSONArray jarr = new JSONArray(json);
		int n = 0;
		while (n < jarr.length()) {
			JSONObject jobj = jarr.getJSONObject(n);
			Map<String, String> m = new HashMap<String, String>();
			for (String key : keys) {
				String value = jobj.getString(key);
				if (!StringUtil.empty(value))
					m.put(key, value);
			}
			if (!m.isEmpty())
				mList.add(m);
			n++;
		}
		
		return mList;
	}
}

