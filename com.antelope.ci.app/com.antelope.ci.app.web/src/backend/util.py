#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
utilities
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *

def get_parent_dir():
    return os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# load configuration from ini file.
def load_ini(ini_path):
    import ConfigParser
    ini = ConfigParser.ConfigParser()
    ini.read(ini_path)
    return ini

def get_ini_value(ini, option, key, default):
    value = None
    if isinstance(default, int):
        value = ini.getint(option, key)
    else:
        value = ini.get(option, key)
    if value is None:
        return default
    else:
        return value

def read_property(property_path):
    property_file = open(property_path, "rU" )
    properties = dict()
    for line in property_file:
        item = line.strip()
        if len(item) == 0:
            continue
        if item[0] in ('#', '!'):
            continue
        delimiter = [item.find(c) for c in '='] + [len(item)]
        position = min([pos for pos in delimiter if pos != -1])
        key = item[:position].rstrip()
        value = item[position:].lstrip('=').rstrip()
        properties[key] = value

    property_file.close()
    return properties



