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
   |    Sequence   |     Version   |            pid                |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |    Status     |       Status Error Length     | Status Err Msg|
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                        Http Sequence                          |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |        Http Error Length      |       Http Error Message      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                       Https Sequence                          |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |        Https Error Length     |       Https Error Message     |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

Sequence: 1 byte
  byte stream order. 0: antive, 1:litten endian, 2:bit endian, 3: network endian. default 3
Version: 1 byte
  API version.
Operation Id: 2 bytes
  increment id of opreration.
Operation Code: 1 byte
  define code of operation that include sending and recieving. 0:query(normal), 1:add(exception), 2:delete, 3:edit
Body Type: 1 byte
  body cotent type. 1: json
Body Length: 4 bytes
  body content length.
*/
public class APIHeader implements Serializable {
  protected byte sequence;
  protected byte version;
  protected short oid; // operation id
  protected byte oc; // operation code
  protected byte bt; // body type
  protected int bl; // body length

  public APIHeader() {
    super();
    init();
  }

  protected void init() {
    sequence = 0x03;
    version = 0x01;
  }


}

