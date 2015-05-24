// com.antelope.ci.bus.storage.flatfile.DatabaseFlatfileAcccess.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.storage.flatfile;

import java.util.List;

import com.antelope.ci.bus.engine.access.Access;
import com.antelope.ci.bus.engine.access.storage.IStorageAcesss;
import com.antelope.ci.bus.engine.access.storage.StorageBox;
import com.antelope.ci.bus.engine.access.storage.StorageCondition;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-9		上午11:20:46 
 */
@Access(name="com.antelope.ci.bus.storage.flatfile")
public class FlatfileStorageAcccess implements IStorageAcesss {

	@Override
	public List<StorageBox> ls(StorageCondition condition) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public void add(StorageBox box) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(List<StorageBox> boxes) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rm(StorageBox box) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rm(List<StorageBox> boxes) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mv(StorageBox src, StorageBox dst) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cp(StorageBox src, StorageBox dst) {
		
		// TODO Auto-generated method stub
		
	}

}

