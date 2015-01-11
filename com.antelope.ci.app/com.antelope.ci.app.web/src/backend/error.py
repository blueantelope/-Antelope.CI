#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
Error information definition.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
from constant import *
import util

errors = util.read_property(ERROR_PROP_PATH)
# parse and objectify option: listening
class Arguments:
    tooshort = TOOSHORT
    illegal = ILLEGAL

    def __init__(self):
        _tooshort = errors["arguments.tooshort"]
        if ~util.str_isblank(_tooshort):
            self.tooshort = _tooshort
        _illegal = errors["arguments.illegal"]
        if ~util.str_isblank(_illegal):
            self.illegal = _illegal

arguments = Arguments()

