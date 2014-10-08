// com.antelope.ci.bus.portal.configuration.xo.Portal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.configuration.xo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.ProxyUtil;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.common.xml.BusXmlHelper;
import com.antelope.ci.bus.common.xml.BusXmlHelper.SetterGetterPair;
import com.antelope.ci.bus.common.xml.XmlElement;
import com.antelope.ci.bus.common.xml.XmlEntity;
import com.antelope.ci.bus.osgi.CommonBusActivator;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_LAYOUT;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_Point;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Action;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Base;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Extension;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Extensions;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Hit;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.HitGroup;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Layout;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Part;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Parts;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Place;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.PlacePart;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.PlacePartTree;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.PlaceParts;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.RenderFont;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-2-2		上午11:25:05 
 */
@XmlEntity(name="portal")
public class Portal implements Serializable {
	private Base base;
	private Layout layout;
	private Parts parts;
	private Extensions extensions;
	private Action action;
	
	public Portal() {
		super();
	}
	
	public String getName() {
		String name = "null";
		if (base != null && base.getName() != null)
			name = base.getName();
		return name;
	}
	
	@XmlElement(name="base")
	public Base getBase() {
		return base;
	}
	public void setBase(Base base) {
		this.base = base;
	}
	
	@XmlElement(name="layout")
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	@XmlElement(name="parts")
	public Parts getParts() {
		return parts;
	}
	public void setParts(Parts parts) {
		this.parts = parts;
	}
	
	@XmlElement(name="extensions")
	public Extensions getExtensions() {
		return extensions;
	}
	public void setExtensions(Extensions extensions) {
		this.extensions = extensions;
	}
	
	@XmlElement(name="action")
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	private void attachBase(Base base_ext) {
		switch (base_ext.getEmbed()) {
			case REPLACE:
				base = base_ext;
				break;
			case APPEND:
				if (base == null) base = new Base();
				SetterGetterPair[] pairs = BusXmlHelper.FetchPairOfXml(Base.class);
				for (SetterGetterPair pair : pairs) {
					try {
						Object arg = ProxyUtil.invokeRet(pair.getGetter(), base_ext);
						if (arg != null) ProxyUtil.invoke(base, pair.getSetter(), new Object[]{arg});
					} catch (CIBusException e) {
						DevAssistant.errorln(e);
					}
				}
				break;
		}
	}
	
	private void attachLayout(List<PlaceParts> ppList_ext) {
		for (PlaceParts pp_ext : ppList_ext) {
			String pp_ename = pp_ext.getName();
			Place pp = null;
			Place p = null;
			List<Place> pList = null;
			if (pp_ename.contains(".")) {
				String[] pps = pp_ename.split(".");
				pp = layout.getPlace(pps[0]);
				if (pp != null) {
					p = pp.getParts().getPlaceMap().get(pps[1]);
					if (pp.getParts() != null) 
						pList = pp.getParts().getPlaceList();
				}
			} else {
				p = layout.getPlace(pp_ename);
				pList = layout.getPlaceList();
			}
			switch (pp_ext.getEmbed()) {
				case REPLACE:
					if (p != null) p.setParts(pp_ext);
					break;
				case APPEND:
					if (p != null) {
						p.getParts().addPartList(pp_ext.getPartList());
					} else {
						p = new Place();
						p.setParts(pp_ext);
						if (pList == null) pList = new ArrayList<Place>();
						pList.add(p);
					}
					break;
			}
		}
	}
	
	public void addPart(Part part) throws CIBusException {
		if (parts == null)
			parts = new Parts();
		if (parts.getPartList() == null)
			parts.setPartList(new ArrayList<Part>());
		for (Part p : parts.getPartList()) {
			if (p.getName().trim().equalsIgnoreCase(part.getName().trim()))
				throw new CIBusException("part has exist");
		}
		parts.getPartList().add(part);
	}
	
	public Map<String, List<PlacePart>> getPlaceList() {
		Map<String, List<PlacePart>> placeList = new HashMap<String, List<PlacePart>>();
		if (layout != null) {
			if (layout.getPlaceList() != null) {
				for (Place place : layout.getPlaceList()) {
					String pName = place.getName();
					if (place.getParts() != null) {
						PlaceParts pps = place.getParts();
						if (pps.getPartList() != null && !pps.getPartList().isEmpty()) {
							placeList.put(pName, pps.getPartList());
						}
						if (pps.getPlaceList() !=null && !pps.getPlaceList().isEmpty()) {
							for (Place pplace : pps.getPlaceList()) {
								String ppName = pplace.getName();
								if (pplace.getParts() != null) {
									if (pplace.getParts().getPartList() != null && !pplace.getParts().getPartList().isEmpty())
										placeList.put(pName+"."+ppName, pplace.getParts().getPartList());
								}
							}
						}
					}
				}
			}
		}
		
		return placeList;
	}
	
	public Map<String, PlacePartTree> makePlacePartTreeMap() {
		Map<String, PlacePartTree> treemap = new HashMap<String, PlacePartTree>();
		List<PlacePartTree> tree = makePlacePartTree();
		for (PlacePartTree node : tree)
			treemap.put(node.getName(), node);
		return treemap;
	}
	
	public List<PlacePartTree> makePlacePartTree() {
		List<PlacePartTree> tree = new ArrayList<PlacePartTree>();
		if (layout != null) {
			if (layout.getPlaceList() != null) {
				for (Place place : layout.getPlaceList()) {
					PlacePartTree root = new PlacePartTree();
					makePlacePartTree(root, place);
					tree.add(root);
				}
			}
		}
		
		return tree;
	}
		
	private void makePlacePartTree(PlacePartTree root, Place place) {
		root.setName(place.getName());
		if (place.getParts() != null) {
			PlaceParts pps = place.getParts();
			root.setRootList(pps.getPartList());
			if (pps.getPlaceList() != null) {
				for (Place childPlace : pps.getPlaceList()) {
					PlacePartTree child = new PlacePartTree();
					root.addChild(child);
					makePlacePartTree(child, childPlace);
				}
			}
		}
		
	}
	
	public Map<String, Map<String, PlacePart>> getPlaceMap() {
		Map<String, Map<String, PlacePart>> placeMap = new HashMap<String, Map<String, PlacePart>>();
		Map<String, List<PlacePart>> placeList = getPlaceList();
		for (String key : placeList.keySet()) {
			Map<String, PlacePart> partMap = new HashMap<String, PlacePart>();
			for (PlacePart part : placeList.get(key)) {
				partMap.put(part.getPlace(), part);
			}
			placeMap.put(key, partMap);
		}
		
		return placeMap;
	}
	
	public Map<String, PlacePart> getPlacePartMap() {
		Map<String, PlacePart> partMap = new HashMap<String, PlacePart>();
		Map<String, List<PlacePart>> placeMap = getPlaceList();
		for (String key : placeMap.keySet()) {
			for (PlacePart part : placeMap.get(key)) {
				partMap.put(key+"."+part.getPlace(), part);
			}
		}
		
		return partMap;
	}
	
	public Map<String, PlacePart> getPalcePartRenderMap() {
		Map<String, PlacePart> renderMap = new HashMap<String, PlacePart>();
		Map<String, PlacePart> ppMap = getPlacePartMap();
		for (String ppName : ppMap.keySet()) {
			PlacePart pp = ppMap.get(ppName);
			if (pp.existRender())
				renderMap.put(ppName, pp);
		}
		
		return renderMap;
	}
	
	public Map<String, Part> getPartMap() {
		Map<String, Part> partMap = new HashMap<String, Part>();
		if (parts != null) {
			partMap = parts.getPartMap();
		}
		
		return partMap;
	}
	
	public Part getPart(String partName) {
		if (parts != null) {
			List<Part> partList = parts.getPartList();
			if (partList != null) {
				Part part = getPart(partList, partName);
				if (part != null)
					return part;
			}
		}
		return null;
	}
	
	private Part getPart(List<Part> partList, String partName) {
		for (Part part : partList) {
			if (part.getName().equalsIgnoreCase(partName))
				return part;
		}
		return null;
	}
	
	public PlacePart getPlacePart(String lplaceName, String lpartName) {
		Map<String, Map<String, PlacePart>> placeMap = getPlaceMap();
		if (placeMap.get(lplaceName) != null) {
			return placeMap.get(lplaceName).get(lpartName);
		}
		
		return null;
	}
	
	public Portal clonePortal() throws CIBusException {
		return (Portal) ProxyUtil.deepClone(this, CommonBusActivator.getClassLoader());
	}
	
	public Map<String, PlaceParts> makePlacePartsMap() {
		Map<String, PlaceParts> ppsMap = new HashMap<String, PlaceParts>();
		if (layout != null) {
			List<Place> placeList = layout.getPlaceList();
			if (placeList != null)
				for (Place place : placeList)
					putPlacePartsMap(ppsMap, place, "");
		}
		return ppsMap;
	}
	
	private void putPlacePartsMap(Map<String, PlaceParts> ppsMap, Place place, String parentKey) {
		String key;
		if (StringUtil.empty(parentKey))
			key = place.getName();
		else
			key = parentKey + "." + place.getName();
		PlaceParts pps = place.getParts();
		ppsMap.put(key,  pps);
		List<Place> placeList = pps.getPlaceList();
		if (placeList != null) {
			for (Place p : placeList) {
				putPlacePartsMap(ppsMap, p, key);
			}
		}
	}
	
	public PlacePart getPlacePart(String name) {
		if (layout != null) {
			List<Place> placeList = layout.getPlaceList();
			if (placeList != null) {
				for (Place place : placeList) {
					PlacePart part  = getPlacePart(place, name);
					if (part != null)
						return part;
				}
			}
		}
		
		return null;
	}
	
	public PlacePart getPlacePart(Place place, String name) {
		PlaceParts parts = place.getParts();
		if (parts != null) {
			List<PlacePart> partList = parts.getPartList();
			if (partList != null) {
				for (PlacePart part : partList) {
					if (part.getName().equalsIgnoreCase(name))
						return part;
				}
				 List<Place> placeList = parts.getPlaceList();
				 if (placeList != null) {
					 for (Place childP : placeList) {
						 if (childP != null) {
							 PlacePart pp = getPlacePart(childP, name);
							 if (pp != null)
								 return pp;
						 }
					 }
				 }
			}
		}
		
		return null;
	}
	
	public Part getExtPart(String name) {
		if (extensions != null) {
			List<Extension> extentionList = extensions.getExtentionList();
			if (extentionList != null) {
				for (Extension extension : extentionList) {
					if (extension != null) {
						try {
							if (extension.toPoint() == EU_Point.PARTS) {
								List<Part> partList = extension.getPartList();
								if (partList != null) {
									Part part = getPart(partList, name);
									if (part != null)
										return part;
								}
							}
						} catch (CIBusException e) {
							DevAssistant.errorln(e);
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public RenderFont getSwitchHitFont() {
		if (action != null)
			return action.getSwitchHitFont();
		
		return null;
	}
	
	public int getOrder() {
		int order = -1;
		if (base == null || base.getOrder() == 0) {
			if (extensions != null)
				order = extensions.getOrder();
		} else {
			order = base.getOrder();
		}
		
		return order;
	}
	
	public PlaceParts getNorthPlace() {
		return getPlace(EU_LAYOUT.NORTH.getName());
	}
	
	public PlaceParts getWestPlace() {
		return getPlace(EU_LAYOUT.WEST.getName());
	}
	
	public PlaceParts getCenterPlace() {
		return getPlace(EU_LAYOUT.CENTER.getName());
	}
	
	public PlaceParts getEastPlace() {
		return getPlace(EU_LAYOUT.EAST.getName());
	}
	
	public PlaceParts getSouthPlace() {
		return getPlace(EU_LAYOUT.SOUTH.getName());
	}
	
	private PlaceParts getPlace(String name) {
		if (layout != null) {
			if (layout.getPlaceList() != null) {
				for (Place place : layout.getPlaceList()) {
					if (place != null) {
						String pname = place.getName();
						if (!StringUtil.empty(pname)) {
							if (pname.trim().equalsIgnoreCase(name))
								return place.getParts();
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public void resetHit() {
		if (action != null)
			action.deweight();
		
	}
	
	public HitGroup getBlockHit() {
		if (action != null)
			return action.getBlockHit();
		return null;
	}
	
	public Hit getHit(String scope, String mode, String name) {
		if (action != null)
			return action.getHit(scope, mode, name);
		return null;
	}
}

