#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
message define file.
form ini file.
--
blueantelope@gmail.com
blueantelope 2015-01-14
"""

from __init__ import *
import util
from constant import *

message_ini = util.load_ini(MESSAGE_INI_PATH)

class HELP(object):
    mode = "Run mode; fg=foregound, bg=background"
    watchdog = "Open to check health, produce a daemon process as monitor"

    def __init__(self):
        self.mode = util.get_ini_value(message_ini, "help", "mode", self.mode)
        self.watchdog = util.get_ini_value(message_ini, "help", "watchdog", self.watchdog)

help = HELP()

