#!/usr/bin/env python
#encoding: utf8

"""
main application. start and stop web server 
--
blueantelope@gmail.com
blueantelope 2014-12-23
"""
import os
import sys

LISTENING = {
	"IP" : "0.0.0.0",
	"PORT" : 9090
}
pidname = ".pid"

def main():
	global pidname
	command = sys.argv[1]
	curdir = os.getcwd()
	pidfile = os.path.join(curdir, pidname)
	if command == "start":
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
			argv.append(LISTENING["IP"] + ":" + str(LISTENING["PORT"]))
		run(argv)
	if command == "stop":
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

def run(argv):
	os.environ.setdefault("DJANGO_SETTINGS_MODULE", "settings")
	from django.core.management import execute_from_command_line
	execute_from_command_line(argv)

if __name__ == "__main__":
	main()

