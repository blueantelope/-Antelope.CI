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

class DogFood(object):
    status = 0
    http_feed = 0
    https_feed = 0

    def __init__(self, tag, **dish):
        self.tag = tag
        self.status = 0
        if dish.http_feed is not None:
            self.http_feed = dish.http_feed
        if dish.https_feed is not None:
            self.https_feed = dish.https_feed

class WatchDog(object):
    def __init__(self, tag=None):
        self.call()
        self.wdf = open(WATCHDOG_PATH, "w")
        if tag is not None:
            self.wdf.write(tag)

    def call(self):
        print "watch dog"
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

if __name__ == "__main__":
    watchdog = WatchDog()
    watchdog.watch()

