#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
test unit for watchdog.py.
--
blueantelope@gmail.com
blueantelope 2015-01-18
"""

from __init__ import *
import struct
from watchdog import *

class WatchdogTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "watchdog.py")
        self.dogfood = DogFood(9426)
        err_msg = "error"
        err_len = len(err_msg)
        fmt = "!BBHBH" + str(err_len) + "sIH" + str(err_len) + "sIH" + str(err_len) + "s"
        self.test_byte = struct.pack(fmt, 0, 3, 9426, 1, err_len, err_msg, 10, err_len, err_msg, 10, err_len, err_msg)
        print("bytes: %s" % (repr(self.test_byte)))

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_from_byte(self):
        self.dogfood.from_byte(self.test_byte)
        print("dogfood: %s" % (str(self.dogfood)))

    def test_to_byte(self):
        bytes = self.dogfood.to_byte()
        print("to byte: %s" % (repr(bytes)))

    def test_to_from(self):
        self.test_from_byte()
        self.test_to_byte()

    def test_feed(self):
        feeddog = FeedDog(9426)
        feeddog.feed()

    def test_watch(self):
        watchdog = WatchDog()
        print watchdog.dogfood

if __name__ == "__main__":
    unittest.main()

