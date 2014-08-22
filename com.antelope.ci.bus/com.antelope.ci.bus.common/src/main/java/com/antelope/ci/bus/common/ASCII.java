// com.antelope.ci.bus.common.ASCII.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2014-8-22		上午11:30:35 
 */
public enum ASCII {
	// 空字符 
	NUL(0, "NUL", "null"),
	//  标题开始 
	SOH(1, "SOH", "start of heading"),
	// 正文开始
	STX(2, "STX", "start of text"),
	// 正文结束 
	ETX(3, "ETX", "end of text"),
	// 传输结束
	EOT(4, "EOT", "end of transmission"),
	// 请求
	ENQ(5, "ENQ", "enquiry"),
	// 收到通知
	ACK(6, "ACK", "acknowledge"),
	// 响铃
	BEL(7, "BEL", "bell"),
	// 退格
	BS(8, "BS", "backspace"),
	// 水平制表符
	HT(9, "HT", "horizontal tab"),
	// 换行键
	LF(10, "LF", "NL line feed, new line"),
	// 垂直制表符
	VT(11, "VT", "vertical tab"),
	// 换页键
	FF(12, "FF", "NP form feed, new page"),
	// 回车键
	CR(13, "CR", "carriage return"),
	// 不用切换
	SO(14, "SO", "shift out"),
	// 启用切换
	SI(15, "SI", "shift in"),
	// 数据链路转义
	DLE(16, "SI", "data link escape"),
	// 设备控制1
	DC1(17, "DC1", "device control 1"),
	// 设备控制2
	DC2(18, "DC2", "device control 2"),
	// 设备控制3
	DC3(19, "DC3", "device control 3"),
	// 设备控制4
	DC4(20, "DC4", "device control 4"),
	// 拒绝接收
	NAK(21, "NAK", "negative acknowledge"),
	// 同步空闲
	SYN(22, "SYN", "synchronous idle"),
	// 传输块结束
	ETB(23, "ETB", "end of trans. block"),
	// 取消
	CAN(24, "CAN", "cancel"),
	// 介质中断
	EM(25, "EM", "end of medium"),
	// 替补
	SUB(26, "SUB", "substitute"),
	// 溢出
	ESC(27, "ESC", "escape"),
	// 文件分割符 
	FS(28, "FS", "file separator"),
	// 分组符 
	GS(29, "GS", "group separator"),
	// 记录分离符
	RS(30, "RS", "record separator"),
	// 单元分隔符 
	US(31, "US", "unit separator"),
	// 空格 
	SPACE(32, "space", "space"),
	/*
	 * 以下是各种符号，以_S开头
	 */
	_S0(33, "!", "!"),
	_S1(34, "\"", "\""),
	_S2(35, "#", "#"),
	_S3(36, "$", "$"),
	_S4(37, "%", "%"),
	_S5(38, "&", "&"),
	_S6(39, "'", "'"),
	_S7(40, "(", "("),
	_S8(41, ")", ")"),
	_S9(42, "*", "*"),
	_S10(43, "+", "+"),
	_S11(44, ",", ","),
	_S12(45, "-", "-"),
	_S13(46, ".", "."),
	_S14(47, "/", "/"),
	/*
	 * 以下为数字，以_N开头
	 */
	_N0(48, "0", "0"),
	_N1(49, "1", "1"),
	_N2(50, "2", "2"),
	_N3(51, "3", "3"),
	_N4(52, "4", "4"),
	_N5(53, "5", "5"),
	_N6(54, "6", "6"),
	_N7(55, "7", "7"),
	_N8(56, "8", "8"),
	_N9(57, "9", "9"),
	/*
	 * 以下是各种符号，以_S开头
	 */
	_S15(58, ":", ":"),
	_S16(59, ";", ";"),
	_S17(60, "<", "<"),
	_S18(61, "=", "="),
	_S19(62, ">", ">"),
	_S20(63, "?", "?"),
	_S21(64, "@", "@"),
	/*
	 * 以下为大写字母
	 */
	A(65, "A", "A"),
	B(66, "B", "B"),
	C(67, "C", "C"),
	D(68, "D", "D"),
	E(69, "E", "E"),
	F(70, "F", "F"),
	G(71, "G", "G"),
	H(72, "H", "H"),
	I(73, "I", "I"),
	J(74, "J", "J"),
	K(75, "K", "K"),
	L(76, "L", "L"),
	M(77, "M", "M"),
	N(78, "N", "N"),
	O(79, "O", "O"),
	P(80, "P", "P"),
	Q(81, "Q", "Q"),
	R(82, "R", "R"),
	S(83, "S", "S"),
	T(84, "T", "T"),
	U(85, "U", "U"),
	V(86, "V", "V"),
	W(87, "W", "W"),
	X(88, "X", "X"),
	Y(89, "Y", "Y"),
	Z(90, "Z", "Z"),
	/*
	 * 以下是各种符号，以_S开头
	 */
	_S22(91, "[", "["),
	_S23(92, "\\", "\\"),
	_S24(93, "]", "]"),
	_S25(94, "^", "^"),
	_S26(95, "_", "_"),
	_S27(96, "`", "`"),
	/*
	 * 以下为小写字母
	 */
	a(97, "a", "a"),
	b(98, "b", "b"),
	c(99, "c", "c"),
	d(100, "d", "d"),
	e(101, "e", "e"),
	f(102, "f", "f"),
	g(103, "g", "g"),
	h(104, "h", "h"),
	i(105, "i", "i"),
	j(106, "j", "j"),
	k(107, "k", "k"),
	l(108, "l", "l"),
	m(109, "m", "m"),
	n(110, "n", "n"),
	o(111, "o", "o"),
	p(112, "p", "p"),
	q(113, "q", "q"),
	r(114, "r", "r"),
	s(115, "s", "s"),
	t(116, "t", "t"),
	u(117, "u", "u"),
	v(118, "v", "v"),
	w(119, "w", "w"),
	x(120, "x", "x"),
	y(121, "y", "y"),
	z(122, "z", "z"),
	/*
	 * 以下是各种符号，以_S开头
	 */
	_S28(123, "{", "{"),
	_S29(124, "|", "|"),
	_S30(125, "}", "}"),
	_S31(126, "~", "~"),
	// 删除 
	DEL(127, "DEL", "delete");
	
	private int dec;
	private String Char;
	private String desc;
	
	private ASCII(int dec, String Char, String desc) {
		this.dec = dec;
		this.Char = Char;
		this.desc = desc;
	}

	public int getDec() {
		return dec;
	}

	public String getChar() {
		return Char;
	}

	public String getDesc() {
		return desc;
	}
	
	@Override public String toString() {
		return ((char) dec)  + " : " + desc;
	}
	
	public static void printHex() {
		ASCII[] as = ASCII.values();
		byte[] abuf = new byte[as.length];
		int n = 0;
		for (ASCII ascii : as)
			abuf[n++] = (byte) ascii.getDec();
		System.out.print(StringUtil.toHexString(abuf));
	}
	
	public static ASCII fromDec(int dec) throws CIBusException {
		for (ASCII ascii : ASCII.values()) {
			if (ascii.getDec() == dec)
				return ascii;
		}
		
		throw new CIBusException("", "unkown ascii Dec");
	}
}

