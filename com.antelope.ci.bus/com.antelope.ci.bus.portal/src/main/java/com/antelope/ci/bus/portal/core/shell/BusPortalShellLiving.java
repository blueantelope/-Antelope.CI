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

import com.antelope.ci.bus.server.shell.buffer.ShellCursor;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014年9月24日		上午10:40:51 
 */
class BusPortalShellLiving {
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
	
	public void removeUnit(List<ShellCursor> removeList) {
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

	static class BusPortalShellUnit {
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
		
		public boolean same(BusPortalShellUnit unit) {
			return cursor.same(unit.cursor);
		}
		
		public boolean same(ShellCursor _cursor) {
			return cursor.same(_cursor);
		}
	}
}

