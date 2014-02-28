// com.antelope.ci.bus.portal.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.portal.shell;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.configuration.LAYOUT;
import com.antelope.ci.bus.portal.configuration.ORIGIN;
import com.antelope.ci.bus.portal.configuration.xo.Part;
import com.antelope.ci.bus.portal.configuration.xo.PlacePart;
import com.antelope.ci.bus.portal.configuration.xo.Portal;
import com.antelope.ci.bus.server.shell.BusBaseFrameShell;
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

	public BusPortalShell() throws CIBusException {
		super();
		init();
		if (portal_config == null)
			throw new CIBusException("", "must set configration of portal");
	}
	
	protected abstract void init() throws CIBusException;

	protected void layout() throws IOException {
		Map<String, Map<String, PlacePart>> placeMap = portal_config.getPlaceMap();
		shiftTop();
		Map<String, PlacePart> northMap = placeMap.get(LAYOUT.NORTH.getName());
		if (northMap != null) {
			layoutInner(northMap, getWidth());
		}
		storeCursor();

		shiftBottom();
		Map<String, PlacePart> southMap = placeMap.get(LAYOUT.SOUTH.getName());
		if (southMap != null) {
			layoutInner(southMap, getWidth());
		}

		restoreCursor();
	}

	protected void layoutInner(Map<String, PlacePart> placeMap, int width) {
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
				for (String line : lines) {
					shiftLineEnd();
					shiftLeft(east_width);
					writeLine(part_cursor, line);
					part_cursor.setPart_x(width - line.length());
				}
				east_width = StringUtil.maxLine(pcon);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}

		restoreCursor();
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
}
