#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""

--
blueantelope@gmail.com
blueantelope 2015-04-21
"""
import sys
reload(sys)
import os
import unittest
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))) + "/src")

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

import logging
import logging.config

def init():
    global logger
    logging.basicConfig(level=logging.DEBUG,
            format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
            datefmt='%a, %d %b %Y %H:%M:%S',
            filemode='w')

init()

