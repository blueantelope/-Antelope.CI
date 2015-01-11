#!/bin/bash

#************************************************
# test script for @Antelope.CI WebServer
#------------------------------------------------
# blueantelope@gmail.com 2014-12-26
#************************************************


ROOT_DIR=$(cd "$(dirname "$0")"; pwd)
SRC_DIR=$ROOT_DIR/src/backend
LISTENING_IP=0.0.0.0
LISTENING_PORT=9430

start() {
  listening=$LISTENING_IP:$LISTENING_PORT
  if [ $# == 2 ]; then
    listenging=$2
  fi
  SERVER_NUM=$(ls $INS_DIR|wc -l)
  cd $SRC_DIR;python main.py start $listening &
}

stop() {
  cd $SRC_DIR;python main.py stop
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    stop
    start
    ;;
  *)
    echo "illegal parameters"
    ;;
esac

