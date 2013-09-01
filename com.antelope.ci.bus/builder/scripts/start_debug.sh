#!/bin/sh
######################################################
#		shell execute for com.antelope.ci.bus
#		blueantelope	blueantelope@gmail.com
#		2013-08-18
######################################################

PARENT=".."
RUN_NAME="com.antelope.ci.bus"
JAVA_OPTS="-Dfile.encoding=utf-8 -D$RUN_NAME -server -ea"
#JAVA_OPTS="-Dfile.encoding=utf-8 -D$RUN_NAME -server -ea -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8765"

for i in $PARENT/lib/*.jar ; do
    CLASSPATH=$CLASSPATH:$i
done
for i in $PARENT/lib/ext/*.jar ; do
    CLASSPATH=$CLASSPATH:$i
done
export CLASSPATH

java com.antelope.ci.bus.CIBus -h $PARENT -m dev
