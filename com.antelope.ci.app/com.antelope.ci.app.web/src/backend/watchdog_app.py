#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
monitor server status.
--
blueantelope@gmail.com
blueantelope 2015-01-15
"""

from __init__ import *
from watchdog import *

if __name__ == "__main__":
    watchdog = WatchDog()
    watchdog.watch()

