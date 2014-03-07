// com.antelope.ci.bus.portal.shell.LayoutPaletteSet.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.shell;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-3-7		上午11:17:37 
 */
public class LayoutPaletteSet {
	private PartPalette north_palette;
	private PartPalette west_palette;
	private PartPalette east_palette;
	private PartPalette center_palette;
	private PartPalette south_palette;
	
	public LayoutPaletteSet() {
		super();
		north_palette = new PartPalette();
		west_palette = new PartPalette();
		east_palette = new PartPalette();
		center_palette = new PartPalette();
		south_palette = new PartPalette();
	}
	

	public LayoutPaletteSet(PartPalette north_palette,
			PartPalette west_palette, PartPalette east_palette,
			PartPalette center_palette, PartPalette south_palette) {
		super();
		this.north_palette = north_palette;
		this.west_palette = west_palette;
		this.east_palette = east_palette;
		this.center_palette = center_palette;
		this.south_palette = south_palette;
	}



	public PartPalette getNorth_palette() {
		return north_palette;
	}

	public void setNorth_palette(PartPalette north_palette) {
		this.north_palette = north_palette;
	}

	public PartPalette getWest_palette() {
		return west_palette;
	}

	public void setWest_palette(PartPalette west_palette) {
		this.west_palette = west_palette;
	}

	public PartPalette getEast_palette() {
		return east_palette;
	}

	public void setEast_palette(PartPalette east_palette) {
		this.east_palette = east_palette;
	}

	public PartPalette getCenter_palette() {
		return center_palette;
	}

	public void setCenter_palette(PartPalette center_palette) {
		this.center_palette = center_palette;
	}

	public PartPalette getSouth_palette() {
		return south_palette;
	}

	public void setSouth_palette(PartPalette south_palette) {
		this.south_palette = south_palette;
	}
	
	public void setAll(PartPalette north_palette, PartPalette south_palette, 
								PartPalette west_palette, PartPalette east_palette, PartPalette center_palette) {
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
		return max(north_palette.getWidth(), south_palette.getWidth(), 
				west_palette.getWidth()+east_palette.getWidth()+center_palette.getWidth());
	}
	
	private int contentHeight() {
		return max(west_palette.getHeight(), east_palette.getHeight(), center_palette.getHeight());
	}
	
	private int max(int a, int b, int c) {
		int m = a > b ? a : b;
		m = m > c ? m : c;
		return m;
	}
}

