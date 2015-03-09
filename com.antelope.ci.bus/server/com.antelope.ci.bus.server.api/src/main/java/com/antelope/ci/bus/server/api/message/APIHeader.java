// com.antelope.ci.bus.server.api.APIHeader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.server.api.message;

import com.antelope.ci.bus.common.exception.CIBusException;
import com.antelope.ci.bus.server.api.base.APIUtil;

/**
 *
 * @author blueantelope
 * @version 0.1
 * @Date 2015年2月22日 下午1:39:29
 */

/*
    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 0 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |      Order    |    Identity   |     Version   |      OID      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |      OID      |                OC             |       OT      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |      BT       |                   BL                          |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |      BL       |                   EXT                         |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                                   EXT                         |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

Order: 1 byte
  byte stream order. 0: native, 1:little endian, 2:big endian, 3: network endian, default 3.
Type: 1 byte
  API type, indicate API type.
Version: 1 byte
  API version.
OID(operation identity): 2 bytes
  indicate a group operations.
OC(operation code): 2 bytes
  define code of operation.
OT(operation type): 1 byte
  define type of operation that include sending and recieving. 0:query(normal), 1:add(exception), 2:delete, 3:edit
BT(body type): 1 byte
  body type. 1: json
BL(body length): 4 bytes
  body length.
EXT(extension): 7 bytes
  extension, for self define header.
*/
public class APIHeader {
	public static final byte HEADER_SIZE = 0x20;
	private static final byte HEADER_EXT_SIZE = 0x07;
	protected short order; // order
	protected short type; // type
	protected short version; // version
	protected int oid; // operation identity
	protected int oc; // operation code
	protected short ot; // operation type
	protected short bt; // body type
	protected long bl; // body length
	protected byte[] ext; // extension

	public APIHeader() {
		super();
		init();
	}

	protected void init() {
		order = 0x03;
		type = 0x00;
		version = 0x01;
		ext = new byte[]
				{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
	}
	
	
	// getter and setter
	public short getOrder() {
		return order;
	}
	public void setOrder(short order) {
		this.order = order;
	}

	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}

	public short getVersion() {
		return version;
	}
	public void setVersion(short version) {
		this.version = version;
	}

	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getOc() {
		return oc;
	}
	public void setOc(int oc) {
		this.oc = oc;
	}

	public short getOt() {
		return ot;
	}
	public void setOt(short ot) {
		this.ot = ot;
	}

	public short getBt() {
		return bt;
	}
	public void setBt(short bt) {
		this.bt = bt;
	}

	public long getBl() {
		return bl;
	}
	public void setBl(long bl) {
		this.bl = bl;
	}

	public byte[] getExt() {
		return ext;
	}
	public void setExt(byte[] ext) {
		this.ext = ext;
	}

	public byte[] toHeaderBytes() {
		byte[] bytes = new byte[20];
		bytes[0] = (byte) order;
		bytes[1] = (byte) type;
		bytes[2] = (byte) version;
		APIUtil.fill2Bytes(oid, bytes, 3);
		APIUtil.fill2Bytes(oc, bytes, 5);
		bytes[7] = (byte) ot;
		bytes[8] = (byte) bt;
		APIUtil.fill4Bytes(bl, bytes, 9);
		APIUtil.fillBytes(ext, bytes, 13, HEADER_EXT_SIZE);
		
		return bytes;
	}
	
	public void fromHeaderBytes(byte[] bytes) throws CIBusException {
		if (bytes.length < HEADER_SIZE)
			throw new CIBusException("", "API header size too small");
		order = bytes[0];
		type = bytes[1];
		version = bytes[2];
		oid = APIUtil.from2Bytes(bytes, 3);
		oc = APIUtil.from2Bytes(bytes, 5);
		ot = bytes[7];
		bt = bytes[8];
		bl = APIUtil.from4Bytes(bytes, 9);
		ext = APIUtil.fromBytes(bytes, 13, HEADER_EXT_SIZE);
	}
}
