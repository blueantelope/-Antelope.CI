#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
Error information definition.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
import util

error_path = util.get_parent_dir() + "/error.properties"
errors = util.read_property(error_path)


# parse and objectify option: listening
class Arguments:
    tooshort = "arguments too short."
    illegal = "illegal argument."

    def __init__(self):
        self.tooshort = errors["arguments.tooshort"]
        self.illegal = errors["arguments.illegal"]

arguments = Arguments()

