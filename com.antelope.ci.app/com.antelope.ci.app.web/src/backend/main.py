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
from message import help as message_help

# constants
FG = 0
BG = 1

# main function, entrance of the program
def main():
    feedback = argument_opt()
    if feedback.result == False:
        sys.stdout.write(handler.info)
        sys.exit(1)
    elif feedback.shutdown:
        shutdown()
    else:
        if feedback.watchdog:
            walk_watchdog()
        run_app(feedback)

def shutdown():
    from config import shutdown
    if shutdown.switch:
        os.system(shutdown.watchdog)
        os.system(shutdown.app)

def run_app(feedback):
    if feedback.mode == FG:
        # run in foreground
        run_in_fg()
    elif feedback.mode == BG:
        # run in background
        run_in_bg()

class ArgsFeedback(Feedback):
    mode = FG
    watchdog = False
    shutdown = False

def argument_opt():
    feedback = ArgsFeedback(True)
    usage = "usage: %prog [options] shutdown"
    parser = OptionParser(usage)
    option_list = (
            make_option("-m", "--mode", action="store", dest="mode", default="fg",
                type="choice", choices=["fg", "bg"], help=message_help.mode),
            make_option("-w", "--watchdog", action="store_true", dest="watchdog",
                help=message_help.watchdog),
    )
    for option in option_list:
        parser.add_option(option)
    (options, args) = parser.parse_args()
    if len(args) > 0:
        if util.str_equal(args[0], "shutdown"):
            feedback.shutdown = True
    else :
        if util.str_equal(options.mode, "bg"):
            feedback.mode = BG
        feedback.watchdog = (options.watchdog and feedback.mode == BG)
    return feedback

def walk_watchdog():
    from constant import WATCHDOG_LIST
    for WATCHDOG in WATCHDOG_LIST:
        if os.path.exists(WATCHDOG):
            break
    os.system("python " + WATCHDOG)

def run_in_fg():
    from app import application
    application()

def run_in_bg():
    import subprocess
    from constant import APP_LIST
    for APP in APP_LIST:
        if os.path.exists(APP):
            break
    os_type = util.get_os_type()
    creationflags = 0
    out_pipe = None
    if os_type == util.OS_WINDOWS:
        creationflags = 0x00000008
    if os_type == util.OS_LINUX or os_type == util.OS_MAC:
        out_pipe = open('/dev/null','w')
    return subprocess.Popen(["python", APP], creationflags=creationflags, stdout=out_pipe, stderr=subprocess.STDOUT)

if __name__ == "__main__":
    main()

