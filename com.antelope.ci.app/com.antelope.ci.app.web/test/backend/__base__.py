#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
base test unit.
--
blueantelope@gmail.com
blueantelope 2015-01-09
"""


class BaseTestCase():
    prefix = "==================** "
    suffix = " **=================="
    info = "test unit"

    def setUp(self, info=None):
        if info is not None:
            self.info = info
        print "\n" + self.prefix + self.info + " test case start" + self.suffix

    def tearDown(self):
        print self.prefix + self.info + " test case finish" + self.suffix

