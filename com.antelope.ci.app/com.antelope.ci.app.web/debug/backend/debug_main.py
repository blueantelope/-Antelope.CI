#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
debug for main.py.
--
blueantelope@gmail.com
blueantelope 2015-01-13
"""

from __init__ import *
import main


def debug_main():
    pdb.set_trace()
    main.main()

def debug():
    debug_main()

debug()

