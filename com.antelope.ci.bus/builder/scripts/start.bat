@echo off  
SETLOCAL enabledelayedexpansion  
cd..  
set APP_HOME=%CD%
cd %APP_HOME%/bin 
set RUN_NAME="com.antelope.ci.bus"
set JAVA_OPTS="-Dfile.encoding=utf-8 -D$RUN_NAME -server"
;set JAVA_OPTS="-Dfile.encoding=utf-8 -D$RUN_NAME -server -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8765"

set CLSPATH=.
FOR %%c IN (%APP_HOME%\lib\*.jar) do (
	set CLSPATH=!CLSPATH!;%%c
)
FOR %%c IN (%APP_HOME%\lib\ext\*.jar) do (
	set CLSPATH=!CLSPATH!;%%c
)
java -cp %CLSPATH% com.antelope.ci.bus.CIBus -h %APP_HOME% -m app

@echo on
