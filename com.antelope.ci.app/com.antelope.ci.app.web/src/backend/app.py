#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
application.
--
blueantelope@gmail.com
blueantelope 2014-12-23
"""

from __init__ import *
import threading.Thread
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
    if queue is not None:
        monitor(queue)
    start()

class MonitorThread(threading.Thread):
    def __init__(self, queue):
        super(MonitorThread, self).__init__()
        self.queue = queue

    def run(self):
        while True:
            queue.put("run")
            time.sleep(30)

def monitor(queue):
    monitor_thread = MonitorThread(queue)
    monitor_thread.setDaemon(True)
    monitor_thread.start()

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
    server.run()

if __name__ == "__main__":
    application()

