#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
utilities
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *

VAR_PREFIX = "{@"
VAR_SUFFIX = "}"

def get_current_path():
    return os.path.dirname(os.path.abspath(__file__))

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

def str_equal(str1, str2):
    if str1 is None and str2 is None:
        return True
    if (str1 is not None
            and str2 is not None
            and str1.lower().strip() == str2.lower().strip()):
        return True
    return False

def str_isblank(str):
    if str is not None and len(str.strip()) > 0:
        return False
    return True

def gen_inter_var(str, var):
    return str.replace(VAR_PREFIX+var+VAR_SUFFIX, str)

def replace_curpath(str):
    return gen_inter_var("curpath", str)

OS_LINUX = 1
OS_MAC = 2
OS_WINDOWS = 3
OS_OTHERS = -1
def get_os_type():
    from sys import platform as _platform
    if _platform == "linux" or _platform == "linux2":
        return OS_LINUX
    elif _platform == "darwin":
        return OS_MAC
    elif _platform == "win32":
        return OS_WINDOWS
    else:
        return OS_OTHERS

