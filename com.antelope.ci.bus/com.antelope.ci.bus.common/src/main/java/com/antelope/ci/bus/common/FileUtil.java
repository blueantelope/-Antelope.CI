// com.antelope.ci.bus.common.FileUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.io.File;


/**
 * 文件类工具
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-7-31		下午9:46:45 
 */
public class FileUtil {
	/**
	 * 判断路径是否为目录
	 * @param  @param path
	 * @param  @return
	 * @return boolean
	 * @throws
	 */
	public static boolean existDir(String path) {
		if (new File(path).isDirectory())
			return true;
		return false;
	}
	
	/**
	 * 判断路径是否为文件
	 * @param  @param path
	 * @param  @return
	 * @return boolean
	 * @throws
	 */
	public static boolean existFile(String path) {
		if (new File(path).isFile())
			return true;
		return false;
	}
}

