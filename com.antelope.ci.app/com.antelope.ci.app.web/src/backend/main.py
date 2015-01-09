#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
main application. start and stop web server
--
blueantelope@gmail.com
blueantelope 2014-12-23
"""

from __init__ import *
import util
import ini
import error

class Feedback():
    result = False
    info = None

    def __init__(self, result=None, info=None):
        if result is not None:
            self.result = result
        if info is not None:
            self.info = info

pidname = ".pid"
pidfile = None
runtype = "restart"

def handle_arguments():
    feedback = Feedback(True)
    if len(sys.argv) < 2:
        feedback.result = False
        feedback.info = error.tooshort
    return feedback

def initial():
    global runtype, pidfile
    runtype = sys.argv[1]
    pidfile = os.path.join(util.get_parent_dir(), pidname)

def run():
    global runtype, pidfile
    if runtype == "start":
        fp = open(pidfile, "a")
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
            argv.append(listening.ip + ":" + str(listenging.port))
        start(argv)
    if runtype == "stop":
        if os.path.exists(pidfile):
            fp = open(pidfile, "r")
            ss = fp.readline()
            for s in ss.split(" "):
                try:
                    if len(s) > 0:
                        os.system("kill -9 " + s)
                except exception:
                    print("exception when kill " + s)
            fp.close()
            os.remove(pidfile)

def start(argv):
    os.environ.setdefault("DJANGO_SETTINGS_MODULE", "settings")
    from django.core.management import execute_from_command_line
    execute_from_command_line(argv)

def main():
    handler = handle_arguments()
    if handler.result == False:
        print handler.info
        sys.exit()
    initial()
    run()

if __name__ == "__main__":
    main()

