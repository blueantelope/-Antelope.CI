 #!/usr/bin/env python
 # -*- coding:utf-8 -*-

"""
default initialization.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

import os
import sys
import unittest
from __base__ import *

def get_src_dir():
    src_dir = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))) + "/src"
    return src_dir

SRC_DIR = get_src_dir()
CONFIG_INI_PATH = SRC_DIR + "/config.ini"
ERROR_PROPERTY_PATH = SRC_DIR + "/error.properties"

def load_src_module():
    sys.path.insert(0, SRC_DIR + "/backend")

load_src_module()

