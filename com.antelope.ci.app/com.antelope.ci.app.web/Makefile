#************************************************
# makefile for @Antelope.CI WebServer
#------------------------------------------------
# blueantelope@gmail.com
# blueantelope 2014-12-22
#************************************************

ROOT_DIR =.
INCLUDE =$(ROOT_DIR)/include
SRC =$(ROOT_DIR)/src
TARGET =$(ROOT_DIR)/antelope.ci.webserver
BIN =$(TARGET)/bin
CONF =$(TARGET)/conf
LIB =$(TARGET)/lib

.PHONY : clean

install:
	pip install django
	mkdir -p $(TARGET)
	mkdir -p $(BIN)
	mkdir -p $(CONF)
	mkdir -p $(LIB)

clean:
	$(RM) -r $(TARGET)

