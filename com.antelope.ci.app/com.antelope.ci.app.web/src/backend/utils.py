#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
utilities
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

import sys
import os

def get_parent_dir():
    return os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# load configuration from ini file.
def load_ini(ini_path):
    import ConfigParser
    ini = ConfigParser.ConfigParser()
    ini.read(ini_path)
    return ini

def get_ini_property(ini, option, key, default):
    value = None
    if isinstance(default, int):
        value = ini.getint(option, key)
    else:
        value = ini.get(option, key)
    if value is None:
        return default
    else:
        return value

