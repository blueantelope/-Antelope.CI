// com.antelope.ci.bus.server.portal.TerminalIO.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.shell.util;

import static com.antelope.ci.bus.common.NetVTKey.BCOLOR;
import static com.antelope.ci.bus.common.NetVTKey.BEL;
import static com.antelope.ci.bus.common.NetVTKey.BLINK;
import static com.antelope.ci.bus.common.NetVTKey.BLINK_OFF;
import static com.antelope.ci.bus.common.NetVTKey.BOLD;
import static com.antelope.ci.bus.common.NetVTKey.BOLD_OFF;
import static com.antelope.ci.bus.common.NetVTKey.COLORINIT;
import static com.antelope.ci.bus.common.NetVTKey.CRLF;
import static com.antelope.ci.bus.common.NetVTKey.DEVICERESET;
import static com.antelope.ci.bus.common.NetVTKey.DOWN;
import static com.antelope.ci.bus.common.NetVTKey.ESCAPE;
import static com.antelope.ci.bus.common.NetVTKey.FCOLOR;
import static com.antelope.ci.bus.common.NetVTKey.ITALIC;
import static com.antelope.ci.bus.common.NetVTKey.ITALIC_OFF;
import static com.antelope.ci.bus.common.NetVTKey.LEFT;
import static com.antelope.ci.bus.common.NetVTKey.LINEWRAP;
import static com.antelope.ci.bus.common.NetVTKey.LOGOUTREQUEST;
import static com.antelope.ci.bus.common.NetVTKey.NOLINEWRAP;
import static com.antelope.ci.bus.common.NetVTKey.RED;
import static com.antelope.ci.bus.common.NetVTKey.RESET;
import static com.antelope.ci.bus.common.NetVTKey.REVERSE;
import static com.antelope.ci.bus.common.NetVTKey.REVERSE_OFF;
import static com.antelope.ci.bus.common.NetVTKey.RIGHT;
import static com.antelope.ci.bus.common.NetVTKey.STYLE;
import static com.antelope.ci.bus.common.NetVTKey.UNDERLINED;
import static com.antelope.ci.bus.common.NetVTKey.UNDERLINED_OFF;
import static com.antelope.ci.bus.common.NetVTKey.UP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.NetVTKey;

/**
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午1:10:12
 */
public class TerminalIO {
	private static Logger log = Logger.getLogger(TerminalIO.class);
	private IO m_io; // low level I/O

	private ConnectionData m_ConnectionData; // holds data of the connection
	private Terminal m_Terminal = new Terminal(); // active terminal
																// object

	// Members
	private boolean m_AcousticSignalling; // flag for accoustic signalling
	private boolean m_Autoflush; // flag for autoflushing mode
	private boolean m_ForceBold; // flag for forcing bold output
	private boolean m_LineWrapping;
	
	public int available() throws IOException {
		return m_io.available();
	}

	public int skip() throws IOException {
		return m_io.skip();
	}

	public IO getM_TelnetIO() {
		return m_io;
	}

	/**
	 * Constructor of the TerminalIO class.
	 * 
	 * @param con
	 *            Connection the instance will be working for
	 */
	public TerminalIO(InputStream in, OutputStream out,
			ConnectionData connectionData) {
		m_AcousticSignalling = true;
		m_Autoflush = true;

		try {
			// create a new telnet io
			m_io = new IO(in, out, connectionData);
			m_ConnectionData = connectionData;
		} catch (Exception ex) {
			// handle, at least log
		}

		// set default terminal
		try {
			setDefaultTerminal();
		} catch (Exception ex) {
			log.error("TerminalIO()", ex);
			throw new RuntimeException();
		}
	}// constructor

	/************************************************************************
	 * Visible character I/O methods *
	 ************************************************************************/
	
	/**
	 * Read a single character and take care for terminal function calls.
	 * 
	 * @return <ul>
	 *         <li>character read
	 *         <li>IOERROR in case of an error
	 *         <li>DELETE,BACKSPACE,TABULATOR,ESCAPE,COLORINIT,LOGOUTREQUEST
	 *         <li>UP,DOWN,LEFT,RIGHT
	 *         </ul>
	 */
	public synchronized int read() throws IOException {
		int i = m_io.readSsh();
		// translate possible control sequences
		i = m_Terminal.translateControlCharacter(i);

		// catch & fire a logoutrequest event
		if (i == LOGOUTREQUEST) {
			// m_Connection.processConnectionEvent(new
			// ConnectionEvent(m_Connection,
			// ConnectionEvent.CONNECTION_LOGOUTREQUEST));
			i = NetVTKey.HANDLED;
		} else if (i > 256 && i == ESCAPE) {
			// translate an incoming escape sequence
			i = handleEscapeSequence(i);
		}

		// return i holding a char or a defined special key
		return i;
	}// read
	
	public synchronized int readForTelnet() throws IOException {
		int i = m_io.read();
		// translate possible control sequences
		i = m_Terminal.translateControlCharacter(i);

		// catch & fire a logoutrequest event
		if (i == LOGOUTREQUEST) {
			// m_Connection.processConnectionEvent(new
			// ConnectionEvent(m_Connection,
			// ConnectionEvent.CONNECTION_LOGOUTREQUEST));
			i = NetVTKey.HANDLED;
		} else if (i > 256 && i == ESCAPE) {
			// translate an incoming escape sequence
			i = handleEscapeSequence(i);
		}

		// return i holding a char or a defined special key
		return i;
	}// read

	public synchronized void write(byte b) throws IOException {
		m_io.write(b);
		if (m_Autoflush) {
			flush();
		}
	}// write
	
	public synchronized void write(byte[] bs) throws IOException {
		for (byte b : bs)
			write(b);
	}// write(byte[])

	public synchronized void write(char ch) throws IOException {
		m_io.write(ch);
		if (m_Autoflush) {
			flush();
		}
	}// write(char)
	
	public synchronized void write(char[] chs) throws IOException {
		for (char ch : chs)
			write(ch);
	}// write(char[])

	public synchronized void write(String str) throws IOException {
		if (m_ForceBold) {
			m_io.write(m_Terminal.formatBold(str));
		} else {
			m_io.write(m_Terminal.format(str));
		}
		if (m_Autoflush) {
			flush();
		}
	}// write(String)

	public synchronized void println(String str) throws IOException {
		write(str + CRLF);
	}

	public synchronized void println() throws IOException {
		write(CRLF);
	}

	public synchronized void error(String str) throws IOException {
		setForegroundColor(RED);
		write(str);
		setForegroundColor(COLORINIT);
	}

	public synchronized void errorln(String str) throws IOException {
		error(str + CRLF);
	}

	/*** End of Visible character I/O methods ******************************/

	/**
	 * *********************************************************************
	 * Erase methods *
	 * **********************************************************************
	 */

	public synchronized void eraseToEndOfLine() throws IOException {
		doErase(NetVTKey.EEOL);
	}// eraseToEndOfLine

	public synchronized void eraseToBeginOfLine() throws IOException {
		doErase(NetVTKey.EBOL);
	}// eraseToBeginOfLine

	public synchronized void eraseLine() throws IOException {
		doErase(NetVTKey.EEL);
	}// eraseLine

	public synchronized void eraseToEndOfScreen() throws IOException {
		doErase(NetVTKey.EEOS);
	}// eraseToEndOfScreen

	public synchronized void eraseToBeginOfScreen() throws IOException {
		doErase(NetVTKey.EBOS);
	}// eraseToBeginOfScreen

	public synchronized void eraseScreen() throws IOException {
		doErase(NetVTKey.EES);
	}// eraseScreen

	private synchronized void doErase(int funcConst) throws IOException {

		m_io.write(m_Terminal.getEraseSequence(funcConst));
		if (m_Autoflush) {
			flush();
		}
	}// erase

	/*** End of Erase methods **********************************************/

	/**
	 * *********************************************************************
	 * Cursor related methods *
	 * **********************************************************************
	 */

	public synchronized void moveCursor(int direction, int times)
			throws IOException {

		m_io.write(m_Terminal.getCursorMoveSequence(direction, times));
		if (m_Autoflush) {
			flush();
		}
	}// moveCursor

	public synchronized void moveLeft(int times) throws IOException {
		moveCursor(LEFT, times);
	}// moveLeft

	public synchronized void moveRight(int times) throws IOException {
		moveCursor(RIGHT, times);
	}// moveRight

	public synchronized void moveUp(int times) throws IOException {
		moveCursor(UP, times);
	}// moveUp

	public synchronized void moveDown(int times) throws IOException {
		moveCursor(DOWN, times);
	}// moveDown

	public synchronized void setCursor(int row, int col) throws IOException {
		int[] pos = new int[2];
		pos[0] = row;
		pos[1] = col;
		m_io.write(m_Terminal.getCursorPositioningSequence(pos));
		if (m_Autoflush) {
			flush();
		}
	}// setCursor

	public synchronized void homeCursor() throws IOException {
		m_io.write(m_Terminal.getCursorPositioningSequence(NetVTKey.HOME));
		if (m_Autoflush) {
			flush();
		}
	}// homeCursor
	
	public synchronized byte[] getCursor() {
		byte[] cursor = m_Terminal.getSpecialSequence(NetVTKey.STORECURSOR);
		return cursor;
	}

	public synchronized void storeCursor() throws IOException {
		byte[] sequence = new byte[3];
		sequence[0] = Terminal.ESC;
		sequence[1] = Terminal.LSB;
		sequence[2] = 's';
		m_io.write(sequence);
//		m_io.write(m_Terminal.getSpecialSequence(STORECURSOR));
	}// store Cursor

	public synchronized void restoreCursor() throws IOException {
		byte[] sequence = new byte[3];
		sequence[0] = Terminal.ESC;
		sequence[1] = Terminal.LSB;
		sequence[2] = 'u';
		m_io.write(sequence);
//		m_io.write(m_Terminal.getSpecialSequence(RESTORECURSOR));
	}// restore Cursor

	/*** End of cursor related methods **************************************/

	/**
	 * *********************************************************************
	 * Special terminal function methods *
	 * **********************************************************************
	 */

	public synchronized void setSignalling(boolean bool) {
		m_AcousticSignalling = bool;
	}// setAcousticSignalling

	public synchronized boolean isSignalling() {
		return m_AcousticSignalling;
	}// isAcousticSignalling

	/**
	 * Method to write the NVT defined BEL onto the stream. If signalling is
	 * off, the method simply returns, without any action.
	 */
	public synchronized void bell() throws IOException {
		if (m_AcousticSignalling) {
			m_io.write(BEL);
		}
		if (m_Autoflush) {
			flush();
		}
	}// bell

	/**
	 * EXPERIMENTAL, not defined in the interface.
	 */
	public synchronized boolean defineScrollRegion(int topmargin,
			int bottommargin) throws IOException {
		if (m_Terminal.supportsScrolling()) {
			m_io.write(m_Terminal.getScrollMarginsSequence(topmargin,
					bottommargin));
			flush();
			return true;
		} else {
			return false;
		}
	}// defineScrollRegion

	public synchronized void setForegroundColor(int color) throws IOException {
		if (m_Terminal.supportsSGR()) {
			m_io.write(m_Terminal.getGRSequence(FCOLOR, color));
			if (m_Autoflush) {
				flush();
			}
		}
	}// setForegroundColor

	public synchronized void setBackgroundColor(int color) throws IOException {
		if (m_Terminal.supportsSGR()) {
			// this method adds the offset to the fg color by itself
			m_io.write(m_Terminal.getGRSequence(BCOLOR, color + 10));
			if (m_Autoflush) {
				flush();
			}
		}
	}// setBackgroundColor

	public synchronized void setBold(boolean b) throws IOException {
		if (m_Terminal.supportsSGR()) {
			if (b) {
				m_io.write(m_Terminal.getGRSequence(STYLE, BOLD));
			} else {
				m_io.write(m_Terminal.getGRSequence(STYLE, BOLD_OFF));
			}
			if (m_Autoflush) {
				flush();
			}
		}
	}// setBold

	public synchronized void forceBold(boolean b) {
		m_ForceBold = b;
	}// forceBold

	public synchronized void setUnderlined(boolean b) throws IOException {
		if (m_Terminal.supportsSGR()) {
			if (b) {
				m_io.write(m_Terminal.getGRSequence(STYLE, UNDERLINED));
			} else {
				m_io.write(m_Terminal
						.getGRSequence(STYLE, UNDERLINED_OFF));
			}
			if (m_Autoflush) {
				flush();
			}

		}
	}// setUnderlined

	public synchronized void setItalic(boolean b) throws IOException {
		if (m_Terminal.supportsSGR()) {
			if (b) {
				m_io.write(m_Terminal.getGRSequence(STYLE, ITALIC));
			} else {
				m_io.write(m_Terminal.getGRSequence(STYLE, ITALIC_OFF));
			}
			if (m_Autoflush) {
				flush();
			}
		}
	}// setItalic

	public synchronized void setReverse(boolean b) throws IOException {
		if (m_Terminal.supportsSGR()) {
			if (b) {
				m_io.write(m_Terminal.getGRSequence(STYLE, REVERSE));
			} else {
				m_io.write(m_Terminal.getGRSequence(STYLE, REVERSE_OFF));
			}
			if (m_Autoflush) {
				flush();
			}
		}
	}// setReverse

	public synchronized void setBlink(boolean b) throws IOException {
		if (m_Terminal.supportsSGR()) {
			if (b) {
				m_io.write(m_Terminal.getGRSequence(STYLE, BLINK));
			} else {
				m_io.write(m_Terminal.getGRSequence(STYLE, BLINK_OFF));
			}
			if (m_Autoflush) {
				flush();
			}
		}
	}// setItalic

	public synchronized void resetAttributes() throws IOException {
		if (m_Terminal.supportsSGR()) {
			m_io.write(m_Terminal.getGRSequence(RESET, 0));
		}
	}// resetGR

	/*** End of special terminal function methods ***************************/

	/************************************************************************
	 * Auxiliary I/O methods *
	 ************************************************************************/

	/**
	 * Method that parses forward for escape sequences
	 */
	private int handleEscapeSequence(int i) throws IOException {
		if (i == ESCAPE) {
			int[] bytebuf = new int[m_Terminal.getAtomicSequenceLength()];
			bytebuf[0] = i;
			// fill atomic length
			// FIXME: ensure CAN, broken Escapes etc.
			EscapeHandlerThread ehandler = new EscapeHandlerThread(m_io, bytebuf);
			ehandler.start();
			if (!handleEscape(ehandler, bytebuf)) {
				try {
					Thread.sleep(60);
				} catch (InterruptedException e) { }
				if (!handleEscape(ehandler, bytebuf)) {
					ehandler.stop();
					return ESCAPE;
				}
			}
			
			return m_Terminal.translateEscapeSequence(bytebuf);
		}
		if (i == NetVTKey.BYTEMISSING) {
			// FIXME:longer escapes etc...
		}

		return NetVTKey.HANDLED;
	}// handleEscapeSequence
	
	private boolean handleEscape(EscapeHandlerThread ehandler, int[] bytebuf) throws IOException {
		if (ehandler.exception != null) {
			ehandler.stop();
			throw ehandler.exception;
		}
		if (ehandler.finished) {
			bytebuf = ehandler.bytebuf;
			ehandler.stop();
			return true;
		}
		return false;
	}
	
	private static class EscapeHandlerThread extends Thread {
		volatile int[] bytebuf;
		boolean finished;
		private IO io;
		IOException exception;
		
		public EscapeHandlerThread(IO m_io, int[] bytebuf) {
			this.io = m_io;
			this.bytebuf = bytebuf;
			exception = null;
			finished = false;
		}
 		
		@Override public void run() {
			while (true) {
				for (int m = 0; m < bytebuf.length; m++) {
					try {
						bytebuf[m] = io.read();
					} catch (IOException e) {
						exception = e;
						break;
					}
				}
				finished = true;
			}
		}
	}

	/**
	 * Accessor method for the autoflushing mechanism.
	 */
	public boolean isAutoflushing() {
		return m_Autoflush;
	}// isAutoflushing

	public synchronized void resetTerminal() throws IOException {
		m_io.write(m_Terminal.getSpecialSequence(DEVICERESET));
	}

	public synchronized void setLinewrapping(boolean b) throws IOException {
		if (b && !m_LineWrapping) {
			m_io.write(m_Terminal.getSpecialSequence(LINEWRAP));
			m_LineWrapping = true;
			return;
		}
		if (!b && m_LineWrapping) {
			m_io.write(m_Terminal.getSpecialSequence(NOLINEWRAP));
			m_LineWrapping = false;
			return;
		}
	}// setLineWrapping

	public boolean isLineWrapping() {
		return m_LineWrapping;
	}//

	/**
	 * Mutator method for the autoflushing mechanism.
	 */
	public synchronized void setAutoflushing(boolean b) {
		m_Autoflush = b;
	}// setAutoflushing

	/**
	 * Method to flush the Low-Level Buffer
	 */
	public synchronized void flush() throws IOException {
		m_io.flush();
	}// flush (implements the famous iToilet)

	public synchronized void close() {
		m_io.closeOutput();
		m_io.closeInput();
	}// close

	public void closeInput() {
		m_io.closeInput();
	}

	public synchronized void closeOutput() {
		m_io.closeOutput();
	}

	/*** End of Auxiliary I/O methods **************************************/

	/************************************************************************
	 * Terminal management specific methods *
	 ************************************************************************/

	/**
	 * Accessor method to get the active terminal object
	 * 
	 * @return Object that implements Terminal
	 */
	public Terminal getTerminal() {
		return m_Terminal;
	}// getTerminal

	// /**
	// * Sets the default terminal ,which will either be
	// * the negotiated one for the connection, or the systems
	// * default.
	// */
	public void setDefaultTerminal() throws IOException {
		// set the terminal passing the negotiated string
		setTerminal(m_ConnectionData.getNegotiatedTerminalType());
	}// setDefaultTerminal

	/**
	 * Mutator method to set the active terminal object If the String does not
	 * name a terminal we support then the vt100 is the terminal of selection
	 * automatically.
	 * 
	 * @param terminalName
	 *            String that represents common terminal name
	 */
	public void setTerminal(String terminalName) throws IOException {

		// m_Terminal =
		// TerminalManager.getReference().getTerminal(terminalName);
		// Terminal is set we init it....
		initTerminal();
		// debug message
		log.debug("Set terminal to " + m_Terminal.toString());
	}// setTerminal

	/**
	 * Terminal initialization
	 */
	private synchronized void initTerminal() throws IOException {
		m_io.write(m_Terminal.getInitSequence());
		flush();
	}// initTerminal

	/**
	     *
	     */
	public int getRows() {
		return m_ConnectionData.getTerminalRows();
	}// getRows

	/**
	     *
	     */
	public int getColumns() {
		return m_ConnectionData.getTerminalColumns();
	}// getColumns

	/**
	 * Accessor Method for the terminal geometry changed flag
	 */
	public boolean isTerminalGeometryChanged() {
		return m_ConnectionData.isTerminalGeometryChanged();
	}// isTerminalGeometryChanged

	/*** End of terminal management specific methods ***********************/

	/** Constants Declaration **********************************************/
}
