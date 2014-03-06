// com.antelope.ci.bus.portal.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.portal.shell;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.configuration.LAYOUT;
import com.antelope.ci.bus.portal.configuration.ORIGIN;
import com.antelope.ci.bus.portal.configuration.PortalConfiguration;
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
	private static final String FILE_SUFFIX 					= "file:";
	protected static final String[] LAYOUT_ORDER		= new String[] {
		LAYOUT.NORTH.getName(), LAYOUT.SOUTH.getName(), 
		LAYOUT.WEST.getName(), LAYOUT.EAST.getName(), 
		LAYOUT.CENTER.getName()};

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
			if (isResource)
				a_pro = DealResource(a_pro);
			portal_config = configHelper.parseExtention(thisCls.getResourceAsStream(a_xml), a_pro, isResource);
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
	
	protected String DealResource(String res) {
		if (this.getClass().getResource(res) == null)
			return this.getClass().getPackage().getName() + "." + res;
		return res;
	}
	
	protected void layoutByTree() throws IOException, CIBusException {
		Map<String, PlacePartTree> placeTreeMap = portal_config.makePlacePartTreeMap();
		shiftTop();
		PlacePartTree northTree = placeTreeMap.get(LAYOUT.NORTH.getName());
		if (northTree != null) {
			Map<String, PlacePart> rootMap = northTree.getRootMap();
			Map<String, Map<String, PlacePart>> childMap = northTree.makeChildMap();
			for (String layout : LAYOUT_ORDER) {
				PlacePart rootPart = rootMap.get(layout);
				if (rootPart != null) {
					
					continue;
				}
				Map<String, PlacePart> childPartMap = childMap.get(layout);
				if (childPartMap != null) {
					layoutInner(childPartMap, getWidth());
				}
			}
		}
	}
	
	protected void layout() throws IOException, CIBusException {
		Map<String, Map<String, PlacePart>> placeMap = portal_config.getPlaceMap();
		shiftTop();
		int north_height = 0;
		Map<String, PlacePart> northMap = placeMap.get(LAYOUT.NORTH.getName());
		if (northMap != null) {
			north_height = getPartHeight(northMap, getWidth());
			layoutInner(northMap, getWidth());
		}
		
		Map<String, PlacePart> southMap = placeMap.get(LAYOUT.SOUTH.getName());
		if (southMap != null) {
			shiftBottom();
			layoutInner(southMap, getWidth());
		}

		shiftTop();
		int west_width = 0;
		Map<String, PlacePart> westMap = placeMap.get(LAYOUT.WEST.getName());
		if (westMap != null) {
			shiftDown(north_height);
			west_width = getPartWdith(westMap);
			layoutInner(westMap, west_width);
		}
		
		shiftTop();
		int east_width = 0;
		Map<String, PlacePart> eastMap = placeMap.get(LAYOUT.EAST.getName());
		if (eastMap != null) {
			shiftDown(north_height);
			east_width = getPartWdith(eastMap);
			shiftRight(getWidth() - east_width);
			layoutInner(eastMap, east_width);
		}
		
		shiftTop();
		Map<String, PlacePart> centerMap = placeMap.get(LAYOUT.CENTER.getName());
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
			pc_height = getContentHeight(placeMap, LAYOUT.NORTH, width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		try {
			pc_height = getContentHeight(placeMap, LAYOUT.SOUTH, width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		try {
			west_width = getContentWidth(placeMap, LAYOUT.WEST);
			pc_height = getContentHeight(placeMap, LAYOUT.WEST, width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		try {
			east_width = getContentWidth(placeMap, LAYOUT.EAST);
			pc_height = getContentHeight(placeMap, LAYOUT.EAST, width-west_width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		
		try {
			pc_height = getContentHeight(placeMap, LAYOUT.CENTER, width-west_width-east_width);
			part_height = pc_height > part_height ? pc_height : part_height;
		} catch (CIBusException e) {}
		
		return part_height;
	}
	
	private int getContentHeight(Map<String, PlacePart> placeMap, LAYOUT layout, int width) throws CIBusException {
		PlacePart pp = placeMap.get(layout.getName());
		if (pp != null) {
			return getContentHeight(pp, width);
		}
		
		return 0;
	}
	
	private int getContentHeight(PlacePart pp, int width) throws CIBusException {
		try {
			String pcon = placePartContent(pp);
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
			pc_width = getContentWidth(placeMap, LAYOUT.NORTH);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, LAYOUT.SOUTH);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, LAYOUT.WEST);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, LAYOUT.EAST);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		try {
			pc_width = getContentWidth(placeMap, LAYOUT.CENTER);
			part_width = pc_width > part_width ? pc_width : part_width;
		} catch (CIBusException e) {}
		
		return part_width;
	}
	
	private int getContentWidth(Map<String, PlacePart> placeMap, LAYOUT layout) throws CIBusException {
		PlacePart pp = placeMap.get(layout.getName());
		if (pp != null) {
			return getContentWidth(pp);
		}
		
		return 0;
	}
	
	private int getContentWidth(PlacePart pp) throws CIBusException {
		try {
			String pcon = placePartContent(pp);
			return StringUtil.maxLine(pcon);
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("", e);
		}
	}

	protected void layoutInner(Map<String, PlacePart> placeMap, int width) {
		storeCursor();
		PartCursor part_cursor = new PartCursor();
		PlacePart northPart = placeMap.get(LAYOUT.NORTH.getName());
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
		PlacePart southPart = placeMap.get(LAYOUT.SOUTH.getName());
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
		PlacePart westPart = placeMap.get(LAYOUT.WEST.getName());
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
		PlacePart eastPart = placeMap.get(LAYOUT.EAST.getName());
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
		PlacePart centerPart = placeMap.get(LAYOUT.CENTER.getName());
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
			println();
			print(line);
			part_cursor.setPart_x(StringUtil.getWordCount(line));
			part_cursor.addPart_y(1);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}

	protected String placePartContent(PlacePart placePart) throws CIBusException {
		ORIGIN origin = ORIGIN.toOrigin(placePart.getOrigin());
		switch (origin) {
			case GLOBAL:
				Part part =portal_config.getPartMap().get(placePart.getName());
				if (part != null)
					return part.getContent().getValue();
				break;
			case PART:
				
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
			layout();
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
