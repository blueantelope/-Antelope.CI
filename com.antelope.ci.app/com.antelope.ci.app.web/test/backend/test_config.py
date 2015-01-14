#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
test unit for config.py.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
import config

class ConfigTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "config.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_http(self):
        print("http server listening: %s/%d" % (config.http.ip, config.http.port))

    def test_https(self):
        print("https server listening: %s/%d" % (config.https.ip, config.https.port))
        print("keyfile is %s" % (config.https.keyfile))
        print("certfile is %s" % (config.https.certfile))

if __name__ == "__main__":
    unittest.main()

