// com.antelope.ci.bus.portal.core.shell.BusPortalShellUnit.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.portal.core.shell;

import java.util.ArrayList;
import java.util.List;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.shell.ShellPalette;
import com.antelope.ci.bus.server.shell.ShellText;
import com.antelope.ci.bus.server.shell.buffer.ShellCursor;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月24日		上午10:40:51 
 */
public class BusPortalShellLiving {
	private ShellCursor position;
	private List<BusPortalShellUnit> unitList;
	
	public BusPortalShellLiving() {
		super();
		position = new ShellCursor(0, 0);
		unitList = new ArrayList<BusPortalShellUnit>();
	}
	
	public boolean inUse() {
		if (!unitList.isEmpty())
			return true;
		return false;
	}
	
	public ShellCursor getPosition() {
		return position;
	}
	public void savePosition(ShellCursor position) {
		this.position = position;
	}
	public void savePosition(int x, int y) {
		this.position = new ShellCursor(x, y);
	}

	public List<BusPortalShellUnit> getUnitList() {
		return unitList;
	}
	public void setUnitList(List<BusPortalShellUnit> unitList) {
		this.unitList = unitList;
	}
	
	public void addUnit(BusPortalShellUnit unit) {
		unitList.add(unit);
	}
	
	public void add(ShellCursor cursor, String text) {
		unitList.add(new BusPortalShellUnit(cursor.clone(), text));
	}
	
	public void addUnits(List<BusPortalShellUnit> addUnitList) {
		unitList.addAll(addUnitList);
	}
	
	public void removeUnit(ShellCursor cursor) {
		int n = 0;
		while (n < unitList.size()) {
			BusPortalShellUnit unit = unitList.get(n);
			if (unit.same(cursor)) {
				unitList.remove(n);
				break;
			}
			n++;
		}
	}
	
	public void removeUnits(List<ShellCursor> removeList) {
		List<Integer> removeIndexList= new ArrayList<Integer>();
		int n = 0;
		for (ShellCursor remove : removeList) {
			n = 0;
			for (BusPortalShellUnit unit : unitList) {
				if (unit.same(remove))  {
					removeIndexList.add(n);
					break;
				}
				n++;
			}
		}
		for (Integer removeIndex : removeIndexList)
			unitList.remove(removeIndex);
	}
	
	public void removeFromPalette(ShellPalette palette) {
		List<Integer> removeIndexList= new ArrayList<Integer>();
		int n = 0;
		for (BusPortalShellUnit unit : unitList) {
			if (unit.contain(palette))
				removeIndexList.add(n);
			n++;
		}
		
		if (!removeIndexList.isEmpty()) {
			for (int i = removeIndexList.size()-1; i >= 0; i--) {
				int removeIndex = removeIndexList.get(i);
				unitList.remove(removeIndex);
			}
		}
	}
	
	public List<BusPortalShellUnit> getAheadLine(int x, int y) {
		List<BusPortalShellUnit> frontUnitList = new ArrayList<BusPortalShellUnit>();
		for (BusPortalShellUnit unit : unitList) {
			try {
				BusPortalShellUnit shellUnit = unit.makeFrontLineUnit(x, y);
				if (shellUnit != null)
					frontUnitList.add(shellUnit);
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
		
		return frontUnitList;
	}

	public static class BusPortalShellUnit {
		ShellCursor cursor;
		String text;
		
		public BusPortalShellUnit() {
			super();
		}
		
		public BusPortalShellUnit(ShellCursor cursor, String text) {
			super();
			this.cursor = cursor;
			this.text = text;
		}
		
		public ShellCursor getCursor() {
			return cursor;
		}

		public String getText() {
			return text;
		}

		public boolean same(BusPortalShellUnit unit) {
			return cursor.coincident(unit.cursor);
		}
		
		public boolean same(ShellCursor _cursor) {
			return cursor.coincident(_cursor);
		}
		
		int width() {
			String s = PortalShellText.peel(text);
			if (ShellText.isShellText(s))
				return ShellText.toShellText(s).placeholderWidth();
			else
				return StringUtil.lengthVT(s);
		}
		
		int height() {
			return 1;
		}
		
		public BusPortalShellUnit makeFrontLineUnit(int x, int y) throws CIBusException {
			if (cursor.getY() == y) {
				int start_x = cursor.getX();
				int width = width();
				int end_x = start_x + width;
				if (start_x < x) {
					if (end_x <= x) {
						return this;
					} else {
						int subLen = width - end_x + x;
						String s = PortalShellText.peel(text);
						if (ShellText.isShellText(s))
							s = ShellText.toShellText(s).getText();
						String subStr = StringUtil.subStringVT(s, 0, subLen);
						try {
							subStr = PortalShellText.createNewText(text, subStr);
						} catch (CIBusException e) {
							DevAssistant.errorln(e);
						}
						return new BusPortalShellUnit(cursor, subStr);
					}
				}
			}
			
			return null;
		}
		
		public boolean contain(ShellPalette palette) {
			int cx = cursor.getX();
			int cy = cursor.getY();
			int cw = cx + width();
			int ch = cy + height();
			int px = palette.getX();
			int py = palette.getY();
			int pw = px + palette.getWidth();
			int ph = py + palette.getHeight();
			if (px <= cx && py <= cy && pw >= cw && ph >= ch)
				return true;
			if (px >= cx && py >= cy && px < cw && py < ch)
				return true;
			if (pw <= cw && py >= cy && pw > cx && py < ch)
				return true;
			if (px >= cx && ph <= ch && px < cw && ph > cy)
				return true;
			if (pw <= cw && ph <= ch && pw > cx && ph > cy)
				return true;
			
			return false;
		}
	}
}

