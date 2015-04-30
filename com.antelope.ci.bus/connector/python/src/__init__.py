#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
initalization for api
--
blueantelope@gmail.com
blueantelope 2015-04-22
"""

import sys
reload(sys)
sys.setdefaultencoding("utf-8")
import os
from common import *
import logging
import logging.config

def init():
    global logger
    logging.basicConfig(level=logging.DEBUG,
            format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
            datefmt='%a, %d %b %Y %H:%M:%S',
            filemode='w')
    logger.debug("startup")

init()

