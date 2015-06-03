// com.antelope.ci.bus.engine.access.storage.produce.IStorageProduceAcesss.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.storage.produce;

import java.util.List;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月25日		上午11:11:42 
 */
public interface IStorageProduceAcesss {
	public List<StorageProduceBox> ls(StorageProduceCondition condition);
	
	public void add(StorageProduceBox box);
	
	public void add(List<StorageProduceBox> boxes);
	
	public void rm(StorageProduceBox box);
	
	public void rm(List<StorageProduceBox> boxes);
	
	public void mv(StorageProduceBox src, StorageProduceBox dst);
	
	public void cp(StorageProduceBox src, StorageProduceBox dst);
}
