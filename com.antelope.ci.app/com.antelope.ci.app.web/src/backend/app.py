#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
application.
--
blueantelope@gmail.com
blueantelope 2014-12-23
"""

from __init__ import *
import ini
import util
import constant
import server
import signal

def application():
    feedback = argument_opt()
    if feedback.result == False:
        sys.stdout.write(handler.info)
        sys.exit(1)
    initial()
    signal_opt()
    start()

class Feedback():
    def __init__(self, result=None, info=None):
        if result is not None:
            self.result = result
        if info is not None:
            self.info = info

def argument_opt():
    feedback = Feedback(True)
    return feedback

def initial():
    global logger
    logging.config.fileConfig(constant.LOGGING_INI_PATH)
    logger = logging.getLogger("main.app")
    logger.debug("startup")

def signal_handler(signum, frame):
    if signum  == signal.SIGINT:
        logger.warn("CONTROL-C pressed, server exit!")
        sys.exit(0)

def signal_opt():
    signal.signal(signal.SIGINT, signal_handler)

def start():
    fp = open(constant.PID_PATH, "a")
    fp.write(str(os.getpid()) + " ")
    fp.close()
    server.run()

if __name__ == "__main__":
    application()

