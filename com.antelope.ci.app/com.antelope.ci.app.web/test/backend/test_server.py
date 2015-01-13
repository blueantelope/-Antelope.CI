#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
test unit for server.py.
--
blueantelope@gmail.com
blueantelope 2015-01-10
"""

from __init__ import *
import server

class ServerTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "server.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_run(self):
        server.run()

if __name__ == "__main__":
    unittest.main()

