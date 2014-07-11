// com.antelope.ci.bus.portal.configuration.xo.PlaceTree.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-6		上午10:59:12 
 */
public class PlacePartTree implements Serializable {
	private String name;
	private List<PlacePart> rootList;
	private List<PlacePartTree> childList;
	
	public PlacePartTree() {
		rootList = new ArrayList<PlacePart>();
		childList = new ArrayList<PlacePartTree>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PlacePart> getRootList() {
		return rootList;
	}
	public void setRootList(List<PlacePart> rootList) {
		this.rootList = rootList;
	}
	public List<PlacePartTree> getChildList() {
		return childList;
	}

	public void setChildList(List<PlacePartTree> childList) {
		this.childList = childList;
	}

	public void addChild(PlacePartTree child) {
		childList.add(child);
	}
	
	public Map<String, PlacePart> makeRootMap() {
		Map<String, PlacePart> rootMap = new HashMap<String, PlacePart>();
		if (rootList != null)
			for (PlacePart root : rootList)
				rootMap.put(root.getPlace(), root);
		return rootMap;
	}
	
	public Map<String, List<PlacePart>> getChildMap() {
		Map<String, List<PlacePart>> childMap = new HashMap<String, List<PlacePart>>();
		for (PlacePartTree child : childList)
			childMap.put(child.getName(), child.getRootList());
		return childMap;
	}
	
	public Map<String, Map<String, PlacePart>> makeChildPartMap() {
		Map<String, Map<String, PlacePart>> childMap = new HashMap<String, Map<String, PlacePart>>();
		for (PlacePartTree child : childList)
			childMap.put(child.getName(), child.makeRootMap());
		return childMap;
	}
	
	public Map<String, PlacePartTree> makeChildMap() {
		Map<String, PlacePartTree> childMap = new HashMap<String, PlacePartTree>();
		for (PlacePartTree child : childList)
			childMap.put(child.getName(), child);
		return childMap;
	}
	
	public List<String> genPlaceGroup() {
		List<String> pgList = new ArrayList<String>();
		if (rootList != null)
			for (PlacePart root : rootList)
				pgList.add(root.getPlace());
		if (childList != null)
			for (PlacePartTree child : childList)
				pgList.add(child.getName());
		return pgList;
	}
	
	public boolean isEmpty(Map<String, Part> partMap) {
		return isEmpty(this, partMap);
	}
	
	public boolean isEmpty(PlacePartTree tree, Map<String, Part> partMap) {
		List<PlacePart> rList = tree.getRootList();
		if (rList != null) {
			for (PlacePart r : rList) {
				Part p = partMap.get(r.getName());
				if (p != null)
					if (!p.contentEmpty())
						return false;
			}
		}
		
		List<PlacePartTree> cList = tree.getChildList();
		if (cList != null) {
			for (PlacePartTree c : cList)
				return isEmpty(c, partMap);
		}
		
		return true;
	}
	
}

