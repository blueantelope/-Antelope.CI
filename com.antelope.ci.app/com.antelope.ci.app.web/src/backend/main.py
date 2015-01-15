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
from multiprocessing import Queue
import threading.Thread
import util
from app import Feedback
from app import application
from constant import APP_LIST
from message import help as message_help

FG = 0
BG = 1
MSG_QUEUE = Queue(5)

def main():
    feedback = argument_opt()
    if feedback.result == False:
        sys.stdout.write(handler.info)
        sys.exit(1)

    if feedback.daemon:
        make_daemon()

    run_app(feedback)

class Watchdog(threading.Thread):
    def __init__(self):
        super(Watchdog, self).__init__()

    def run(self):
        while True:
            if MSG_QUEUE.empty() > 0:
                msg = MSG_QUEUE.get()
                print "收到：" + msg
            time.sleep(16)

def run_app(feedback):
    for APP in APP_LIST:
        if os.path.exists(APP):
            break

    if feedback.mode == FG:
        # run in foreground
        pid = os.getpid()
        application()
    elif feedback.mode == BG:
        # run in background
        pid = run_in_bg(APP)


class ArgsFeedback(Feedback):
    mode = FG
    daemon = False

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

def fork_daemon_process(order):
    try:
        pid = os.fork()
        if pid > 0:
            sys.exit(0)
    except OSError, e:
        order_str = "first"
        if order == 2:
           order_str = "second"
        sys.stderr.write("fork daemon failed at " +  order_str)
        sys.exit(1)

def make_daemon():
    fork_daemon_process(1)
    os.chdir("/")
    os.setsid()
    os.umask(0)
    fork_daemon_process(2)

def run_in_bg(app):
    import subprocess
    os_type = util.get_os_type()
    creationflags = 0
    out_pipe = None
    if os_type == util.OS_WINDOWS:
        creationflags = 0x00000008
    if os_type == util.OS_LINUX or os_type == util.OS_MAC:
        out_pipe = open('/dev/null','w')
    return subprocess.Popen(["python", app], creationflags=creationflags, stdout=out_pipe, stderr=subprocess.STDOUT)

if __name__ == "__main__":
    main()

