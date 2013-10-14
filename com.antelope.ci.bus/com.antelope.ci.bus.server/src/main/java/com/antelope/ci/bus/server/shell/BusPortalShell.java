// com.antelope.ci.bus.server.shell.BusPortalShell.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.shell;

import java.io.IOException;
import java.util.List;

import com.antelope.ci.bus.common.StringUtil;

/**
 * TODO 描述
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午3:02:14
 */
public class BusPortalShell extends BusShell {
	private static final int ROWS = 6;
	private long timestamp;
	boolean stop = false;
	private boolean schedule = false;

	private int pageSize = 23; // 页面显示行大小
	private int pageColumn = 80; // 页面显示列大小
	private int dataLine = 5; // 数据位置
	private int pageNo = 1;
	private int tabSize = 15;

	private List<String> groups;
	private int selectedLine = -1;

	private final static int[] COLUMN_SIZE = new int[] { // 列宽度数组(所有列之和为80)
	27, // 设备名称
			21, // 设备IP
			27 // 设备型号
	};

	public BusPortalShell(BusShellSession session) throws IOException {
		super(session);
	}

	@Override
	public int login() throws IOException {
		return 1;
	}

	@Override
	public void showBanner() throws IOException {
		io.println("Portal for @Antelope CI BUS");
	}

	@Override
	public void showMainFrame() throws IOException {
		timestamp = System.currentTimeMillis();
		schedule = true;
		showData();
		mainLoop();
	}

	private void showData() throws IOException {
		dataLine = 5;
		clear();

		pageSize = getConsoleHeight() - ROWS;
		pageColumn = getConsoleWidth();

		// show header
		showHeader();
		// show body
		showBody();
		// show footer
		showFooter();

		io.setCursor(dataLine, 1);
		selectedLine = -1;
	}

	private void showHeader() throws IOException {
		String top = "welcome to portal of @Antelope CI BUS";
		println(top);
		io.println();
		int headerLength = top.getBytes().length % pageColumn == 0 ? top
				.getBytes().length / pageColumn - 1 : top.getBytes().length
				/ pageColumn;
		dataLine = dataLine + headerLength;
		pageSize = pageSize - headerLength;

		// show tab

		// 打印标题,注意列宽度调整（通过空格进行控制）
		io.setBold(true);
		String header = "[N]设备名称                [I]IP地址            [M]设备型号";
		io.println(header);
		headerLength = header.getBytes().length / pageColumn;
		dataLine = dataLine + headerLength;
		pageSize = pageSize - headerLength;
	}

	private void showBody() throws IOException {
		io.println(toRowString(new String[] { "test1", "192.168.1.1", "dms" }));
		io.println(toRowString(new String[] { "test2", "192.168.1.2", "DNSys" }));
	}

	private String toRowString(String[] rowData) {
		StringBuffer strBuf = new StringBuffer();
		for (int j = 0; j < rowData.length; j++) {
			strBuf.append(StringUtil.getFixLengthString(rowData[j],
					COLUMN_SIZE[j], ' ', 1));
		}
		return strBuf.toString();
	}

	private void showFooter() throws IOException {
		io.homeCursor();
		io.moveDown(getConsoleHeight() - 1);
		String footer = "共 2 行 1/1 [←=上一页  →=下一页  ↑=上一行  ↓=下一行 P=修改密码 Q=退出]";
		io.write(footer);
	}

	private void mainLoop() throws IOException {
		stop = false;
		while (!stop) {
			int ch = io.read(1);

			timestamp = System.currentTimeMillis();
			switch (ch) {
			case 'f':
			case 'F': // refresh portal window
				showData();
				break;
			case 'q':
			case 'Q': // 退出
				exit();
				break;
			default:
				break;
			}
		}

		schedule = false;
		timestamp = -1;
	}

	private void exit() throws IOException {
		io.closeInput();
		io.eraseScreen();
		io.homeCursor();
		io.flush();
		stop = true;
	}
}
