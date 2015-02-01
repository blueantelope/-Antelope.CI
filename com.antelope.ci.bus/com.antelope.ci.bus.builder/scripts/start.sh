#!/bin/sh
######################################################
# shell execute for com.antelope.ci.bus
# blueantelope@gmail.com
# blueantelope 2013-08-18
######################################################

ROOT=$(cd "$(dirname "$0")"; cd ..; pwd)
RUN_NAME="com.antelope.ci.bus"
JAVA_OPTS="-Dfile.encoding=utf-8 -D$RUN_NAME -server"
#JAVA_OPTS="-Dfile.encoding=utf-8 -D$RUN_NAME -server -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8765"

for i in $ROOT/lib/*.jar ; do
    CLASSPATH=$CLASSPATH:$i
done
for i in $ROOT/system/lib/*.jar ; do
    CLASSPATH=$CLASSPATH:$i
done
export CLASSPATH

java com.antelope.ci.bus.CIBus -h $ROOT -m app
