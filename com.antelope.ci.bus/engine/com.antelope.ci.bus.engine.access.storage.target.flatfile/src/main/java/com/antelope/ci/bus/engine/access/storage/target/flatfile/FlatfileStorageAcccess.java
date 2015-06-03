// com.antelope.ci.bus.storage.flatfile.DatabaseFlatfileAcccess.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.access.storage.target.flatfile;

import java.util.List;
import java.util.Properties;

import com.antelope.ci.bus.engine.access.storage.target.IStorageTargetAcesss;
import com.antelope.ci.bus.engine.access.storage.target.StorageTargetBox;
import com.antelope.ci.bus.engine.access.storage.target.StorageTargetCondition;



/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-9		上午11:20:46 
 */
public class FlatfileStorageAcccess implements IStorageTargetAcesss {
	
	public void open(Properties properties) {
		
		properties.getUrlList();
	}
	
	public FlatfileStorageAcccess() {
		super();
	}
	
	public 

	@Override
	public List<StorageTargetBox> ls(StorageTargetCondition condition) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public void add(StorageTargetBox box) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(List<StorageTargetBox> boxes) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rm(StorageTargetBox box) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rm(List<StorageTargetBox> boxes) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mv(StorageTargetBox src, StorageTargetBox dst) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cp(StorageTargetBox src, StorageTargetBox dst) {
		
		// TODO Auto-generated method stub
		
	}

}

