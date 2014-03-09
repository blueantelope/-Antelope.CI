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
import com.antelope.ci.bus.server.shell.Shell;
import com.antelope.ci.bus.server.shell.buffer.ShellCursor;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-29 下午9:15:32
 */
public abstract class BusPortalShell extends BusBaseFrameShell {
	private static final Logger log = Logger.getLogger(BusPortalShell.class);
	protected Portal portal_config;
	private static final String CP_SUFFIX 						= "classpath:";
	private static final String FILE_SUFFIX 						= "file:";
	protected static final EU_LAYOUT[] LAYOUT_ORDER		= new EU_LAYOUT[] 
			{EU_LAYOUT.NORTH, EU_LAYOUT.SOUTH, EU_LAYOUT.WEST, EU_LAYOUT.EAST, EU_LAYOUT.CENTER};
	protected final static Map<String, Integer> CONTENT_SCALE = new HashMap<String, Integer>();
	static {
		CONTENT_SCALE.put(EU_LAYOUT.WEST.getName(), 2);
		CONTENT_SCALE.put(EU_LAYOUT.CENTER.getName(), 6);
		CONTENT_SCALE.put(EU_LAYOUT.EAST.getName(), 2);
	}

	public BusPortalShell() throws CIBusException {
		super();
		customInit();
		initPortal_Config();
		if (portal_config == null)
			throw new CIBusException("", "must set configration of portal");
	}
	
	protected void initPortal_Config() throws CIBusException {
		portal_config = null;
		BusPortalConfigurationHelper configHelper = BusPortalConfigurationHelper.getHelper();
		configHelper.init();
		Class thisCls = this.getClass();
		if (thisCls.isAnnotationPresent(PortalConfiguration.class)) {
			PortalConfiguration pc = (PortalConfiguration) thisCls.getAnnotation(PortalConfiguration.class);
			boolean isResource = pc.properties().startsWith(CP_SUFFIX) ? true : false;
			String a_xml = trunckConfig(pc.xml() + ".xml");
			String a_pro = trunckConfig(pc.properties());
			portal_config = configHelper.parseExtention(thisCls.getResourceAsStream(a_xml), a_pro, isResource, thisCls.getPackage().getName());
		} else {
			if (thisCls.isAnnotationPresent(Shell.class)) {
				Shell a_shell = (Shell) thisCls.getAnnotation(Shell.class);
				portal_config = configHelper.getPortalExtention(a_shell.name());
			} else {
				portal_config = configHelper.getPortal();			// default configuration
			}
		}
		if (portal_config == null)
			portal_config = configHelper.getPortal();
	}
	
	protected String trunckConfig(String config) {
		String new_config = config;
		if (config.startsWith(CP_SUFFIX))
			new_config = config.substring(CP_SUFFIX.length());
		
		if (new_config.startsWith(FILE_SUFFIX))
			new_config = config.substring(FILE_SUFFIX.length());
		
		return new_config;
	}
	
	protected void draw() throws IOException, CIBusException {
		Map<String, PlacePartTree> placeTreeMap = portal_config.makePlacePartTreeMap();
		Map<String, Integer> contentWidthMap = divideContentWidth(getWidth(), placeTreeMap);
		shiftTop();
		
		PlacePartTree northTree = placeTreeMap.get(EU_LAYOUT.NORTH.getName());
		LayoutPaletteSet northPaletteSet = new LayoutPaletteSet();
		if (northTree != null) {
			int north_height = getPartTreeHeight(northTree, EU_LAYOUT.NORTH, getWidth());
			northPaletteSet = drawRootTree(northTree, getWidth(), north_height);
		}
		
		PlacePartTree southTree = placeTreeMap.get(EU_LAYOUT.SOUTH.getName());
		LayoutPaletteSet southPaletteSet = new LayoutPaletteSet();
		if (southTree != null) {
			shiftBottom();
			if (northPaletteSet.getHeight() > 1)
				shiftUp(northPaletteSet.getHeight()-1);
			int south_height = getPartTreeHeight(southTree, EU_LAYOUT.SOUTH, getWidth());
			southPaletteSet = drawRootTree(southTree, getWidth(), south_height);
		}
		
		int content_height = getHeight() - northPaletteSet.getHeight() - southPaletteSet.getHeight();
		PlacePartTree westTree = placeTreeMap.get(EU_LAYOUT.WEST.getName());
		LayoutPaletteSet westPaletteSet = new LayoutPaletteSet();
		if (westTree != null) {
			shiftTop();
			shiftDown(northPaletteSet.getHeight());
			Integer west_width = contentWidthMap.get(EU_LAYOUT.WEST.getName());
			if (west_width != null)
				westPaletteSet = drawRootTree(westTree, west_width, content_height);
		}
		
		PlacePartTree eastTree = placeTreeMap.get(EU_LAYOUT.EAST.getName());
		LayoutPaletteSet eastPaletteSet = new LayoutPaletteSet();
		if (eastTree != null) {
			shiftTop();
			shiftDown(northPaletteSet.getHeight());
			shiftRight(getWidth()-westPaletteSet.getWidth());
			Integer east_width = contentWidthMap.get(EU_LAYOUT.EAST.getName());
			if (east_width != null)
				eastPaletteSet = drawRootTree(eastTree, east_width, content_height);
		}
		
		PlacePartTree centerTree = placeTreeMap.get(EU_LAYOUT.CENTER.getName());
		LayoutPaletteSet centerPaletteSet = new LayoutPaletteSet();
		if (centerTree != null) {
			shiftTop();
			shiftDown(northPaletteSet.getHeight());
			shiftRight(westPaletteSet.getWidth());
			Integer center_width = contentWidthMap.get(EU_LAYOUT.CENTER.getName());
			if (center_width != null)
				centerPaletteSet = drawRootTree(centerTree, center_width, content_height);
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
		Map<String, Part> partMap = portal_config.getPartMap();
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
	
	protected LayoutPaletteSet drawRootTree(PlacePartTree partTree, int width, int height) {
		Map<String, Integer> widthMap = divideTreeWidth(width, partTree);
		PartPalette north_palette = new PartPalette();
		PartPalette south_palette = new PartPalette();
		PartPalette west_palette = new PartPalette();
		PartPalette east_palette = new PartPalette();
		PartPalette center_palette = new PartPalette();
		PartCursor cursor = new PartCursor();
		for (EU_LAYOUT layout : LAYOUT_ORDER) {
			try {
				int tree_height = getPartTreeHeight(partTree, layout, width);
				switch (layout) {
					case NORTH:
						north_palette = drawPartTree(partTree, layout, cursor, width, tree_height);
						break;
					case SOUTH:
						cursor.setPart_y(height-tree_height);
						south_palette = drawPartTree(partTree, layout, cursor, width, tree_height);
						cursor.setPart_x(north_palette.getHeight());
						break;
					case WEST:
						Integer west_width = widthMap.get(EU_LAYOUT.WEST.getName());
						if (west_width != null)
							west_palette = drawPartTree(partTree, layout, cursor, west_width, height-north_palette.getHeight());
						break;
					case EAST:
						Integer east_width = widthMap.get(EU_LAYOUT.EAST.getName());
						if (east_width != null)
							east_palette = drawPartTree(partTree, layout, cursor, east_width, height-north_palette.getHeight());
						break;
					case CENTER:
						Integer center_width = widthMap.get(EU_LAYOUT.CENTER.getName());
						if (center_width != null)
							center_palette = drawPartTree(partTree, layout, cursor, center_width, height-north_palette.getHeight());
						break;
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
				log.error(e);
			}
		}
		
		return new LayoutPaletteSet(north_palette, south_palette, west_palette, east_palette, center_palette);
	}
	
	protected PartPalette drawPartTree(PlacePartTree partTree, EU_LAYOUT layout, PartCursor cursor, int width, int height) {
		Map<String, PlacePart> rootMap = partTree.getRootMap();
		Map<String, Map<String, PlacePart>> childMap = partTree.makeChildMap();
		PlacePart rootPart = rootMap.get(layout.getName());
		PartPalette palette = new PartPalette();
		if (rootPart != null) {
			palette = drawPart(rootPart, width, cursor);
		} else {
			Map<String, PlacePart> childPartMap = childMap.get(layout.getName());
			if (childPartMap != null) {
				drawInner(childPartMap, width, height);
				int part_width = getPartWdith(childPartMap);
				part_width = part_width < width ? part_width : width;
				palette.setShapePoint(part_width, getPartHeight(childPartMap, width));
			}
		}
		
		return palette;
	}
	
	protected void drawInner(Map<String, PlacePart> placeMap, int width, int height) {
		storeCursor();
		PartCursor part_cursor = new PartCursor();
		PlacePart northPart = placeMap.get(EU_LAYOUT.NORTH.getName());
		int north_height = 0;
		if (northPart != null) {
			try {
				String pcon = placePartContent(northPart);
				if (pcon != null) {
					String[] lines = StringUtil.toLines(pcon, width);
					north_height = lines.length;
					for (String line : lines) {
						writeLine(part_cursor, line);
					}
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		restoreCursor();
		storeCursor();
		int south_height = 0;
		PlacePart southPart = placeMap.get(EU_LAYOUT.SOUTH.getName());
		if (southPart != null) {
			try {
				String pcon = placePartContent(southPart);
				String[] lines = StringUtil.toLines(pcon, width);
				south_height = lines.length;
				shiftDown(height - north_height - south_height);
				for (String line : lines) {
					writeLine(part_cursor, line);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
				e.printStackTrace();
			}
		}

		restoreCursor();
		storeCursor();
		int west_width = 0;
		PlacePart westPart = placeMap.get(EU_LAYOUT.WEST.getName());
		if (westPart != null) {
			try {
				String pcon = placePartContent(westPart);
				String[] lines = StringUtil.toLines(pcon);
				for (String line : lines) {
					writeLine(part_cursor, line);
				}
				west_width = StringUtil.maxLine(pcon);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		restoreCursor();
		storeCursor();
		int east_width = 0;
		PlacePart eastPart = placeMap.get(EU_LAYOUT.EAST.getName());
		if (eastPart != null) {
			try {
				String pcon = placePartContent(eastPart);
				String[] lines = StringUtil.toLines(pcon);
				east_width = StringUtil.maxLine(pcon);
				for (String line : lines) {
					shiftRight(width - east_width);
					writeLine(part_cursor, line);
					part_cursor.setPart_x(width - line.length());
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		restoreCursor();
		storeCursor();
		PlacePart centerPart = placeMap.get(EU_LAYOUT.CENTER.getName());
		if (centerPart != null) {
			try {
				String pcon = placePartContent(centerPart);
				if (pcon != null) {
					int center_width = width - west_width - east_width;
					String[] lines = StringUtil.toLines(pcon, center_width);
					for (String line : lines) {
						shiftLeft(east_width);
						writeLine(part_cursor, line);
						part_cursor.setPart_x(east_width + part_cursor.getPart_x());
					}
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		try {
			if (part_cursor.getPart_y() > 0) shiftUp(part_cursor.getPart_y() - 1);
			if (part_cursor.getPart_x() > 0) shiftLeft(part_cursor.getPart_x());
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
		restoreCursor();
	}
	
	protected PartPalette drawPart(PlacePart part, int width, PartCursor cursor) {
		PartPalette palette = new PartPalette();
		storeCursor();
		try {
			if (cursor.getPart_x() > 0) shiftRight(x);
			if (cursor.getPart_y() > 0) shiftDown(y);
			String pcon = placePartContent(part);
			if (pcon != null) {
				String[] lines = StringUtil.toLines(pcon, width);
				int palette_width = StringUtil.maxLine(pcon);
				palette_width = palette_width < width ? palette_width : width;
				int palette_height = lines.length;
				palette.setShapePoint(palette_width, palette_height);
				for (String line : lines) {
					writeLine(cursor, line);
				}
			}
		} catch (Exception e) {
			DevAssistant.errorln(e);
		}
		restoreCursor();
		return palette;
	}
	
	protected int getPartTreeHeight(PlacePartTree partTree, EU_LAYOUT layout, int width) throws CIBusException {
		Map<String, PlacePart> rootMap = partTree.getRootMap();
		Map<String, Map<String, PlacePart>> childMap = partTree.makeChildMap();
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
		Map<String, Map<String, PlacePart>> placeMap = portal_config.getPlaceMap();
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
			west_width = getPartWdith(westMap);
			layoutInner(westMap, west_width);
		}
		
		shiftTop();
		int east_width = 0;
		Map<String, PlacePart> eastMap = placeMap.get(EU_LAYOUT.EAST.getName());
		if (eastMap != null) {
			shiftDown(north_height);
			east_width = getPartWdith(eastMap);
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
		int part_height = 0;
		int pc_height = 0;
		int west_width = 0;
		int east_width = 0;
		
		try {
			pc_height = getContentHeight(placeMap, EU_LAYOUT.NORTH, width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		try {
			pc_height = getContentHeight(placeMap, EU_LAYOUT.SOUTH, width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		try {
			west_width = getContentWidth(placeMap, EU_LAYOUT.WEST);
			pc_height = getContentHeight(placeMap, EU_LAYOUT.WEST, width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		try {
			east_width = getContentWidth(placeMap, EU_LAYOUT.EAST);
			pc_height = getContentHeight(placeMap, EU_LAYOUT.EAST, width-west_width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		
		try {
			pc_height = getContentHeight(placeMap, EU_LAYOUT.CENTER, width-west_width-east_width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		
		return part_height;
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
			String pcon = placePartContent(pp);
			if (pcon == null) return 0;
			return StringUtil.toLines(pcon, width).length;
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}
	
	protected int getPartWdith(Map<String, PlacePart> placeMap) {
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
	
	private int getContentWidth(Map<String, PlacePart> placeMap, EU_LAYOUT layout) throws CIBusException {
		PlacePart pp = placeMap.get(layout.getName());
		if (pp != null) {
			return getContentWidth(pp);
		}
		
		return 0;
	}
	
	private int getContentWidth(PlacePart pp) throws CIBusException {
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
		PartCursor part_cursor = new PartCursor();
		PlacePart northPart = placeMap.get(EU_LAYOUT.NORTH.getName());
		if (northPart != null) {
			try {
				String pcon = placePartContent(northPart);
				String[] lines = StringUtil.toLines(pcon, width);
				for (String line : lines) {
					writeLine(part_cursor, line);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		restoreCursor();
		storeCursor();
		PlacePart southPart = placeMap.get(EU_LAYOUT.SOUTH.getName());
		if (southPart != null) {
			try {
				String pcon = placePartContent(southPart);
				String[] lines = StringUtil.toLines(pcon, width);
				for (String line : lines) {
					writeLine(part_cursor, line);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		restoreCursor();
		storeCursor();
		int west_width = 0;
		PlacePart westPart = placeMap.get(EU_LAYOUT.WEST.getName());
		if (westPart != null) {
			try {
				String pcon = placePartContent(westPart);
				String[] lines = StringUtil.toLines(pcon);
				for (String line : lines) {
					writeLine(part_cursor, line);
				}
				west_width = StringUtil.maxLine(pcon);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		restoreCursor();
		storeCursor();
		int east_width = 0;
		PlacePart eastPart = placeMap.get(EU_LAYOUT.EAST.getName());
		if (eastPart != null) {
			try {
				String pcon = placePartContent(eastPart);
				String[] lines = StringUtil.toLines(pcon);
				east_width = StringUtil.maxLine(pcon);
				for (String line : lines) {
					shiftRight(width - east_width);
					writeLine(part_cursor, line);
					part_cursor.setPart_x(width - line.length());
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		restoreCursor();
		storeCursor();
		PlacePart centerPart = placeMap.get(EU_LAYOUT.CENTER.getName());
		if (centerPart != null) {
			try {
				String pcon = placePartContent(centerPart);
				int center_width = width - west_width - east_width;
				String[] lines = StringUtil.toLines(pcon, center_width);
				for (String line : lines) {
					shiftLeft(east_width);
					writeLine(part_cursor, line);
					part_cursor.setPart_x(east_width + part_cursor.getPart_x());
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		try {
			if (part_cursor.getPart_y() > 0) shiftUp(part_cursor.getPart_y() - 1);
			if (part_cursor.getPart_x() > 0) shiftLeft(part_cursor.getPart_x());
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
		restoreCursor();
	}
	
	protected void writeLine(PartCursor part_cursor, String line) {
		try {
			print(line);
			shiftNext(line);
			part_cursor.setPart_x(StringUtil.getWordCount(line));
			part_cursor.addPart_y(1);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}

	protected String placePartContent(PlacePart placePart) throws CIBusException {
		EU_ORIGIN origin = EU_ORIGIN.toOrigin(placePart.getOrigin());
		Part part;
		switch (origin) {
			case GLOBAL:
				part = portal_config.getPartMap().get(placePart.getName());
				if (part != null)
					return part.getContent().getValue();
				break;
			case PART:
				part = portal_config.getPartMap().get(placePart.getName());
				if (part != null)
					return part.getContent().getValue();
				break;
		}

		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.antelope.ci.bus.server.shell.BusBaseFrameShell#view()
	 */
	@Override
	protected void view() throws CIBusException {
		try {
			draw();
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}

	@Override
	protected ShellCursor initCursorPosistion() {
		return new ShellCursor(0, 0);
	}
	
	protected abstract void customInit() throws CIBusException;
}
