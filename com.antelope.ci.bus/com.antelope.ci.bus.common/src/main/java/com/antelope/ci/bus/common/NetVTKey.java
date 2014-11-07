// com.antelope.ci.bus.common.TerminalKey.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-4-15		下午4:41:07 
 */
public class NetVTKey {
	public static final int ESC = 27;
	
	public static final int ESCAPE = 1200; 	// Escape Sequence-ing 12xx
	
	/**
	 * Network Virtual Terminal Specific Keys Thats what we have to offer at
	 * least.
	 */
	public static final int BEL = 7;
	public static final int BS = 8;
	public static final int DEL = 127;
	public static final int CR = 13;
	public static final int LF = 10;

	public static final int FCOLOR = 10001;
	public static final int BCOLOR = 10002;
	public static final int STYLE = 10003;
	public static final int RESET = 10004;
	public static final int BOLD = 1;
	public static final int BOLD_OFF = 22;
	public static final int ITALIC = 3;
	public static final int ITALIC_OFF = 23;
	public static final int BLINK = 5;
	public static final int BLINK_OFF = 25;
	public static final int UNDERLINED = 4;
	public static final int UNDERLINED_OFF = 24;
	public static final int REVERSE = 7;
	public static final int REVERSE_OFF = 27;
	public static final int DEVICERESET = 10005;
	public static final int LINEWRAP = 10006;
	public static final int NOLINEWRAP = 10007;

	// Constants
	
	/**
	 * blank
	 */
	public static final int SPACE = 32;

	/**
	 * Left (defining a direction on the terminal)
	 */
	public static final int UP = 1001;

	/**
	 * Right (defining a direction on the terminal)
	 */
	public static final int DOWN = 1002;

	/**
	 * Up (defining a direction on the terminal)
	 */
	public static final int RIGHT = 1003;

	/**
	 * Down (defining a direction on the terminal)
	 */
	public static final int LEFT = 1004;

	/**
	 * Tabulator (defining the tab key)
	 */
	public static final int TABULATOR = 1301;

	/**
	 * Delete (defining the del key)
	 */
	public static final int DELETE = 1302;

	/**
	 * Backspace (defining the backspace key)
	 */
	public static final int BACKSPACE = 1303;

	/**
	 * Enter (defining the return or enter key)
	 */
//	public static final int ENTER = 10;

	/**
	 * Color init (defining ctrl-a atm)
	 */
	public static final int COLORINIT = 1304;

	/**
	 * Logout request (defining ctrl-d atm)
	 */
	public static final int LOGOUTREQUEST = 1306;
	
	
	/**
	 * Terminal independent representation constants for terminal functions.
	 */
	public static final int[] HOME = { 0, 0 };

	public static final int IOERROR = -1; // IO error
	// HOME=1005, //Home cursor pos(0,0)

	public static final int// Functions 105x
	STORECURSOR = 1051; // store cursor position + attributes
	public static final int RESTORECURSOR = 1052; // restore cursor + attributes

	public static final int// Erasing 11xx
	EEOL = 1100; // erase to end of line
	public static final int EBOL = 1101; // erase to beginning of line
	public static final int EEL = 1103; // erase entire line
	public static final int EEOS = 1104; // erase to end of screen
	public static final int EBOS = 1105; // erase to beginning of screen
	public static final int EES = 1106; // erase entire screen

	public static final int BYTEMISSING = 1201; // another byte needed
	public static final int UNRECOGNIZED = 1202; // escape match missed

	public static final int HANDLED = 1305;

	/**
	 * Internal UpdateType Constants
	 */
	public static final int LineUpdate = 475, CharacterUpdate = 476,
			ScreenpartUpdate = 477;

	/**
	 * Internal BufferType Constants
	 */
	public static final int EditBuffer = 575, LineEditBuffer = 576;

	/**
	 * Black
	 */
	public static final int BLACK = 30;

	/**
	 * Red
	 */
	public static final int RED = 31;

	/**
	 * Green
	 */
	public static final int GREEN = 32;

	/**
	 * Yellow
	 */
	public static final int YELLOW = 33;

	/**
	 * Blue
	 */
	public static final int BLUE = 34;

	/**
	 * Magenta
	 */
	public static final int MAGENTA = 35;

	/**
	 * Cyan
	 */
	public static final int CYAN = 36;

	/**
	 * White
	 */
	public static final int WHITE = 37;

	/**
	 * CRLF (defining carriage+linebreak which is obligation)
	 */
	public static final String CRLF = "\r\n";
	
	public static final int[] Set = {
		ESCAPE,
		BEL,
		BS,
		DEL,
		CR,
		LF,
		FCOLOR,
		BCOLOR,
		STYLE,
		RESET,
		BOLD,
		BOLD_OFF,
		ITALIC,
		ITALIC_OFF,
		BLINK,
		BLINK_OFF,
		UNDERLINED,
		UNDERLINED_OFF,
		REVERSE,
		REVERSE_OFF,
		DEVICERESET,
		LINEWRAP,
		NOLINEWRAP,
		SPACE,
		UP,
		DOWN,
		RIGHT,
		LEFT,
		TABULATOR,
		DELETE,
		BACKSPACE,
		COLORINIT,
		LOGOUTREQUEST,
		BLACK,
		RED,
		GREEN,
		YELLOW,
		BLUE,
		MAGENTA,
		CYAN,
	};
}

