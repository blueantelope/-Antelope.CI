#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
main application. start and stop web server
--
blueantelope@gmail.com
blueantelope 2014-12-23
"""

from __init__ import *
import ini
import util
import constant
import server

class Feedback():
    result = False
    info = None

    def __init__(self, result=None, info=None):
        if result is not None:
            self.result = result
        if info is not None:
            self.info = info

def handle_arguments():
    feedback = Feedback(True)
    if len(sys.argv) < 2:
        feedback.result = False
        feedback.info = error.tooshort
    return feedback

def initial():
    global runtype, logger
    runtype = sys.argv[1]
    logging.config.fileConfig(constant.LOGGING_INI_PATH)
    logger = logging.getLogger("main")
    logger.debug("startup")

def run():
    global runtype
    if runtype == "start":
        fp = open(constant.PID_PATH, "a")
        fp.write(str(os.getpid()) + " ")
        fp.close()
        argv = []
        n = 0
        for a in sys.argv:
            if n == 1:
                argv.append("runserver")
            else:
                argv.append(a)
            n = n + 1
        if len(sys.argv) < 3:
            argv.append(ini.http.ip + ":" + str(ini.http.port))
        start(argv)
    if runtype == "stop":
        if os.path.exists(constant.PID_PATH):
            fp = open(constant.PID_PATH, "r")
            ss = fp.readline()
            for s in ss.split(" "):
                try:
                    if len(s) > 0:
                        os.system("kill -9 " + s)
                except exception:
                    print("exception when kill " + s)
            fp.close()
            os.remove(constant.PID_PATH)

def start(argv):
    server.run()

def main():
    handler = handle_arguments()
    if handler.result == False:
        print handler.info
        sys.exit()
    initial()
    run()

if __name__ == "__main__":
    main()

