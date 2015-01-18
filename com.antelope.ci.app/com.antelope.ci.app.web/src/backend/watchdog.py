#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
monitor server status.
--
blueantelope@gmail.com
blueantelope 2015-01-15
"""

from __init__ import *
import struct
from constant import WATCHDOG_PATH

"""
DogFood Binary Format. byte(4 bits) as unit

    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |    Version    |    Sequence   |     Status    |Status Err Len |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |Status Err Len |             Status Error Message              |
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
    indicate format of the pack. default 0
Sequence: 1 byte
    byte order. 0: antive, 1:litten endian, 2:bit endian, 3: network endian. default 3
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

FMT = "!BBBHpIBpIBp"
class DogFood(object):
    status = 0
    status_err_msg = ""
    http_seq = 0
    http_err_msg = ""
    https_seq = 0
    https_err_msg = ""

    def __init__(self, pid):
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
            "{pid=%(pid)s,"
            "version=%(version)s,"
            "sequence=%(sequence)s,"
            "status=%(status)s,"
            "status error length=%(status_err_len)s,"
            "status error message=%(status_err_msg)s,"
            "http sequence=%(http_seq)s,"
            "http error length=%(http_err_len)s,"
            "http error message=%(http_err_msg)s,"
            "https sequence=%(https_seq)s,"
            "https error length=%(https_err_len)s,"
            "https error message=%(https_err_msg)s}\n"
        ) % {
            "pid": self.pid,
            "version": 0,
            "sequence": 3,
            "status": self.status,
            "status_err_len": len(self.status_err_msg),
            "status_err_msg": self.status_err_msg,
            "http_seq": self.http_seq,
            "http_err_len": len(self.http_err_msg),
            "http_err_msg": self.http_err_msg,
            "https_seq": self.https_seq,
            "https_err_len": len(self.https_err_msg),
            "https_err_msg": self.https_err_msg
        }
        return s


    def from_stream(self, stream):
        food_list = struct.unpack(FMT, stream)
        self.status = food_list[2]
        if food_list[3] > 0:
            self.status_err_msg = food_list[4]
        self.http_seq = food_list[5]
        if food_list[6] > 0:
            self.http_err_msg = food_list[7]
        self.https_seq = food_list[8]
        if food_list[9] > 0:
            self.https_err_msg = food_list[10]

    def to_stream(self):
        food_list = [0, 3]
        food_list.append(self.status)
        food_list.append(len(self.status_err_msg))
        food_list.append(self.status_err_msg)
        food_list.append(self.http_seq)
        food_list.append(len(self.http_err_msg))
        food_list.append(self.http_err_msg)
        food_list.append(self.https_seq)
        food_list.append(len(self.https_err_msg))
        food_list.append(self.https_err_msg)
        return struct.pack(FMT, food_list)

class FeedDog(object):
    def __init__(self, pid):
        self.wdf = open(WATCHDOG_PATH, "wb")
        self.food = DogFood(self.pid)

    def feed(self, **diet):
        if diet is not None:
            self.food(diet)

    def shut_eye(self):
        if self.wdf:
            self.close()

class WatchDog(object):
    def __init__(self):
        self.call()
        self.wdf = open(WATCHDOG_PATH, "a")
        if tag is not None:
            self.wdf.write(tag)

    def call(self):
        self.born(1)
        os.chdir("/")
        os.setsid()
        os.umask(0)
        self.born(2)

    def born(self, dogs):
        try:
            tag = os.fork()
            if tag > 0:
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

