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
from constant import *

class DogFood(object):
    status = 0
    http_feed = 0
    https_feed = 0

    def __init__(self, pid, dish={})
        self.pid = pid
        self.status = 0
        if dish.http_feed is not None:
            self.http_feed = dish.http_feed
        if dish.https_feed is not None:
            self.https_feed = dish.https_feed

class WatchDog(object):
    def __init__(self, pid=None):
        self.wdf = open(constant.WATCHDOG_PATH, "wb")
        if pid is not None:
            self.wdf.write(pid)

    def feed(self, food):


    def shut_eye():
        wdf.close()

