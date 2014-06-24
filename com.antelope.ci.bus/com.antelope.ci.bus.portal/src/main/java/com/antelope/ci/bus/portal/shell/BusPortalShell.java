// com.antelope.ci.bus.portal.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.portal.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.configuration.xo.EU_LAYOUT;
import com.antelope.ci.bus.portal.configuration.xo.EU_ORIGIN;
import com.antelope.ci.bus.portal.configuration.xo.Part;
import com.antelope.ci.bus.portal.configuration.xo.PlacePart;
import com.antelope.ci.bus.portal.configuration.xo.PlacePartTree;
import com.antelope.ci.bus.portal.configuration.xo.Portal;
import com.antelope.ci.bus.server.shell.BusBaseFrameShell;
import com.antelope.ci.bus.server.shell.BusShellStatus;
import com.antelope.ci.bus.server.shell.Shell;
import com.antelope.ci.bus.server.shell.ShellText;
import com.antelope.ci.bus.server.shell.buffer.ShellCursor;

/**
 * it is a template of portal shell.
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-29 下午9:15:32
 */
@Shell(name="base.frame", commandAdapter="com.antelope.ci.bus.portal.shell.command.PortalCommandAdapter")
public abstract class BusPortalShell extends BusBaseFrameShell {
	private static final Logger log = Logger.getLogger(BusPortalShell.class);
	
	protected static final EU_LAYOUT[] LAYOUT_ORDER		= new EU_LAYOUT[] 
			{EU_LAYOUT.NORTH, EU_LAYOUT.SOUTH, EU_LAYOUT.WEST, EU_LAYOUT.EAST, EU_LAYOUT.CENTER};
	protected final static Map<String, Integer> CONTENT_SCALE = new HashMap<String, Integer>();
	static {
		CONTENT_SCALE.put(EU_LAYOUT.WEST.getName(), 2);
		CONTENT_SCALE.put(EU_LAYOUT.CENTER.getName(), 6);
		CONTENT_SCALE.put(EU_LAYOUT.EAST.getName(), 2);
	}
	
	private Portal portal;
	private ShellPalette contentPalette;

	public BusPortalShell() throws CIBusException {
		super();
		contentPalette = null;
		Class thisCls = this.getClass();
		if (thisCls.isAnnotationPresent(Shell.class)) {
			Shell sa = (Shell) thisCls.getAnnotation(Shell.class);
			this.sort = BusShellStatus.code(sa.status());
		} 
		customInit();
		parsePortal();
		if (portal == null)
			throw new CIBusException("", "must set configration of portal");
	}
	
	@Override public void writeContent(Object content) throws IOException {
		clearContent();
		if (content instanceof ShellLineContentSet) {
			ShellLineContentSet contentSet = (ShellLineContentSet) content;
			this.writeLine(getContentCursor(), contentSet.getContentList());
		}
	}
	
	@Override public void clearContent() throws IOException {
		if (contentPalette != null) {
			int px = contentPalette.getX();
			int py = contentPalette.getY();
			int ph = contentPalette.getHeight();
			int pw = contentPalette.getWidth();
			int h = 0, w = 0;
			
			shiftTop();
			move(px, py);
			ShellCursor cursor = new ShellCursor(px, py);
			String[] lines = new String[ph];
			String line = "";
			for (; w < pw; w++)
				line += " ";
			while (h < ph)
				lines[h++] = line;
			write(cursor, lines);
			shiftTop();
			move(px, py);
		}
	}
	
	protected ShellCursor getContentCursor() {
		return new ShellCursor(contentPalette.getX(), contentPalette.getY());
	}
	
	protected void parsePortal() throws CIBusException {
		portal = null;
		Class thisCls = this.getClass();
		BusPortalConfigurationHelper configHelper = BusPortalConfigurationHelper.getHelper();
		if (thisCls.isAnnotationPresent(PortalConfiguration.class)) {
			PortalConfiguration pc = (PortalConfiguration) thisCls.getAnnotation(PortalConfiguration.class);
			configHelper.addConfigPair(thisCls.getName(), pc.properties(), pc.xml());
		} 
		portal = configHelper.parse(this.getClass().getName());
	}
	
	@Override
	public void setSort(int sort) {
		// nothing
	}
	
	private void draw() throws IOException, CIBusException {
		Map<String, PlacePartTree> placeTreeMap = portal.makePlacePartTreeMap();
		Map<String, Integer> contentWidthMap = divideContentWidth(getWidth(), placeTreeMap);
		shiftTop();
		
		PlacePartTree northTree = placeTreeMap.get(EU_LAYOUT.NORTH.getName());
		LayoutPaletteSet northPaletteSet = new LayoutPaletteSet();
		int x = 0;
		int y = 0;
		if (northTree != null) {
			int north_height = calculateTreeRootHeight(northTree, getWidth());
			northPaletteSet = drawRootTree(northTree, x, y, getWidth(), north_height);
		}
		
		PlacePartTree southTree = placeTreeMap.get(EU_LAYOUT.SOUTH.getName());
		LayoutPaletteSet southPaletteSet = new LayoutPaletteSet();
		if (southTree != null) {
			int south_height = calculateTreeRootHeight(southTree, getWidth());
			x = 0;
			y = getHeight() - south_height;
			southPaletteSet = drawRootTree(southTree, x, y, getWidth(), south_height);
		}
		
		int content_height = getHeight() - northPaletteSet.getHeight() - southPaletteSet.getHeight();
		PlacePartTree westTree = placeTreeMap.get(EU_LAYOUT.WEST.getName());
		LayoutPaletteSet westPaletteSet = new LayoutPaletteSet();
		if (westTree != null) {
			Integer west_width = contentWidthMap.get(EU_LAYOUT.WEST.getName());
			x = 0;
			y = northPaletteSet.getHeight();
			if (west_width != null)
				westPaletteSet = drawRootTree(westTree, x, y, west_width, content_height);
		}
		
		PlacePartTree eastTree = placeTreeMap.get(EU_LAYOUT.EAST.getName());
		LayoutPaletteSet eastPaletteSet = new LayoutPaletteSet();
		if (eastTree != null) {
			Integer east_width = contentWidthMap.get(EU_LAYOUT.EAST.getName());
			x = getWidth()-westPaletteSet.getWidth();
			y = northPaletteSet.getHeight();
			if (east_width != null)
				eastPaletteSet = drawRootTree(eastTree, x, y, east_width, content_height);
		}
		
		PlacePartTree centerTree = placeTreeMap.get(EU_LAYOUT.CENTER.getName());
		LayoutPaletteSet centerPaletteSet = new LayoutPaletteSet();
		if (centerTree != null) {
			Integer center_width = contentWidthMap.get(EU_LAYOUT.CENTER.getName());
			x = westPaletteSet.getWidth(); 
			y = northPaletteSet.getHeight();
			if (center_width != null)
				centerPaletteSet = drawRootTree(centerTree, x, y, center_width, content_height);
		}
	}
	
	protected Map<String, Integer> divideTreeWidth(int total_width, PlacePartTree partTree) {
		List<String> keyList = new ArrayList<String>();
		List<String> placeList = partTree.genPlaceGroup();
		for (String place : placeList) {
			try {
				EU_LAYOUT l = EU_LAYOUT.toLayout(place);
				switch (l) {
					case WEST:
					case CENTER:
					case EAST:
						keyList.add(place);
						break;
					default:
						break;
				}
			} catch (CIBusException e) {
				
			}
		}
		return divideWidth(total_width, keyList.toArray(new String[keyList.size()]), CONTENT_SCALE);
	}
	
	protected Map<String, Integer> divideContentWidth(int total_width, Map<String, PlacePartTree> placeTreeMap) {
		List<String> keyList = new ArrayList<String>();
		addContentKey(keyList, placeTreeMap, EU_LAYOUT.WEST);
		addContentKey(keyList, placeTreeMap, EU_LAYOUT.CENTER);
		addContentKey(keyList, placeTreeMap, EU_LAYOUT.EAST);
		return divideWidth(total_width, keyList.toArray(new String[keyList.size()]), CONTENT_SCALE);
	}
	
	protected void addContentKey(List<String> keyList, Map<String, PlacePartTree> placeTreeMap, EU_LAYOUT layout) {
		PlacePartTree pptree = placeTreeMap.get(layout.getName());
		Map<String, Part> partMap = portal.getPartMap();
		if (pptree != null && !pptree.isEmpty(partMap))
			keyList.add(layout.getName());
	}
	
	protected Map<String, Integer> divideWidth(int total_width, String[] keys, Map<String, Integer> scale) {
		Map<String, Integer> widthMap = new HashMap<String, Integer>();
		int total = 0;
		for (String key : keys)
			total += scale.get(key);
		for (String key : keys)
			widthMap.put(key, total_width * scale.get(key) / total);
		return widthMap;
	}
	
	protected LayoutPaletteSet drawRootTree(PlacePartTree partTree, int x, int y, int width, int height) {
		Map<String, Integer> widthMap = divideTreeWidth(width, partTree);
		ShellPalette north_palette = new ShellPalette();
		ShellPalette south_palette = new ShellPalette();
		ShellPalette west_palette = new ShellPalette();
		ShellPalette east_palette = new ShellPalette();
		ShellPalette center_palette = new ShellPalette();
		ShellCursor cursor = new ShellCursor(x, y);
		ShellCursor content_cursor = null;
		for (EU_LAYOUT layout : LAYOUT_ORDER) {
			try {
				int tree_height = getPartTreeHeight(partTree, layout, width);
				if (tree_height == 0) continue;
				
				switch (layout) {
					case NORTH:
						north_palette = drawPartTree(partTree, layout, cursor, width, tree_height);
						content_cursor = cursor.clone();
						break;
					case SOUTH:
						if (height - tree_height > 0)
							cursor.setY(height-tree_height);
						south_palette = drawPartTree(partTree, layout, cursor, width, tree_height);
						cursor.setX(north_palette.getWidth());
						break;
					case WEST:
						Integer west_width = widthMap.get(EU_LAYOUT.WEST.getName());
						if (west_width != null) {
							if (content_cursor == null)
								content_cursor = cursor.clone();
							west_palette = drawPartTree(partTree, layout, cursor, west_width, height-north_palette.getHeight());
							content_cursor.setX(west_width);
						}
						break;
					case EAST:
						Integer east_width = widthMap.get(EU_LAYOUT.EAST.getName());
						if (east_width != null) {
							if (content_cursor == null)
								content_cursor = cursor.clone();
							else
								cursor = content_cursor.clone();
							cursor.setX(width-east_width);
							east_palette = drawPartTree(partTree, layout, cursor, east_width, height-north_palette.getHeight());
						}
						break;
					case CENTER:
						Integer center_width = widthMap.get(EU_LAYOUT.CENTER.getName());
						if (center_width != null) {
							if (content_cursor == null)
								content_cursor = cursor.clone();
							else
								cursor = content_cursor.clone();
							center_palette = drawPartTree(partTree, layout, cursor, center_width, height-north_palette.getHeight());
						}
						break;
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
				log.error(e);
			}
		}
		
		return new LayoutPaletteSet(north_palette, south_palette, west_palette, east_palette, center_palette);
	}
	
	protected ShellPalette drawPartTree(PlacePartTree partTree, EU_LAYOUT layout, ShellCursor cursor, int width, int height) {
		Map<String, PlacePart> rootMap = partTree.makeRootMap();
		Map<String, Map<String, PlacePart>> childMap = partTree.makeChildPartMap();
		PlacePart rootPart = rootMap.get(layout.getName());
		ShellPalette palette = new ShellPalette();
		palette.setStartPoint(cursor.getX(), cursor.getY());
		if (rootPart != null) {
			palette = drawPart(rootPart, width, cursor);
		} else {
			Map<String, PlacePart> childPartMap = childMap.get(layout.getName());
			if (childPartMap != null) {
				drawInner(childPartMap, cursor, width, height);
				int part_width = getPartWdith(childPartMap, width);
				part_width = part_width < width ? part_width : width;
				palette.setShapePoint(part_width, getPartHeight(childPartMap, width));
			}
		}
		
		return palette;
	}
	
	protected void drawInner(Map<String, PlacePart> placeMap, ShellCursor cursor, int width, int height) {
		ShellCursor content_cursor = null;
		storeCursor();
		PlacePart northPart = placeMap.get(EU_LAYOUT.NORTH.getName());
		int north_height = 0;
		if (northPart != null) {
			try {
				List<List<String>> contentList = placePartContent(northPart, width);
				if (!contentList.isEmpty()) {
					moveCursor(cursor);
					north_height = contentList.size();
					writeLine(cursor, contentList);
					content_cursor = cursor.clone();
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		storeCursor();
		int south_height = 0;
		PlacePart southPart = placeMap.get(EU_LAYOUT.SOUTH.getName());
		if (southPart != null) {
			try {
				List<List<String>> contentList = placePartContent(southPart, width);
				if (!contentList.isEmpty()) {
					if (content_cursor == null)
						content_cursor = cursor.clone();
					south_height = contentList.size();
					cursor.addY(height - north_height - south_height);
					moveCursor(cursor);
					writeLine(cursor, contentList);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
				e.printStackTrace();
			}
		}

		storeCursor();
		int west_width = 0;
		PlacePart westPart = placeMap.get(EU_LAYOUT.WEST.getName());
		if (westPart != null) {
			try {
				List<List<String>> contentList = placePartContent(westPart, width);
				if (!contentList.isEmpty()) {
					if (content_cursor == null)
						content_cursor = cursor.clone();
					else
						cursor = content_cursor.clone();
					moveCursor(cursor);
					west_width = PortalShellUtil.maxLine(contentList);
					writeLine(cursor, contentList);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		storeCursor();
		int east_width = 0;
		PlacePart eastPart = placeMap.get(EU_LAYOUT.EAST.getName());
		if (eastPart != null) {
			try {
				List<List<String>> contentList = placePartContent(eastPart, width);
				if (!contentList.isEmpty()) {
					if (content_cursor == null)
						content_cursor = cursor.clone();
					else
						cursor = content_cursor.clone();
					moveCursor(cursor);
					shiftRight(west_width);
					east_width = PortalShellUtil.maxLine(contentList);
					writeLine(cursor, contentList);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		storeCursor();
		PlacePart centerPart = placeMap.get(EU_LAYOUT.CENTER.getName());
		if (centerPart != null) {
			try {
				int center_width = width - west_width - east_width;
				List<List<String>> contentList = placePartContent(centerPart, center_width);
				if (!contentList.isEmpty()) {
					if (content_cursor == null)
						content_cursor = cursor.clone();
					else
						cursor = content_cursor.clone();
					moveCursor(cursor);
					writeLine(cursor, contentList);
					putContentPalette(centerPart, content_cursor, center_width, contentList.size());
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	private void putContentPalette(PlacePart part, ShellCursor cursor, int width, int height) {
		if ("content".equals(part.getName())) {
			contentPalette = new ShellPalette();
			contentPalette.setStartPoint(cursor.getX(), cursor.getY());
			contentPalette.setShapePoint(width, height);
		}
	}
	
	private void moveCursor(ShellCursor cursor) throws IOException {
		if (cursor.getX() != 0)
			shiftRight(cursor.getX());
		if (cursor.getY() != 0)
			shiftDown(cursor.getY());
	}
	
	protected ShellPalette drawPart(PlacePart part, int width, ShellCursor cursor) {
		ShellPalette palette = new ShellPalette();
		storeCursor();
		try {
			moveCursor(cursor);
			List<List<String>> contentList = placePartContent(part, width);
			if (!contentList.isEmpty()) {
				int palette_width = PortalShellUtil.maxLine(contentList);
				palette_width = palette_width < width ? palette_width : width;
				int palette_height = contentList.size();
				palette.setShapePoint(palette_width, palette_height);
				writeLine(cursor, contentList);
			}
		} catch (Exception e) {
			DevAssistant.errorln(e);
		}
		return palette;
	}
	
	protected int calculateTreeRootHeight(PlacePartTree partTree, int width) {
		int height = 0;
		try {
			height = calculateTreeHeight(partTree, width, height);
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
		}
		return height;
	}
	
	protected int calculateTreeHeight(PlacePartTree partTree, int width, int height) throws CIBusException {
		Map<String, PlacePart> rootMap = partTree.makeRootMap();
		Map<String, PlacePartTree> childMap = partTree.makeChildMap();
		if (!rootMap.isEmpty())
			height += getPartHeight(rootMap, width);
		if (childMap.isEmpty()) {
			int north_height = 0;
			int south_height = 0;
			int west_height = 0;
			int east_height = 0;
			int center_height = 0;
			int content_height = 0;
			Map<String, Integer> widthMap = divideContentWidth(width, childMap);
			
			PlacePartTree northTree = childMap.get(EU_LAYOUT.NORTH.getName());
			if (northTree != null)
				calculateTreeHeight(northTree, width, north_height);
			
			PlacePartTree southTree = childMap.get(EU_LAYOUT.SOUTH.getName());
			if (southTree != null)
				calculateTreeHeight(southTree, width, south_height);
			
			PlacePartTree westTree = childMap.get(EU_LAYOUT.WEST.getName());
			if (westTree != null)
				calculateTreeHeight(westTree, widthMap.get(EU_LAYOUT.WEST.getName()), west_height);
			
			PlacePartTree eastTree = childMap.get(EU_LAYOUT.EAST.getName());
			if (eastTree != null)
				calculateTreeHeight(eastTree, widthMap.get(EU_LAYOUT.EAST.getName()), east_height);
			
			PlacePartTree centerTree = childMap.get(EU_LAYOUT.CENTER.getName());
			if (centerTree != null)
				calculateTreeHeight(centerTree, widthMap.get(EU_LAYOUT.CENTER.getName()), center_height);
			
			try {
				content_height = BusPortalShellUtil.max(west_height, east_height, center_height);
			} catch (CIBusException e) {
				DevAssistant.assert_exception(e);
			}
			
			height += north_height + south_height + content_height;
		}
		
		return height;
	}
	
	protected int getPartTreeHeight(PlacePartTree partTree, EU_LAYOUT layout, int width) throws CIBusException {
		Map<String, PlacePart> rootMap = partTree.makeRootMap();
		Map<String, Map<String, PlacePart>> childMap = partTree.makeChildPartMap();
		PlacePart rootPart = rootMap.get(layout.getName());
		int height = 0;
		if (rootPart != null) {
			height = getContentHeight(rootPart, width);
		} else {
			Map<String, PlacePart> childPartMap = childMap.get(layout.getName());
			if (childPartMap != null)
				height = getPartHeight(childPartMap, width);
		}
		
		return height;
	}
	
	protected void layout() throws IOException, CIBusException {
		Map<String, Map<String, PlacePart>> placeMap = portal.getPlaceMap();
		shiftTop();
		int north_height = 0;
		Map<String, PlacePart> northMap = placeMap.get(EU_LAYOUT.NORTH.getName());
		if (northMap != null) {
			north_height = getPartHeight(northMap, getWidth());
			layoutInner(northMap, getWidth());
		}
		
		Map<String, PlacePart> southMap = placeMap.get(EU_LAYOUT.SOUTH.getName());
		if (southMap != null) {
			shiftBottom();
			layoutInner(southMap, getWidth());
		}

		shiftTop();
		int west_width = 0;
		Map<String, PlacePart> westMap = placeMap.get(EU_LAYOUT.WEST.getName());
		if (westMap != null) {
			shiftDown(north_height);
			west_width = getPartWdith(westMap, getWidth());
			layoutInner(westMap, west_width);
		}
		
		shiftTop();
		int east_width = 0;
		Map<String, PlacePart> eastMap = placeMap.get(EU_LAYOUT.EAST.getName());
		if (eastMap != null) {
			shiftDown(north_height);
			east_width = getPartWdith(eastMap, getWidth());
			shiftRight(getWidth() - east_width);
			layoutInner(eastMap, east_width);
		}
		
		shiftTop();
		Map<String, PlacePart> centerMap = placeMap.get(EU_LAYOUT.CENTER.getName());
		if (centerMap != null) {
			shiftDown(north_height);
			shiftRight(west_width);
			int center_width = getWidth() - west_width - east_width;
			if (center_width < 0)
				throw new CIBusException("", "not enough width for center");
			layoutInner(centerMap, center_width);
		}
	}
	
	protected int getPartHeight(Map<String, PlacePart> placeMap, int width) {
		int north_height = 0;
		int south_height = 0;
		int west_height = 0;
		int east_height = 0;
		int center_height = 0;
		int west_width = 0;
		int east_width = 0;
		
		try {
			north_height = getContentHeight(placeMap, EU_LAYOUT.NORTH, width);
		} catch (CIBusException e) {}
		try {
			south_height = getContentHeight(placeMap, EU_LAYOUT.SOUTH, width);
		} catch (CIBusException e) {}
		try {
			west_width = getContentWidth(placeMap, EU_LAYOUT.WEST, width);
			west_height = getContentHeight(placeMap, EU_LAYOUT.WEST, width);
		} catch (CIBusException e) {}
		try {
			east_width = getContentWidth(placeMap, EU_LAYOUT.EAST, width);
			east_height = getContentHeight(placeMap, EU_LAYOUT.EAST, width-west_width);
		} catch (CIBusException e) {}
		
		try {
			center_height = getContentHeight(placeMap, EU_LAYOUT.CENTER, width-west_width-east_width);
		} catch (CIBusException e) {}
		
		int content_height = 0;
		try {
			content_height = BusPortalShellUtil.max(west_height, east_height, center_height);
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
		}
		return north_height + south_height + content_height;
	}
	
	private int getContentHeight(Map<String, PlacePart> placeMap, EU_LAYOUT layout, int width) throws CIBusException {
		PlacePart pp = placeMap.get(layout.getName());
		if (pp != null) {
			return getContentHeight(pp, width);
		}
		
		return 0;
	}
	
	private int getContentHeight(PlacePart pp, int width) throws CIBusException {
		try {
			List<List<String>> contentList = placePartContent(pp, width);
			return contentList.size();
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	protected int getPartWdith(Map<String, PlacePart> placeMap, int width) {
		int part_width = 0;
		int pc_width = 0;
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.NORTH, width);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.SOUTH, width);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.WEST, width);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.EAST, width);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.CENTER, width);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		
		return part_width;
	}
	
	@Deprecated protected int getPartWdith(Map<String, PlacePart> placeMap) {
		int part_width = 0;
		int pc_width = 0;
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.NORTH);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.SOUTH);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.WEST);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.EAST);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, EU_LAYOUT.CENTER);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		
		return part_width;
	}
	
	private int getContentWidth(Map<String, PlacePart> placeMap, EU_LAYOUT layout, int width) throws CIBusException {
		PlacePart pp = placeMap.get(layout.getName());
		if (pp != null) {
			return getContentWidth(pp, width);
		}
		
		return 0;
	}
	
	protected int getContentWidth(PlacePart pp, int width) throws CIBusException {
		try {
			List<List<String>> contentList = placePartContent(pp, width);
			return PortalShellUtil.maxLine(contentList);
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	@Deprecated private int getContentWidth(Map<String, PlacePart> placeMap, EU_LAYOUT layout) throws CIBusException {
		PlacePart pp = placeMap.get(layout.getName());
		if (pp != null) {
			return getContentWidth(pp);
		}
		
		return 0;
	}
	
	@Deprecated private int getContentWidth(PlacePart pp) throws CIBusException {
		try {
			String pcon = placePartContent(pp);
			if (pcon == null) return 0;
			return StringUtil.maxLine(pcon);
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}

	protected void layoutInner(Map<String, PlacePart> placeMap, int width) {
		storeCursor();
		ShellCursor cursor = new ShellCursor();
		PlacePart northPart = placeMap.get(EU_LAYOUT.NORTH.getName());
		if (northPart != null) {
			try {
				List<List<String>> contentList = placePartContent(northPart, width);
				writeLine(cursor, contentList);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		storeCursor();
		PlacePart southPart = placeMap.get(EU_LAYOUT.SOUTH.getName());
		if (southPart != null) {
			try {
				List<List<String>> contentList = placePartContent(southPart, width);
				writeLine(cursor, contentList);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		storeCursor();
		int west_width = 0;
		PlacePart westPart = placeMap.get(EU_LAYOUT.WEST.getName());
		if (westPart != null) {
			try {
				List<List<String>> contentList = placePartContent(westPart, width);
				writeLine(cursor, contentList);
				west_width = PortalShellUtil.maxLine(contentList);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		storeCursor();
		int east_width = 0;
		PlacePart eastPart = placeMap.get(EU_LAYOUT.EAST.getName());
		if (eastPart != null) {
			try {
				List<List<String>> contentList = placePartContent(eastPart, width);
				east_width = PortalShellUtil.maxLine(contentList);
				writeLine(cursor, contentList);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		storeCursor();
		PlacePart centerPart = placeMap.get(EU_LAYOUT.CENTER.getName());
		if (centerPart != null) {
			try {
				int center_width = width - west_width - east_width;
				List<List<String>> contentList = placePartContent(centerPart, center_width);
				writeLine(cursor, contentList);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		try {
			if (cursor.getY() > 0) shiftUp(cursor.getY() - 1);
			if (cursor.getX() > 0) shiftLeft(cursor.getX());
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void writeLine(ShellCursor cursor, List<List<String>> valueList) {
		int default_x = cursor.getX();
		boolean first = true;
		for (List<String> value : valueList) {
			try {
				if (first)
					first = false;
				else
					shiftDown(1);
				int count = 0;
				for (String c : value) {
					if (ShellText.isShellText(c)) {
						ShellText text = writeFormat(cursor, c);
						if (text != null)
							count += StringUtil.getWordCount(text.getText());
					} else {
						write(cursor, c);
						count = StringUtil.getWordCount(c);
					}
				}
				if (count != 0)
					shiftLeft(count);
				cursor.setX(default_x);
				cursor.addY(1);
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	@Deprecated protected void writeLine(ShellCursor cursor, Object[] lines) {
		int default_x = cursor.getX();
		boolean first = true;
		for (Object line : lines) {
			try {
				if (first)
					first = false;
				else
					shiftDown(1);
				if (line instanceof String) {
					String v = (String) line;
					if (ShellText.isShellText(v)) {
						ShellText text = writeFormat(cursor, v);
						if (text != null)
							shiftLeft(StringUtil.getWordCount(text.getText()));
					} else {
						write(cursor, v);
						shiftLeft(StringUtil.getWordCount(v));
					}
				}
				
				if (line instanceof ShellText) {
					ShellText text = (ShellText) line;
					write(cursor, text);
					if (text != null)
						shiftLeft(StringUtil.getWordCount(text.getText()));
				}
				
				cursor.setX(default_x);
				cursor.addY(1);
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected void write(ShellCursor cursor, String[] lines) {
		int default_x = cursor.getX();
		boolean first = true;
		int lastsize = 0;
		for (String line : lines) {
			try {
				if (first) {
					first = false;
				} else {
					cursor.setX(default_x);
					cursor.addY(1);
					if (lastsize != 0)
						shiftLeft(lastsize);
					shiftDown(1);
				}
				lastsize = StringUtil.getWordCount(line);
				write(cursor, line);
			} catch (IOException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected void write(ShellCursor cursor, String value) throws IOException {
		print(value);
		cursor.addX(StringUtil.getWordCount(value));
	}
	
	protected void write(ShellCursor cursor, ShellText text) throws IOException {
		print(text);
		cursor.addX(StringUtil.getWordCount(text.getText()));
	}
	
	protected ShellText writeFormat(ShellCursor cursor, String value) throws IOException {
		ShellText text = ShellText.toShellText(value);
		if (text != null)
			write(cursor, text);
		return text;
	}
	
	protected List<List<String>> placePartContent(PlacePart placePart, int width)  {
		Part part = portal.getPartMap().get(placePart.getName());
		return part.reListContent(width);
	}

	@Deprecated protected String placePartContent(PlacePart placePart) throws CIBusException {
		EU_ORIGIN origin = EU_ORIGIN.toOrigin(placePart.getOrigin());
		Part part;
		switch (origin) {
			case GLOBAL:
				part = portal.getPartMap().get(placePart.getName());
				if (part != null)
					return part.getContent().getValue();
				break;
			case PART:
				part = portal.getPartMap().get(placePart.getName());
				if (part != null)
					return part.getContent().getValue();
				break;
		}

		return null;
	}

	@Override protected void view() throws CIBusException {
		try {
			draw();
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}

	@Override protected ShellCursor initCursorPosistion() {
		return new ShellCursor(0, 0);
	}
	
	protected abstract void customInit() throws CIBusException;
}
