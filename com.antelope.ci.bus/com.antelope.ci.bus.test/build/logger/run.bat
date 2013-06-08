@echo off
@echo deploy...
D:\programing\apache-ant-1.8.4\bin\ant -buildfile build.xml test.win
@echo on
@echo run test
cd test
java -jar bin/felix.jar