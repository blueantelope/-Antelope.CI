// com.antelope.ci.bus.server.api.APIHeader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2015, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.server.api;

import java.io.Serializable;


/**
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2015年2月22日		下午1:39:29 
 */

/*
    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
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
EXT(extension): 17 bytes
  extension, for self define header.
*/
public class APIHeader {
  protected byte order; // order
  protected byte type; // identity
  protected byte version; // version
  protected short oid; // operation identity
  protected short oc; // operation code
  protected byte ot; // operation type
  protected byte bt; // body type
  protected int bl; // body length
  protected byte[] ext; // extension

  public APIHeader() {
    super();
    init();
  }

  protected void init() {
    order = 0x03;
    type = 0x00;
    version = 0x01;
    ext = new byte[7];
  }


}

