// com.antelope.ci.bus.common.PropertiesUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.util.Properties;


/**
 *	properties类操作工具集
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-5		下午10:33:04 
 */
public class PropertiesUtil {
	/**
	 * 得到属性中的int型参数
	 * 如果属性集为空或不存在相应的参数，就取默认值
	 * @param  @param props
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return int
	 * @throws
	 */
	public static int getInt(Properties props, String key, int def) {
		int value = def;
		if (props != null) {
			try {
				String v = props.getProperty(key, String.valueOf(def));
				value = Integer.valueOf(v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	public static long getLong(Properties props, String key, long def) {
		long value = def;
		if (props != null) {
			try {
				String v = props.getProperty(key, String.valueOf(def));
				value = Long.valueOf(v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	/**
	 * 得到属性中的string型参数
	 * 如果属性集为空或不存在相应的参数，就取默认值
	 * @param  @param props
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String getString(Properties props, String key, String def) {
		String value = def;
		if (props != null) {
			value = props.getProperty(key, def);
		}
		
		return value;
	}
	
	/**
	 * 得到属性中的布尔型参数
	 * 如果属性集为空或不存在相应的参数，就取默认值
	 * @param  @param props
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return boolean
	 * @throws
	 */
	public static boolean getBoolean(Properties props, String key, boolean def) {
		boolean value = def;
		if (props != null) {
			try {
				String v = props.getProperty(key, String.valueOf(def));
				value = Boolean.parseBoolean(v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	/**
	 * 得到属性中的double型参数
	 * 如果属性集为空或不存在相应的参数，就取默认值
	 * @param  @param props
	 * @param  @param key
	 * @param  @param def
	 * @param  @return
	 * @return double
	 * @throws
	 */
	public static double getDouble(Properties props, String key, double def) {
		double value = def;
		if (props != null) {
			try {
				String v = props.getProperty(key, String.valueOf(def));
				value = Double.valueOf(v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
}

