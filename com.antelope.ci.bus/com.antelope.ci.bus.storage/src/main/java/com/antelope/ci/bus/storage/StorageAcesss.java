// com.antelope.ci.bus.storage.StorageAcesss.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.storage;

import java.util.List;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-2		上午10:39:41 
 */
public interface StorageAcesss {
	public void save(StorageBox box);
	
	public List<StorageBox> get();
	
	public void rm(StorageBox box);
	
	public void mv(StorageBox src, StorageBox dst);
	
	public void cp(StorageBox src, StorageBox dst);
}

