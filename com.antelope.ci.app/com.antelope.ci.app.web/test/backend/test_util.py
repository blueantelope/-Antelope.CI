#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
test unit for util.py.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
import util

class UtilTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "util.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_load_ini(self):
        ini = util.load_ini(CONFIG_INI_PATH)
        ini_http = ini.options("http")
        print ini_http
        ini_http_ip = ini.get("http", "ip")
        print ini_http_ip

    def test_read_property(self):
        properties = util.read_property(ERROR_PROPERTY_PATH)
        print properties

if __name__ == "__main__":
    unittest.main()

