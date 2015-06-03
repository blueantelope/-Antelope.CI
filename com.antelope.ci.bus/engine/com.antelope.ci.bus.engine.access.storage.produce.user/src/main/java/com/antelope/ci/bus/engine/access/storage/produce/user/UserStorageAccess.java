// com.antelope.ci.bus.engine.access.storage.produce.user.UserStorageAccess.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.storage.produce.user;

import java.util.List;

import com.antelope.ci.bus.engine.access.Access;
import com.antelope.ci.bus.engine.access.storage.produce.IStorageProduceAcesss;
import com.antelope.ci.bus.engine.access.storage.produce.StorageProduceBox;
import com.antelope.ci.bus.engine.access.storage.produce.StorageProduceCondition;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年5月25日		上午11:22:36 
 */
@Access(name="com.antelope.ci.bus.engine.access.storage.produce.user")
public class UserStorageAccess implements IStorageProduceAcesss {

	@Override
	public List<StorageProduceBox> ls(StorageProduceCondition condition) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public void add(StorageProduceBox box) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(List<StorageProduceBox> boxes) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rm(StorageProduceBox box) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rm(List<StorageProduceBox> boxes) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mv(StorageProduceBox src, StorageProduceBox dst) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cp(StorageProduceBox src, StorageProduceBox dst) {
		
		// TODO Auto-generated method stub
		
	}

}

