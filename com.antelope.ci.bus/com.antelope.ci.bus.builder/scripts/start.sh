#!/bin/sh
######################################################
# shell execute for com.antelope.ci.bus
# blueantelope@gmail.com
# blueantelope 2013-08-18
######################################################

PRGDIR=$(dirname "$PRG")
EXECUTABLE="@antelope-ci-bus.sh"

"$PRGDIR"/"$EXECUTABLE" start "$@"
