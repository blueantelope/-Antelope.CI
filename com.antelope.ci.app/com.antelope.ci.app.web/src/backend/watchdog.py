#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
monitor server status.
--
blueantelope@gmail.com
blueantelope 2015-01-15
"""

from __init__ import *
import logging as logger
import struct
from constant import WATCHDOG_PATH

"""
DogFood Binary Format. byte(4 bits) as unit

    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |    Version    |    Sequence   |            pid                |
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


Version: 1 byte
Version: 1 byte
    indicate format of the pack. default 0
    indicate format of the pack. default 0
Sequence: 1 byte
    byte order. 0: antive, 1:litten endian, 2:bit endian, 3: network endian. default 3
pid: 2 bytes
    process id of operation system. max 65536
Status: 1 byte
    application status. 0:normal, 1:warning, 2:exception, 3:error
Status Err Len: 2 Bytes
    length of status error message length.
Status Error Message: fixed, according to (Status Error Length)
    status error message. error includes status(1, 2, 3)
Http Sequence: 4 bytes
    http server sequence. it is incremental.
Http Error Length: 4 bytes
    length of http server error message.
Http Error Message: fixed, according to (Http Error Length)
    http error message.
Https Sequence: 4 bytes
    https server sequence. it is incremental.
Https Error Length: 4 bytes
    length of https server error message.
Https Error Message: fixed, according to (Https Error Length)
    https error message.

"""

logger.basicConfig(level=logging.DEBUG,
        format="%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s",
        datefmt="%Y-%m-%d %H:%M:%S")

class DogFood(object):
    status = 0
    status_err_msg = ""
    http_seq = 0
    http_err_msg = ""
    https_seq = 0
    https_err_msg = ""

    def __init__(self, pid=None):
        self.pid = pid

    def __call__(self, **diet):
        if diet.status is not None:
            self.status = diet.status
        if diet.status_err_msg is not None:
            self.status_err_msg = diet.status_err_msg
        if diet.http_seq is not None:
            self.http_seq = diet.http_seq
        if diet.http_err_msg is not None:
            self.http_err_msg= diet.http_err_msg
        if diet.https_seq is not None:
            self.https_seq = diet.https_seq
        if diet.https_err_msg is not None:
            self.https_err_msg= diet.https_err_msg

    def __str__(self):
        s = (
            "{version = %(version)s, "
            "sequence = %(sequence)s, "
            "pid = %(pid)s, "
            "status = %(status)s, "
            "status_err_msg = \"%(status_err_msg)s\", "
            "http_seq = %(http_seq)s, "
            "http_err_msg = \"%(http_err_msg)s\", "
            "https_seq = %(https_seq)s, "
            "https_err_msg = \"%(https_err_msg)s\"}\n"
        ) % {
            "version": 0,
            "sequence": 3,
            "pid": self.pid,
            "status": self.status,
            "status_err_msg": self.status_err_msg,
            "http_seq": self.http_seq,
            "http_err_msg": self.http_err_msg,
            "https_seq": self.https_seq,
            "https_err_msg": self.https_err_msg
        }
        return s

    def byte_to_string(self, byte, offset, length):
        fmt = "!" + str(length) + "s"
        s = struct.unpack_from(fmt, byte, offset)[0]
        return s

    def from_server_byte(self, byte, offset):
        fmt = "!IH"
        food_list = struct.unpack_from(fmt, byte, offset)
        server_seq = food_list[0]
        server_err_len = food_list[1]
        offset = offset + 6
        server_err_msg = ""
        if server_err_len > 0:
            server_err_msg = self.byte_to_string(byte, offset, server_err_len)
            offset = offset + server_err_len
        return {"seq":server_seq, "err_msg":server_err_msg}

    def from_byte(self, byte):
        fmt = "!HBH"
        offset = 2
        food_list = struct.unpack_from(fmt, byte, offset)
        self.pid = food_list[0]
        self.status = food_list[1]
        status_err_len = food_list[2]
        offset = 7
        if status_err_len > 0:
            self.status_err_msg = self.byte_to_string(byte, offset, status_err_len)
            offset = offset + status_err_len

        server_info = self.from_server_byte(byte, offset)
        self.http_seq = server_info["seq"]
        self.http_err_msg = server_info["err_msg"]

        server_info = self.from_server_byte(byte, offset)
        self.https_seq = server_info["seq"]
        self.https_err_msg = server_info["err_msg"]

    def format_msg(self, msg):
        msg_len = len(msg)
        if msg_len > 0:
            return str(msg_len) + "s"
        return ""

    def to_byte(self):
        fmt = (
            "!BBHBH%(status_err_len)s"
            "IH%(http_err_len)s"
            "IH%(https_err_len)s"
        ) % {
            "status_err_len": self.format_msg(self.status_err_msg),
            "http_err_len": self.format_msg(self.http_err_msg),
            "https_err_len": self.format_msg(self.https_err_msg)
        }
        logger.debug("to_byte() fmt: " + fmt)
        food_list = [0, 3]
        food_list.append(self.pid)
        food_list.append(self.status)
        status_err_len = len(self.status_err_msg)
        food_list.append(status_err_len)
        if status_err_len > 0:
            food_list.append(self.status_err_msg)
        food_list.append(self.http_seq)
        http_err_len = len(self.http_err_msg)
        food_list.append(http_err_len)
        if http_err_len > 0:
            food_list.append(self.http_err_msg)
        food_list.append(self.https_seq)
        https_err_len = len(self.https_err_msg)
        food_list.append(https_err_len)
        if https_err_len > 0:
            food_list.append(self.https_err_msg)
        logger.debug("to_byte() food_list: " + str(food_list))
        return struct.pack(fmt, *food_list)

class FeedDog(object):
    def __init__(self, pid):
        self.wdf = open(WATCHDOG_PATH, "wb")
        self.dogfood = DogFood(pid)
        self.wdf.write(self.dogfood.to_byte())

    def feed(self, **diet):
        if diet is not None:
            self.dogfood(diet)

    def shut_eye(self):
        if self.wdf:
            self.close()

class WatchDog(object):
    def __init__(self):
        self.call()
        self.wdf = open(WATCHDOG_PATH, "rb")
        self.dogfood = DogFood()
        rawfood = self.wdf.read()
        self.dogfood.from_byte(rawfood)
        logger.debug("WatchDog dogfood is: %s" % (str(self.dogfood)))

    def call(self):
        self.born(1)
        os.chdir("/")
        os.setsid()
        os.umask(0)
        self.born(2)

    def born(self, dogs):
        try:
            pid = os.fork()
            if pid > 0:
                sys.exit(0)
        except OSError, e:
            dog_tag = "elder"
            if dogs == 1:
                dog_tag = "litter"
            sys.stderr.write("born " + dog_tag + " dog.")
            sys.exit(1)

    def watch(self):
        while True:
            time.sleep(5)

    def shut_eye(self):
        if wdf:
            wdf.close()

