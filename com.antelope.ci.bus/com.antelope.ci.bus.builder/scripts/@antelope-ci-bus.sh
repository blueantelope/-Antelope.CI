#!/bin/sh
######################################################
#		environment parameters
#		blueantelope	blueantelope@gmail.com
#		2015-03-30
######################################################


ROOT=$(cd "$(dirname "$0")"; cd ..; pwd)
RUN_NAME="com.antelope.ci.bus"
COMMON_OPTS="-Dfile.encoding=utf-8 -D$RUN_NAME -server"
REMOTE_PORT=8765
REMOTE_OPTS="$COMMON_OPTS -ea -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=$REMOTE_PORT"
MAIN_APP=com.antelope.ci.bus.CIBus
APP_MODE=app

for i in $ROOT/lib/*.jar ; do
    CLASSPATH=$CLASSPATH:$i
done
for i in $ROOT/system/lib/*.jar ; do
    CLASSPATH=$CLASSPATH:$i
done
export CLASSPATH

if [ "$1" = "start" ] ; then
	JAVA_OPTS=$COMMON_OPTS

	if [ -z "$2" ] ; then
		if [ "$2" = "remote" ] ; then
			JAVA_OPTS=$REMOTE_OPTS
		fi

	fi

	java $JAVA_OPTS $MAIN_APP -h $ROOT -m $APP_MODE 
fi

