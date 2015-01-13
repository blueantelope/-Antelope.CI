#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
test unit for error.py.
--
blueantelope@gmail.com
blueantelope 2015-01-10
"""

from __init__ import *
import error

class UtilsTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "error.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_argument(self):
        print("%s" % (error.arguments.tooshort))


if __name__ == "__main__":
    unittest.main()

