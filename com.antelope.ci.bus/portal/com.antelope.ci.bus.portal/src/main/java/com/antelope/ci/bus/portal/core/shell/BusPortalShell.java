// com.antelope.ci.bus.portal.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.portal.core.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.DevAssistant;
import com.antelope.ci.bus.common.NetVTKey;
import com.antelope.ci.bus.common.StringUtil;
import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.portal.BusPortalActivator;
import com.antelope.ci.bus.portal.core.configuration.BusPortalConfigurationHelper;
import com.antelope.ci.bus.portal.core.configuration.PortalConfiguration;
import com.antelope.ci.bus.portal.core.configuration.xo.Portal;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.EU_LAYOUT;
import com.antelope.ci.bus.portal.core.configuration.xo.meta.FontExpression;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.CommonHit;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.Part;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.PlacePart;
import com.antelope.ci.bus.portal.core.configuration.xo.portal.PlacePartTree;
import com.antelope.ci.bus.portal.core.shell.BusPortalShellLiving.BusPortalShellUnit;
import com.antelope.ci.bus.portal.core.shell.buffer.BusPortalBufferFactory;
import com.antelope.ci.bus.portal.core.shell.command.PortalCommandAdapter;
import com.antelope.ci.bus.portal.core.shell.form.PortalFormContext;
import com.antelope.ci.bus.portal.core.shell.form.PortalFormContextFactory;
import com.antelope.ci.bus.server.shell.base.BusBaseFrameShell;
import com.antelope.ci.bus.server.shell.base.BusShellMode;
import com.antelope.ci.bus.server.shell.base.BusShellMode.BaseShellMode;
import com.antelope.ci.bus.server.shell.base.BusShellStatus;
import com.antelope.ci.bus.server.shell.base.Shell;
import com.antelope.ci.bus.server.shell.base.ShellMode.BaseShellModeType;
import com.antelope.ci.bus.server.shell.base.ShellPalette;
import com.antelope.ci.bus.server.shell.base.ShellText;
import com.antelope.ci.bus.server.shell.buffer.BusBuffer;
import com.antelope.ci.bus.server.shell.buffer.ShellCursor;

/**
 * it is a template of portal shell.
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-29 下午9:15:32
 */
@Shell(name="base.frame", commandAdapter=PortalCommandAdapter.NAME)
public abstract class BusPortalShell extends BusBaseFrameShell {
	protected static final Logger log = Logger.getLogger(BusPortalShell.class);
	
	public enum CONTROLAIM{SILENT, ACTION, COMMAND, INPUT};
	protected static final EU_LAYOUT[] LAYOUT_ORDER		= new EU_LAYOUT[] 
			{EU_LAYOUT.NORTH, EU_LAYOUT.SOUTH, EU_LAYOUT.WEST, EU_LAYOUT.EAST, EU_LAYOUT.CENTER};
	protected final static Map<String, Integer> CONTENT_SCALE = new HashMap<String, Integer>();
	static {
		CONTENT_SCALE.put(EU_LAYOUT.WEST.getName(), 2);
		CONTENT_SCALE.put(EU_LAYOUT.CENTER.getName(), 6);
		CONTENT_SCALE.put(EU_LAYOUT.EAST.getName(), 2);
	}
	
	protected Portal portal;
	protected ShellPalette contentPalette;
	protected PortalBlock activeBlock;
	protected PortalBlock mainBlock;
	protected List<PortalBlock> mainBlocks;
	protected boolean loadMainblock;
	protected Map<String, List<PortalBlock>> minorBlocksSet;
	protected BusPortalShellLiving shellLiving;
	protected ShellCursor initPosition;
	protected List<BusPortalBufferFactory> bufferFactoryList;
	protected boolean inputInitialized;
	protected boolean inputFinished;
	protected BusBuffer mainBuffer;
	protected boolean formControlMode;
	protected PortalFormContext activeFormContext;
	protected long form_command_wait;
	protected long lastFormCommandTime;
	protected final int[] control_keys = new int[]{
			NetVTKey.BACKSPACE,
			NetVTKey.DELETE,
			NetVTKey.LEFT,
			NetVTKey.RIGHT,
			NetVTKey.UP,
			NetVTKey.DOWN
	};

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
		mainBlocks = new ArrayList<PortalBlock>();
		loadMainblock = true;
		minorBlocksSet = new HashMap<String, List<PortalBlock>>();
		shellLiving = new BusPortalShellLiving();
		initPosition = new ShellCursor(0, 0);
		bufferFactoryList = new ArrayList<BusPortalBufferFactory>();
		inputInitialized = false;
		inputFinished = false;
		formControlMode = false;
		lastFormCommandTime = -1;
		form_command_wait = BusPortalActivator.getFormCommandWait();
		if (portal == null)
			throw new CIBusException("", "must set configration of portal");
	}
	
	public void startFormCommand() {
		lastFormCommandTime = System.currentTimeMillis();
	}
	
	public boolean formCommandMode() {
		return formControlMode;
	}
	
	public void replaceActiveFormContext(PortalFormContext activeFormContext) {
		this.activeFormContext = activeFormContext;
	}
	
	public PortalFormContext getActiveFormContext() {
		return activeFormContext;
	}
	
	public void finishFormCommandMode() {
		formControlMode = false;
		lastFormCommandTime = -1;
		commandAdapter.closeUserFinal();
	}
	
	public Portal getPortal() {
		return portal;
	}
	
	public BusPortalShellLiving getLiving() {
		return shellLiving;
	}
	
	public void addFormBuffer(BusPortalBufferFactory formBuffer) {
		bufferFactoryList.add(formBuffer);
	}
	
	public void savePositionFromContent(int x, int y) {
		savePosition(contentPalette.getX() + x, contentPalette.getY() + y);
	}
	
	public void savePosition(int x, int y) {
		shellLiving.savePosition(x, y);
	}
	
	public ShellPalette getContentPalette() {
		return contentPalette;
	}
	
	public void commomIO() {
		try {
			io.setUnderlined(!editMode);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void editIO() {
		try {
			io.setUnderlined(editMode);
		} catch (IOException e) {
			DevAssistant.errorln(e);
		}
	}
	
	public void drawForm(String name, Object content) throws CIBusException {
		clearContent();
		if (content instanceof ShellLineContentSet) {
			ShellLineContentSet contentSet = (ShellLineContentSet) content;
			List<PortalBlock> formBlocks = writeFormLine(getContentCursor(), contentSet.getContentList());
			minorBlocksSet.put(name, formBlocks);
			List<List<PortalBlock>> formMatrix = genMatrix(formBlocks);
			activeBlock = fromMatrix(formMatrix);
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#writeContent(java.lang.Object)
	 */
	@Override public void writeContent(Object content) throws CIBusException {
		clearContent();
		if (content instanceof ShellLineContentSet) {
			ShellLineContentSet contentSet = (ShellLineContentSet) content;
			writePortalLine(getContentCursor(), contentSet.getContentList());
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#clearContent()
	 */
	@Override public void clearContent() throws CIBusException {
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
			shellLiving.removeFromPalette(contentPalette);
		}
	}
	
	@Override public void moveContent() throws CIBusException {
		if (contentPalette != null) {
			shiftTop();
			move(contentPalette.getX(), contentPalette.getY());
		}
	}
	
	public void enterEdit() {
		activeBlock.disable();
		lastEditMode = false;
		editMode = true;
	}
	
	public PortalBlock getActiveBlock() {
		return activeBlock;
	}
	
	public void updateBlock(PortalBlock block) {
		this.activeBlock = block;
	}
	
	public void leaveBlock() {
		activeBlock.disable();
	}
	
	public void lostFocus() throws CIBusException {
		String value = activeBlock.getValue();
		ShellText text;
		if (ShellText.isShellText(value)) {
			text = ShellText.toShellText(value);
		} else {
			text = new ShellText();
			text.setText(value);
		}
		value = text.toString();
		rewriteUnit(activeBlock.getCursor(), value);
		move(-activeBlock.getWidth(), 0);
	}
	
	public void defaultFocus() throws CIBusException {
		focus(null);
	}
	
	public void focus(CommonHit hit) throws CIBusException {
		if (activeBlock == null || !activeBlock.available())
			return;
	
		String value = activeBlock.getValue();
		ShellText text;
		if (ShellText.isShellText(value)) {
			text = ShellText.toShellText(value);
		} else {
			text = new ShellText();
			text.setText(value);
		}
		if (hit == null) {
			hit = portal.getBlockHit();
			FontExpression font = hit.getFont().toRenderFont().toFontExpression();
			text.setFont_mark(font.getMark().getCode());
			text.setFont_size(font.getSize().getCode());
			text.setFont_style(font.getSytle().getCode());
		}
		value = text.toString();
		rewriteUnit(activeBlock.getCursor(), value);
		move(-activeBlock.getWidth(), 0);
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
			configHelper.addConfigPair(thisCls.getName(), pc.properties(), pc.xml(), pc.validate());
		} 
		portal = configHelper.parse(this.getClass().getName());
	}
	
	@Override public void setSort(int sort) {
		// nothing
	}
	
	public void addMinorBlock(String name, List<PortalBlock> blockList) {
		if (!minorBlocksSet.containsKey(name))
			minorBlocksSet.put(name, blockList);
		List<List<PortalBlock>> minorMatrix = genMatrix(blockList);
		activeBlock = fromMatrix(minorMatrix);
	}
	
	protected void initMainBlocks() {
		if (loadMainblock) {
			loadMainblock = false;
			List<List<PortalBlock>> mainMatrix = genMatrix(mainBlocks);
			mainBlock = fromMatrix(mainMatrix);
			activeBlock = mainBlock;
		}
	}
	
	protected List<List<PortalBlock>> genMatrix(List<PortalBlock> blockList) {
		List<List<PortalBlock>> matrix = new ArrayList<List<PortalBlock>>();
		Collections.sort(blockList, new Comparator<PortalBlock>() {
			@Override public int compare(PortalBlock b1, PortalBlock b2) {
				int ret = b1.getCursor().getX() - b2.getCursor().getX();
				if (ret == 0)
					return b1.getCursor().getY() - b2.getCursor().getY();
				return ret;
			}
		});
		
		int current_y = 0;
		int last_y = 0;
		List<PortalBlock> rowList = new ArrayList<PortalBlock>();
		for (PortalBlock block : blockList) {
			current_y = block.getCursor().getY();
			if (current_y != last_y) {
				if (!rowList.isEmpty())
					matrix.add(rowList);
				 rowList = new ArrayList<PortalBlock>();
			}
			rowList.add(block);
			last_y = current_y;
		}
		if (!rowList.isEmpty())
			matrix.add(rowList);
		
		return matrix;
	}
	
	protected PortalBlock fromMatrix(List<List<PortalBlock>> matrix) {
		PortalBlock rootBlock = null;
		PortalBlock up_block;
		PortalBlock down_block;
		PortalBlock left_block;
		PortalBlock right_block;
		int row_index = 0;
		int col_index = 0;
		List<PortalBlock> lastList = null;
		List<PortalBlock> nextList = null;
		for (List<PortalBlock> rowList : matrix) {
			up_block = null;
			down_block = null;
			left_block = null;
			right_block = null;
			col_index = 0;
			lastList = null;
			nextList = null;
			
			for (PortalBlock current_block : rowList) {
				if (row_index == 0 && col_index == 0)
					rootBlock = current_block;
				if (row_index > 0) {
					lastList = matrix.get(row_index-1);
					if (!lastList.isEmpty())
						left_block = lastList.get(lastList.size() - 1);
				}
				if (matrix.size() > (row_index+1)) {
					nextList = matrix.get(row_index+1);
					if (!nextList.isEmpty())
						right_block = nextList.get(0);
				}
				
				if (lastList != null)
					up_block = findUpBlock(current_block, lastList);
				if (up_block != null)
					current_block.setUp(up_block);
				
				if (nextList != null)
					down_block = findUpBlock(current_block, nextList);
				if (down_block != null)
					current_block.setDown(down_block);
				
				if (col_index > 0)
					left_block = rowList.get(col_index - 1);
				if (left_block != null)
					current_block.setLeft(left_block);
					
				if ((col_index + 1) < rowList.size())
					right_block = rowList.get(col_index + 1);
				if (right_block != null)
					current_block.setRight(right_block);
				
				col_index++;
			}
			
			row_index++;
		}
		
		return rootBlock;
	}
	
	protected PortalBlock findUpBlock(PortalBlock block, List<PortalBlock> rowList) {
		return findValignBlock(block, rowList, 0);
	}
	
	protected PortalBlock findDownBlock(PortalBlock block, List<PortalBlock> rowList) {
		return findValignBlock(block, rowList, 1);
	}
	
	protected PortalBlock findValignBlock(PortalBlock block, List<PortalBlock> rowList, int updown) {
		int x_start, x_end, row_x;
		for (PortalBlock row_block : rowList) {
			boolean mark;
			if (updown == 0)	mark = row_block.isMarkUp();
			else						mark = row_block.isMarkDown();
			if (!mark) {
				x_start = block.getCursor().getX();
				x_end = x_start + block.getWidth();
				row_x = row_block.getCursor().getX();
				if (x_start <= row_x || x_end > row_x) {
					if (updown == 0)	row_block.markUp();
					else						row_block.markDown();
					return row_block;
				}
			}
		}
		
		return null;
	}
	
	protected void draw() throws CIBusException {
		if (shellLiving.inUse())
			drawLiving();
		else
			drawPortal();
	}
	
	protected void drawLiving() throws CIBusException {
		for (BusPortalShellUnit unit : shellLiving.getUnitList())
			writeLivingUnit(unit);
		shiftTop();
		moveCursor(shellLiving.getPosition());
	}
	
	protected void drawPortal() throws CIBusException {
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
					north_height = sizeMatrix(contentList);
					writePortalLine(cursor, contentList);
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
					south_height = sizeMatrix(contentList);
					cursor.addY(height - north_height - south_height);
					moveCursor(cursor);
					writePortalLine(cursor, contentList);
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
					writePortalLine(cursor, contentList);
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
					writePortalLine(cursor, contentList);
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
				int center_height = height - north_height - south_height;
				List<List<String>> contentList = placePartContent(centerPart, center_width);
				if (!contentList.isEmpty()) {
					if (content_cursor == null)
						content_cursor = cursor.clone();
					else
						cursor = content_cursor.clone();
					moveCursor(cursor);
					writePortalLine(cursor, contentList);
					putContentPalette(centerPart, content_cursor, center_width, center_height);
				}
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected void putContentPalette(PlacePart part, ShellCursor cursor, int width, int height) {
		if ("content".equals(part.getName())) {
			contentPalette = new ShellPalette();
			contentPalette.setStartPoint(cursor.getX(), cursor.getY());
			contentPalette.setShapePoint(width, height);
			super.addPalette(PortalShellUtil.LAYOUT_CONTENT, contentPalette);
		}
	}
	
	protected void moveCursor(ShellCursor cursor) throws CIBusException {
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
				int palette_height = sizeMatrix(contentList);
				palette.setShapePoint(palette_width, palette_height);
				writePortalLine(cursor, contentList);
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
	
	protected int getContentHeight(Map<String, PlacePart> placeMap, EU_LAYOUT layout, int width) throws CIBusException {
		PlacePart pp = placeMap.get(layout.getName());
		if (pp != null)
			return getContentHeight(pp, width);
		
		return 0;
	}
	
	protected int getContentHeight(PlacePart pp, int width) throws CIBusException {
		try {
			List<List<String>> contentList = placePartContent(pp, width);
			return sizeMatrix(contentList);
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
	
	protected int getContentWidth(Map<String, PlacePart> placeMap, EU_LAYOUT layout, int width) throws CIBusException {
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
	
	protected void layoutInner(Map<String, PlacePart> placeMap, int width) {
		storeCursor();
		ShellCursor cursor = new ShellCursor();
		PlacePart northPart = placeMap.get(EU_LAYOUT.NORTH.getName());
		if (northPart != null) {
			try {
				List<List<String>> contentList = placePartContent(northPart, width);
				writePortalLine(cursor, contentList);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		
		storeCursor();
		PlacePart southPart = placeMap.get(EU_LAYOUT.SOUTH.getName());
		if (southPart != null) {
			try {
				List<List<String>> contentList = placePartContent(southPart, width);
				writePortalLine(cursor, contentList);
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
				writePortalLine(cursor, contentList);
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
				writePortalLine(cursor, contentList);
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
				writePortalLine(cursor, contentList);
			} catch (Exception e) {
				DevAssistant.errorln(e);
			}
		}
		try {
			if (cursor.getY() > 0) shiftUp(cursor.getY() - 1);
			if (cursor.getX() > 0) shiftLeft(cursor.getX());
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected List<PortalBlock> writeFormLine(ShellCursor cursor, List<List<String>> valueList) {
		List<PortalBlock> formBlocks = new ArrayList<PortalBlock>();
		writeLine(formBlocks, cursor, valueList, 1);
		return formBlocks;
	}
	
	protected void writePortalLine(ShellCursor cursor, List<List<String>> valueList) {
		writeLine(mainBlocks, cursor, valueList, 0);
	}
	
	protected void writeLine(List<PortalBlock> blocks, ShellCursor cursor, List<List<String>> valueList, int type) {
		int default_x = cursor.getX();
		boolean first = true;
		for (List<String> values : valueList) {
			try {
				if (first)
					first = false;
				else
					shiftDown(1);
				int count = 0;
				for (String value : values) {
					if (ShellText.containP(value)) {
						List<String> vList = PortalShellText.splitForP(value);
						for (String v : vList)
							count += writeUnit(blocks, cursor, v, type);
					} else {
						count += writeUnit(blocks, cursor, value, type);
					}
				}
				if (count != 0)
					shiftLeft(count);
				cursor.setX(default_x);
				cursor.addY(1);
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected int writeUnit(List<PortalBlock> blocks, ShellCursor cursor, String str, int type) throws CIBusException {
		shellLiving.add(cursor, str);
		ShellCursor blockCursor = cursor.clone();
		int width = 0;
		if (PortalShellText.isFocus(str)) {
			initPosition = cursor.clone();
			shellLiving.savePosition(initPosition);
		}
		String s = PortalShellText.peel(str);
		if (ShellText.isShellText(s)) {
			ShellText text = writeFormat(cursor, s);
			width= text.placeholderWidth();
		} else {
			write(cursor, s);
			width = StringUtil.lengthVT(s);
		}
		
		PortalBlock portalBlock = null;
		if (PortalShellText.containBlock(str)) {
			portalBlock = new PortalBlock();
			portalBlock.setName(PortalShellText.getName(str));
			portalBlock.setCursor(blockCursor);
			portalBlock.setWidth(width);
			portalBlock.setValue(s);
		}
		if (portalBlock != null) {
			switch (type) {
				case 1:			// form
					blocks.add(portalBlock);
					break;
				case 0:			// portal
				default:
					if (loadMainblock)
						blocks.add(portalBlock);
					break;
			}
	}
		
		return width;
	}
	
	protected void writeLivingUnit(BusPortalShellUnit unit) throws CIBusException {
		writeLivingUnit(unit.cursor, unit.text);
	}
	
	public void rewriteUnit(ShellCursor cursor, String str) throws CIBusException {
		shiftTop();
		moveCursor(cursor);
		if (ShellText.isShellText(str))
			writeFormat(str);
		else
			print(str);
	}
	
	public void rewriteUnits(List<BusPortalShellUnit> units) {
		for (BusPortalShellUnit unit : units) {
			try {
				rewriteUnit(unit.getCursor(), PortalShellText.peel(unit.getText()));
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected void writeLivingUnit(ShellCursor cursor, String str) throws CIBusException {
		shiftTop();
		moveCursor(cursor);
		String s = PortalShellText.peel(str);
		if (ShellText.isShellText(s))
			writeFormat(s);
		else
			print(s);
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
				lastsize = StringUtil.lengthVT(line);
				write(cursor, line);
			} catch (CIBusException e) {
				DevAssistant.errorln(e);
			}
		}
	}
	
	protected void write(ShellCursor cursor, String value) throws CIBusException {
		print(value);
		cursor.addX(StringUtil.lengthVT(value));
	}
	
	protected void write(ShellCursor cursor, ShellText text) {
		print(text);
		cursor.addX(StringUtil.lengthVT(text.getText()));
	}
	
	protected void writeFormat(String value) {
		ShellText text = ShellText.toShellText(value);
		if (text != null)
			print(text);
	}
	
	protected ShellText writeFormat(ShellCursor cursor, String value) {
		ShellText text = ShellText.toShellText(value);
		if (text != null)
			write(cursor, text);
		return text;
	}
	
	protected List<List<String>> placePartContent(PlacePart placePart, int width)  {
		Part part = portal.getPartMap().get(placePart.getName());
		return part.relist(width);
	}
	
	protected<O> int sizeMatrix(List<List<O>> matrix) {
		return matrix.size();
	}
	
	protected<O> int totalSizeMatrix(List<List<O>> matrix) {
		int size = 0;
		for (List<O> rowList : matrix)
			size += rowList.size();
		return size;
	}
	
	@Override protected void view() throws CIBusException {
		try {
			draw();
			initMainBlocks();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}

	@Override protected ShellCursor initCursorPosistion() {
		if (!shellLiving.inUse())
			shellLiving.savePosition(initPosition);
		return initFocus();
	}
	
	protected ShellCursor initFocus() {
		try {
			defaultFocus();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
		return shellLiving.getPosition();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#handleInput(int)
	 */
	@Override protected boolean handleInput(int c) {
		if (lastFormCommandTime != -1 && 
				((System.currentTimeMillis() - lastFormCommandTime) > form_command_wait))
			activeFormContext.exitFormCommand();
			
		try {
			BaseShellModeType baseModeType = BusShellMode.getBaseModeType(mode);
			if (baseModeType == null)
				return false;
			switch (baseModeType) {
				case INPUT:
				case EDIT:
					if (!inputInitialized)
						initInput();
					
					if (formControlMode) {
						action(c);
					} else {
						CONTROLAIM aim = handleInputControl(c);
						switch (aim) {
							case SILENT:
								break;
							case ACTION:
								formControlMode = true;
								resetControlKey(c);
								action(c);
								formControlMode = false;
								break;
							case COMMAND:
								formControlMode = true;
								resetControlKey(c);
								action(c);
								input.put((char) c); 
								break;
							case INPUT:
								input.put((char) c);
								break;
						}
					}
					return true;
				case MAIN:
				default:
					return false;
			}
		} catch (Exception e) {
			DevAssistant.errorln(e);
		}
		
		return false;
	}
	
	protected CONTROLAIM handleInputControl(int c) throws CIBusException {
		switch (c) {
			case NetVTKey.BACKSPACE:
				input.backspace();
				return CONTROLAIM.SILENT;
			case NetVTKey.DELETE:
				input.delete();
				return CONTROLAIM.SILENT;
			case NetVTKey.LEFT:
				return CONTROLAIM.ACTION;
			case NetVTKey.RIGHT:
				return CONTROLAIM.ACTION;
			case NetVTKey.UP:
				return CONTROLAIM.ACTION;
			case NetVTKey.DOWN:
				return CONTROLAIM.ACTION;
			case NetVTKey.ESCAPE:
				return CONTROLAIM.COMMAND;
			case NetVTKey.LF:
				return CONTROLAIM.SILENT;
			default:
				return CONTROLAIM.INPUT;
		}
	}
	
	public void exitInput() throws CIBusException {
		mode = BaseShellMode.MAIN.getName();
		activeBlock = mainBlock;
		activeBlock.enable();
		lastEditMode = true;
		editMode = false;
		defaultFocus();
	}
	
	protected void initInput() {
		inputInitialized = true;
		inputFinished = false;
	}
	
	protected void finishInput() {
		inputInitialized = false;
		inputFinished = true;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#handleMode()
	 */
	@Override protected void handleMode() {
		try {
			changeMode();
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	protected void changeMode() throws CIBusException {
		if (lastEditMode ^ editMode) {
			try {
				io.setUnderlined(editMode);
				lastEditMode = editMode;
			} catch (IOException e) {
				throw new CIBusException("", "change into input mode", e);
			}
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#up()
	 */
	@Override public void up() {
		move(1);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#down()
	 */
	@Override public void down() {
		move(2);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#left()
	 */
	@Override public void left() {
		move(3);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#right()
	 */
	@Override public void right() {
		move(4);
	}
	
	protected void move(int direction) {
		BaseShellModeType baseModeType = BusShellMode.getBaseModeType(mode);
		if (baseModeType == null)
			return;
		switch (baseModeType) {
			case INPUT:
			case EDIT:
				switch (direction) {
					case 1:
						super.up();
						activeFormContext.upWidget();
						break;
					case 2:
						super.down();
						activeFormContext.downWidget();
						break;
					case 3:
						super.left();
						activeFormContext.leftWidget();
						break;
					case 4:
						super.right();
						activeFormContext.rightWidget();
						break;
					default:
						break;
				}
				break;
			case MAIN:
			default:
				moveBlock(direction);
				break;
		}
	}
	
	public void upBlock() {
		moveBlock(1);
	}
	
	public void downBlock() {
		moveBlock(2);
	}
	
	public void leftBlock() {
		moveBlock(3);
	}
	
	public void rightBlock() {
		moveBlock(4);
	}
	
	protected void moveBlock(int direction) {
		if (null == activeBlock || !activeBlock.available())
			return;
		try {
			lostFocus();
			PortalBlock moveBlock = null;
			switch (direction) {
				case 1:
					moveBlock = activeBlock.getUp();
					break;
				case 2:
					moveBlock = activeBlock.getDown();
					break;
				case 3:
					moveBlock = activeBlock.getLeft();
					break;
				case 4:
					moveBlock = activeBlock.getRight();
					break;
			}
			if (moveBlock != null) {
				int x = moveBlock.getCursor().getX();
				int y = moveBlock.getCursor().getY();
				shift(x, y);
				savePosition(x, y);
				updateBlock(moveBlock);
				moveBlock.enable();
			}
		} catch (CIBusException e) {
			DevAssistant.errorln(e);
		}
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusBaseFrameShell#afterView()
	 */
	@Override protected void afterView() {
		mainBuffer = input;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#shutdown()
	 */
	@Override protected void shutdown() throws CIBusException {
		PortalFormContextFactory.getFactory().remove(this);
		customShutdown();
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.antelope.ci.bus.server.shell.BusShell#clearData()
	 */
	@Override public void clearData() {
		BaseShellModeType modeType = BusShellMode.getBaseModeType(mode);
		if (modeType == null)
			modeType = BaseShellModeType._DEFAULT;
		switch (modeType) {
			case INPUT:
			case EDIT:
				break;
			case MAIN:
			case _DEFAULT:
				super.clearData();
				break;
		}
		
	}
	
	protected abstract void customInit() throws CIBusException;
	
	protected abstract PortalBlock loadBlock();
	
	protected abstract void customShutdown() throws CIBusException;
}