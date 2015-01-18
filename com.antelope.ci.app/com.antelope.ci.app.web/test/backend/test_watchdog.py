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
        #FMT = "!BBBHpIBpIBp"
        self.test_stream = struct.pack(FMT, 0, 3, 1, err_len, err_msg, 10, err_len, err_msg, 10, err_len, err_msg)
        print("bytes: " + repr(self.test_stream))

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_from_stream(self):
        self.dogfood.from_stream(self.test_stream)
        print("dogfood: " + str(self.dogfood))

if __name__ == "__main__":
    unittest.main()

