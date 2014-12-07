// com.antelope.ci.bus.model.project.Project.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.engine.model.project;

import java.util.Date;

import com.antelope.ci.bus.engine.model.BaseModel;
import com.antelope.ci.bus.engine.model.Model;
import com.antelope.ci.bus.engine.model.ModelData;


/**
 * project model data
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-7-1		下午5:50:11 
 */
@Model
public class Project extends BaseModel {
	protected String name;
	protected String desc;
	protected Date createDate;
	protected String creater;
	
	
	public String getName() {
		return name;
	}
	@ModelData(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDesc() {
		return desc;
	}
	@ModelData(name="desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	@ModelData(name="createDate")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getCreater() {
		return creater;
	}
	@ModelData(name="creater")
	public void setCreater(String creater) {
		this.creater = creater;
	}
}

