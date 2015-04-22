#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
common code.
--
blueantelope@gmail.com
blueantelope 2015-04-21
"""

class ApiError(Exception):
    def __init__(self, value):
        self.value = value
    def __str__(self):
        return repr(self.value)

