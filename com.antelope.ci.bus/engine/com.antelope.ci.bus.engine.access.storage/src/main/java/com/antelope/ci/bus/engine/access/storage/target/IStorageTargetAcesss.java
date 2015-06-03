// com.antelope.ci.bus.storage.StorageAcesss.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.storage.target;

import java.util.List;
import java.util.Properties;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-2		上午10:39:41 
 */
public interface IStorageTargetAcesss {
	public void open(Properties properties) throws CIBusException;
	
	public List<StorageTargetBox> ls(StorageTargetCondition condition);
	
	public void add(StorageTargetBox box);
	
	public void add(List<StorageTargetBox> boxes);
	
	public void rm(StorageTargetBox box);
	
	public void rm(List<StorageTargetBox> boxes);
	
	public void mv(StorageTargetBox src, StorageTargetBox dst);
	
	public void cp(StorageTargetBox src, StorageTargetBox dst);
}

