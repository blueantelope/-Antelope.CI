#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
default initialization.
--
blueantelope@gmail.com
blueantelope 2015-01-13
"""

import os
import sys
import error
import pdb

def get_src_dir():
    src_dir = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))) + "/src"
    return src_dir

SRC_DIR = get_src_dir()
def load_src_module():
    sys.path.insert(0, SRC_DIR + "/backend")

load_src_module()

