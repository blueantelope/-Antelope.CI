#!/bin/bash

#************************************************
# test script for @Antelope.CI WebServer
#------------------------------------------------
# blueantelope@gmail.com 2014-12-26
#************************************************


ROOT_DIR=$(cd "$(dirname ../"$0")"; pwd)
SRC_DIR=$ROOT_DIR/src
LISTENING_IP=0.0.0.0
LISTENING_PORT=9430

test_start() {
  listening=$LISTENING_IP:$LISTENING_PORT
  if [ $# == 2 ]; then
    listenging=$2
  fi
  SERVER_NUM=$(ls $INS_DIR|wc -l)
  cd $SRC_DIR;python main.py start $listening &
}

test_stop() {
  cd $SRC_DIR;python main.py stop
}

case "$1" in
  test_start)
    test_start
  ;;
  test_stop)
    test_stop
  ;;
  test_restart)
    test_stop
    test_start
  ;;
  *)
    echo "illegal parameters"
  ;;
esac

