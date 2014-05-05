// com.antelope.ci.bus.portal.shell.LayoutPaletteSet.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-7		上午11:17:37 
 */
public class LayoutPaletteSet {
	private ShellPalette north_palette;
	private ShellPalette west_palette;
	private ShellPalette east_palette;
	private ShellPalette center_palette;
	private ShellPalette south_palette;
	
	public LayoutPaletteSet() {
		super();
		north_palette = new ShellPalette();
		west_palette = new ShellPalette();
		east_palette = new ShellPalette();
		center_palette = new ShellPalette();
		south_palette = new ShellPalette();
	}
	

	public LayoutPaletteSet(ShellPalette north_palette,
			ShellPalette west_palette, ShellPalette east_palette,
			ShellPalette south_palette, ShellPalette center_palette) {
		super();
		this.north_palette = north_palette;
		this.west_palette = west_palette;
		this.east_palette = east_palette;
		this.south_palette = south_palette;
		this.center_palette = center_palette;
	}



	public ShellPalette getNorth_palette() {
		return north_palette;
	}

	public void setNorth_palette(ShellPalette north_palette) {
		this.north_palette = north_palette;
	}

	public ShellPalette getWest_palette() {
		return west_palette;
	}

	public void setWest_palette(ShellPalette west_palette) {
		this.west_palette = west_palette;
	}

	public ShellPalette getEast_palette() {
		return east_palette;
	}

	public void setEast_palette(ShellPalette east_palette) {
		this.east_palette = east_palette;
	}

	public ShellPalette getCenter_palette() {
		return center_palette;
	}

	public void setCenter_palette(ShellPalette center_palette) {
		this.center_palette = center_palette;
	}

	public ShellPalette getSouth_palette() {
		return south_palette;
	}

	public void setSouth_palette(ShellPalette south_palette) {
		this.south_palette = south_palette;
	}
	
	public void setAll(ShellPalette north_palette, ShellPalette south_palette, 
								ShellPalette west_palette, ShellPalette east_palette, ShellPalette center_palette) {
		this.north_palette = north_palette;
		this.south_palette = south_palette;
		this.west_palette = west_palette;
		this.east_palette = east_palette;
		this.center_palette = center_palette;
	}
	
	public int getHeight() {
		return north_palette.getHeight() + south_palette.getHeight() + contentHeight();
	}
	
	public int getWidth() {
		int w = 0;
		try {
			w = BusPortalShellUtil.max(north_palette.getWidth(), south_palette.getWidth(), 
					west_palette.getWidth()+east_palette.getWidth()+center_palette.getWidth());
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
		}
		return w;
	}
	
	private int contentHeight() {
		int h = 0;
		try {
			h = BusPortalShellUtil.max(west_palette.getHeight(), east_palette.getHeight(), center_palette.getHeight());
		} catch (CIBusException e) {
			DevAssistant.assert_exception(e);
		}
		return h;
	}
}

