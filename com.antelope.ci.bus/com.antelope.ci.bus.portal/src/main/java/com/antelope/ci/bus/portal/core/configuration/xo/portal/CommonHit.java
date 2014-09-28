// com.antelope.ci.bus.portal.core.configuration.xo.meta.CommonHit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.XmlAttribute;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Scope;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月28日		上午10:47:29 
 */
public class CommonHit implements Serializable {
	protected String name;
	protected String scope;
	protected String mode;
	protected HitFont font;
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute(name="scope")
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public EU_Scope toScope() throws CIBusException {
		return EU_Scope.toScope(scope);
	}
	
	@XmlAttribute(name="mode")
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	@XmlElement(name="font")
	public HitFont getFont() {
		return font;
	}
	public void setFont(HitFont font) {
		this.font = font;
	}
	
	public boolean same(CommonHit hit) {
		if (hit.getName().trim().equalsIgnoreCase(name.trim())
			&& hit.getScope().trim().equalsIgnoreCase(scope.trim())
			&& hit.getMode().trim().equalsIgnoreCase(mode.trim()))
			return true;
		return false;
	}
	
	public int compareTo(CommonHit anotherHit) {
		int ret = StringUtil.compare(scope, anotherHit.getScope());
		if (ret == 0)
			ret = StringUtil.compare(mode, anotherHit.getMode());
		if (ret == 0)
			ret = StringUtil.compare(name, anotherHit.getName()); 
		return ret;
	}
	
	public static <H extends CommonHit> void deweightCommonHitList(List<H> hitList) {
		Collections.sort(hitList, new Comparator<H>() {
			@Override public int compare(H h1, H h2) {
				return h1.compareTo(h2);
			}
		});
		List<Integer> removeList = new ArrayList<Integer>();
		H lastHit = null;
		int n = 0;
		for (H hit : hitList) {
			if (lastHit == null) {
				continue;
			} else {
				if (hit.compareTo(lastHit) == 0)
					removeList.add(n);
			}
			lastHit = hit;
			n++;
		}
		if (!removeList.isEmpty()) {
			n = removeList.size() - 1;
			do {
				hitList.remove(n--);
			} while (n > 0);
		}
	}
	
	public static <H extends CommonHit> List<H> getGlobalHit(List<H> hitList) {
		List<H> globalHitList = new ArrayList<H>();
		for (H hit : hitList) {
			try {
				if (hit.toScope() == EU_Scope.GLOBAL)
					globalHitList.add(hit);
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return globalHitList;
	}
	
	public static <H extends CommonHit> List<H> merge(List<H> hitList, List<H> anotherHitList) {
		List<Integer> removeList = new ArrayList<Integer>();
		int index = 0;
		for (H hit : hitList) {
			for (H anotherHit : anotherHitList) {
				if (hit.same(anotherHit)) {
					removeList.add(index);
					break;
				}
			}
			index++;
		}
		if (!removeList.isEmpty()) {
			index = removeList.size() - 1;
			do {
				hitList.remove(index--);
			} while (index > 0) ;
		}
		hitList.addAll(anotherHitList);
		return hitList;
	}
}