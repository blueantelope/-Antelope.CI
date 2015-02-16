// com.antelope.ci.bus.portal.configuration.xo.Action.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Scope;
import com.antelope.ci.bus.server.shell.base.BusShellMode.BaseShellMode;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-10		上午11:13:46 
 */
@XmlEntity(name="action")
public class Action implements Serializable {
	protected RenderFont font;
	protected List<Hit> hitList;
	protected List<HitGroup> hitgroupList;
	
	public Action() {
		super();
		this.hitList = new ArrayList<Hit>();
		this.hitgroupList = new ArrayList<HitGroup>();
	}
	
	@XmlElement(name="font")
	public RenderFont getFont() {
		return font;
	}
	public void setFont(RenderFont font) {
		this.font = font;
	}

	@XmlElement(name="hit", isList=true, listClass=Hit.class)
	public List<Hit> getHitList() {
		return hitList;
	}
	public void setHitList(List<Hit> hitList) {
		this.hitList = hitList;
	}
	public void deweightHitList() {
		CommonHit.deweightCommonHitList(hitList);
	}
	
	@XmlElement(name="hitgroup", isList=true, listClass=HitGroup.class)
	public List<HitGroup> getHitgroupList() {
		return hitgroupList;
	}
	public void setHitgroupList(List<HitGroup> hitgroupList) {
		this.hitgroupList = hitgroupList;
	}
	public void deweightHitgroupList() {
		CommonHit.deweightCommonHitList(hitgroupList);
	}
	
	public void deweight() {
		deweightHitList();
		deweightHitgroupList();
	}
	
	public List<Hit> getGlobalHit() {
		return CommonHit.getGlobalHit(hitList);
	}
	
	public Hit getHit(String scope, String mode, String name) {
		return CommonHit.getHit(hitList, scope, mode, name);
	}
	
	public HitGroup getHitGroup(String scope, String mode, String name) {
		return CommonHit.getHit(hitgroupList, scope, mode, name);
	}
	
	public List<HitGroup> getGlobalHitgroup() {
		return CommonHit.getGlobalHit(hitgroupList);
	}
	
	public HitGroup getBlockHit() {
		for (HitGroup hitgroup : hitgroupList) {
			if (hitgroup.isBlock())
				return hitgroup;
		}
		
		return null;
	}
	
	public void merge(Action anotherAction) {
		if (anotherAction != null) {
			hitList = CommonHit.merge(hitList, anotherAction.getHitList());
			hitgroupList = CommonHit.merge(hitgroupList, anotherAction.getHitgroupList());
		}
	}
	
	private RenderFont getHitFont(String scope, String mode, String name) {
		return CommonHit.getHitFont(hitList, scope, mode, name);
	}
	
	private RenderFont getHitGroupFont(String scope, String mode, String name) {
		return CommonHit.getHitFont(hitgroupList, scope, mode, name);
	}
	
	public RenderFont getSwitchHitFont() {
		RenderFont font = getHitFont(EU_Scope.GLOBAL.getName(), BaseShellMode.MAIN.getSimple(), "switch_portal");
		if (font != null)
			return font;
		font = getHitGroupFont(EU_Scope.GLOBAL.getName(), BaseShellMode.MAIN.getSimple(), "switch_portal");
		if (font != null)
			return font;
		
		return font;
	}
}