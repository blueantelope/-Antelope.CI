// com.antelope.ci.bus.server.portal.TelnetTerminal.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.shell.util;

import com.antelope.ci.bus.common.NetVTKey;

/**
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午1:13:17
 */
public class Terminal {

	// Associations
	protected Colorizer m_Colorizer;

	/**
	 * Constructs an instance with an associated colorizer.
	 */
	public Terminal() {
		m_Colorizer = Colorizer.getReference();
	}// constructor

	public int translateControlCharacter(int c) {

		switch (c) {
		case DEL:
			return NetVTKey.DELETE;
		case BS:
			return NetVTKey.BACKSPACE;
		case HT:
			return NetVTKey.TABULATOR;
		case ESC:
			return NetVTKey.ESCAPE;
		case SGR:
			return NetVTKey.COLORINIT;
		case EOT:
			return NetVTKey.LOGOUTREQUEST;
		default:
			return c;
		}
	}// translateControlCharacter

	public int translateEscapeSequence(int[] buffer) {
		try {
			if (buffer[0] == LSB) {
				switch (buffer[1]) {
				case A:
					return NetVTKey.UP;
				case B:
					return NetVTKey.DOWN;
				case C:
					return NetVTKey.RIGHT;
				case D:
					return NetVTKey.LEFT;
				default:
					break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return NetVTKey.BYTEMISSING;
		}

		return NetVTKey.UNRECOGNIZED;
	}// translateEscapeSequence

	public byte[] getCursorMoveSequence(int direction, int times) {
		byte[] sequence = null;

		if (times == 1) {
			sequence = new byte[3];
		} else {
			sequence = new byte[times * 3];
		}

		for (int g = 0; g < times * 3; g++) {

			sequence[g] = ESC;
			sequence[g + 1] = LSB;
			switch (direction) {
			case NetVTKey.UP:
				sequence[g + 2] = A;
				break;
			case NetVTKey.DOWN:
				sequence[g + 2] = B;
				break;
			case NetVTKey.RIGHT:
				sequence[g + 2] = C;
				break;
			case NetVTKey.LEFT:
				sequence[g + 2] = D;
				break;
			default:
				break;
			}
			g = g + 2;
		}

		return sequence;
	}// getCursorMoveSequence

	public byte[] getCursorPositioningSequence(int[] pos) {

		byte[] sequence = null;

		if (pos[0] == NetVTKey.HOME[0] && pos[1] == NetVTKey.HOME[1]) {
			sequence = new byte[3];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = H;
		} else {
			// first translate integer coords into digits
			byte[] rowdigits = translateIntToDigitCodes(pos[0]);
			byte[] columndigits = translateIntToDigitCodes(pos[1]);
			int offset = 0;
			// now build up the sequence:
			sequence = new byte[4 + rowdigits.length + columndigits.length];
			sequence[0] = ESC;
			sequence[1] = LSB;
			// now copy the digit bytes
			System.arraycopy(rowdigits, 0, sequence, 2, rowdigits.length);
			// offset is now 2+rowdigits.length
			offset = 2 + rowdigits.length;
			sequence[offset] = SEMICOLON;
			offset++;
			System.arraycopy(columndigits, 0, sequence, offset,
					columndigits.length);
			offset = offset + columndigits.length;
			sequence[offset] = H;
		}
		return sequence;
	}// getCursorPositioningSequence

	public byte[] getEraseSequence(int eraseFunc) {

		byte[] sequence = null;

		switch (eraseFunc) {
		case NetVTKey.EEOL:
			sequence = new byte[3];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = LE;
			break;
		case NetVTKey.EBOL:
			sequence = new byte[4];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = 49; // Ascii Code of 1
			sequence[3] = LE;
			break;
		case NetVTKey.EEL:
			sequence = new byte[4];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = 50; // Ascii Code 2
			sequence[3] = LE;
			break;
		case NetVTKey.EEOS:
			sequence = new byte[3];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = SE;
			break;
		case NetVTKey.EBOS:
			sequence = new byte[4];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = 49; // Ascii Code of 1
			sequence[3] = SE;
			break;
		case NetVTKey.EES:
			sequence = new byte[4];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = 50; // Ascii Code of 2
			sequence[3] = SE;
			break;
		default:
			break;
		}

		return sequence;
	}// getEraseSequence

	public byte[] getSpecialSequence(int function) {

		byte[] sequence = null;

		switch (function) {
		case NetVTKey.STORECURSOR:
			sequence = new byte[2];
			sequence[0] = ESC;
			sequence[1] = 55; // Ascii Code of 7
			break;
		case NetVTKey.RESTORECURSOR:
			sequence = new byte[2];
			sequence[0] = ESC;
			sequence[1] = 56; // Ascii Code of 8
			break;
		case NetVTKey.DEVICERESET:
			sequence = new byte[2];
			sequence[0] = ESC;
			sequence[1] = 99; // Ascii Code of c
			break;
		case NetVTKey.LINEWRAP:
			sequence = new byte[4];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = 55; // Ascii code of 7
			sequence[3] = 104; // Ascii code of h
			break;
		case NetVTKey.NOLINEWRAP:
			sequence = new byte[4];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = 55; // Ascii code of 7
			sequence[3] = 108; // Ascii code of l
			break;
		}
		return sequence;
	}// getSpecialSequence

	public byte[] getGRSequence(int type, int param) {

		byte[] sequence = new byte[0];
		int offset = 0;

		switch (type) {
		case NetVTKey.FCOLOR:
		case NetVTKey.BCOLOR:
			byte[] color = translateIntToDigitCodes(param);
			sequence = new byte[3 + color.length];

			sequence[0] = ESC;
			sequence[1] = LSB;
			// now copy the digit bytes
			System.arraycopy(color, 0, sequence, 2, color.length);
			// offset is now 2+color.length
			offset = 2 + color.length;
			sequence[offset] = 109; // ASCII Code of m
			break;

		case NetVTKey.STYLE:
			byte[] style = translateIntToDigitCodes(param);
			sequence = new byte[3 + style.length];

			sequence[0] = ESC;
			sequence[1] = LSB;
			// now copy the digit bytes
			System.arraycopy(style, 0, sequence, 2, style.length);
			// offset is now 2+style.length
			offset = 2 + style.length;
			sequence[offset] = 109; // ASCII Code of m
			break;

		case NetVTKey.RESET:
			sequence = new byte[5];
			sequence[0] = ESC;
			sequence[1] = LSB;
			sequence[2] = 52; // ASCII Code of 4
			sequence[3] = 56; // ASCII Code of 8
			sequence[4] = 109; // ASCII Code of m
			break;
		}

		return sequence;
	}// getGRsequence

	public byte[] getScrollMarginsSequence(int topmargin, int bottommargin) {

		byte[] sequence = new byte[0];

		if (supportsScrolling()) {
			// first translate integer coords into digits
			byte[] topdigits = translateIntToDigitCodes(topmargin);
			byte[] bottomdigits = translateIntToDigitCodes(bottommargin);
			int offset = 0;
			// now build up the sequence:
			sequence = new byte[4 + topdigits.length + bottomdigits.length];
			sequence[0] = ESC;
			sequence[1] = LSB;
			// now copy the digit bytes
			System.arraycopy(topdigits, 0, sequence, 2, topdigits.length);
			// offset is now 2+topdigits.length
			offset = 2 + topdigits.length;
			sequence[offset] = SEMICOLON;
			offset++;
			System.arraycopy(bottomdigits, 0, sequence, offset,
					bottomdigits.length);
			offset = offset + bottomdigits.length;
			sequence[offset] = r;
		}

		return sequence;
	}// getScrollMarginsSequence

	public String format(String str) {
		return m_Colorizer.colorize(str, supportsSGR(), false);
	}// format

	public String formatBold(String str) {
		return m_Colorizer.colorize(str, supportsSGR(), true);
	}// formatBold

	public byte[] getInitSequence() {
		byte[] sequence = new byte[0];

		return sequence;
	}// getInitSequence

	public int getAtomicSequenceLength() {
		return 2;
	}// getAtomicSequenceLength

	/**
	 * Translates an integer to a byte sequence of its digits.<br>
	 * 
	 * @param in
	 *            integer to be translated.
	 * @return the byte sequence representing the digits.
	 */
	public byte[] translateIntToDigitCodes(int in) {
		return Integer.toString(in).getBytes();
	}// translateIntToDigitCodes

	public boolean supportsSGR() {
		return true;
	}

	public boolean supportsScrolling() {
		return true;
	}

	// Constants

	/**
	 * <b>End of transmission</b><br>
	 * Ctrl-d, which flags end of transmission, or better said a client logout
	 * request.
	 */
	public static final byte EOT = 4;

	/**
	 * <b>BackSpace</b><br>
	 * The ANSI defined byte code of backspace.
	 */
	public static final byte BS = 8;

	/**
	 * <b>Delete</b><br>
	 * The ANSI defined byte code of delete.
	 */
	public static final byte DEL = 127;

	/**
	 * <b>Horizontal Tab</b><br>
	 * The ANSI defined byte code of a horizontal tabulator.
	 */
	public static final byte HT = 9;

	/**
	 * <b>FormFeed</b><br>
	 * The ANSI defined byte code of a form feed.
	 */
	public static final byte FF = 12;

	/**
	 * <b>SGR Input Key</b><br>
	 * Ctrl-a as defined byte code. It might be of interest to support graphics
	 * rendition in edit mode, for the user to create marked up (i.e. formatted)
	 * input for the application context.
	 */
	public static final byte SGR = 1;

	/**
	 * <b>Cancel</b><br>
	 * The ANSI defined byte code for cancelling an escape sequence.
	 */
	public static final byte CAN = 24;

	/**
	 * <b>Escape</b><br>
	 * The ANSI definde byte code of escape.
	 */
	public static final byte ESC = 27;

	/**
	 * <b>[ Left Square Bracket</b><br>
	 * The ANSI defined byte code of a left square bracket, as used in escape
	 * sequences.
	 */
	public static final byte LSB = 91;

	/**
	 * <b>; Semicolon</b><br>
	 * The ANSI defined byte code of a semicolon, as used in escape sequences.
	 */
	public static final byte SEMICOLON = 59;

	/**
	 * <b>A (UP)</b><br>
	 * The byte code of A, as used in escape sequences for cursor up.
	 */
	public static final byte A = 65;

	/**
	 * <b>B (DOWN)</b><br>
	 * The byte code of B, as used in escape sequences for cursor down.
	 */
	public static final byte B = 66;

	/**
	 * <b>C (RIGHT)</b><br>
	 * The byte code of C, as used in escape sequences for cursor right.
	 */
	public static final byte C = 67;

	/**
	 * <b>D (LEFT)</b><br>
	 * The byte code of D, as used in escape sequences for cursor left.
	 */
	public static final byte D = 68;

	/**
	 * <b>Other characters used in escape sequences.</b>
	 */
	public static final byte E = 69; // for next Line (like CR/LF)
	public static final byte H = 72; // for Home and Positionsetting or f
	public static final byte f = 102;
	public static final byte r = 114;

	/**
	 * <b>Characters needed for erase sequences.</B>
	 */
	public static final byte LE = 75; // K...line erase actions related
	public static final byte SE = 74; // J...screen erase actions related
}
