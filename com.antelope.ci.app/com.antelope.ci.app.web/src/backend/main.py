#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
main program interface.
--
blueantelope@gmail.com
blueantelope 2014-12-23
"""

from __init__ import *
from optparse import OptionParser, make_option
import util
from app import Feedback
from app import application
from constant import APP
from message import help as message_help

FG = 0
BG = 1

def main():
    feedback = argument_opt()
    if feedback.result == False:
        sys.stdout.write(handler.info)
        sys.exit(1)
    if feedback.mode == FG:
        # run in foreground
        application()
    elif feedback.mode == BG:
        # run in background
        run_in_bg(APP)

class ArgsFeedback(Feedback):
    mode = FG

def argument_opt():
    feedback = ArgsFeedback(True)
    usage = "usage: %prog [options] arg1 arg2"
    parser = OptionParser(usage)
    option_list = (
            make_option("-m", "--mode", action="store", dest="mode", default="fg",
                type="choice", choices=["fg", "bg"], help=message_help.mode),
    )
    for option in option_list:
        parser.add_option(option)
    (options, args) = parser.parse_args()
    if util.str_equal(options.mode, "bg"):
        feedback.mode = BG
    return feedback

def run_in_bg(app):
    import subprocess
    os_type = util.get_os_type()
    creationflags = 0
    out_pipe = None
    if os_type == util.OS_WINDOWS:
        creationflags = 0x00000008
    if os_type == util.OS_LINUX or os_type == util.OS_MAC:
        out_pipe = open('/dev/null','w')
    subprocess.Popen(["python", app], creationflags=creationflags, stdout=out_pipe, stderr=subprocess.STDOUT)

if __name__ == "__main__":
    main()

