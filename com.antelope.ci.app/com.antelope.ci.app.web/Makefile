#************************************************
# makefile for @Antelope.CI WebServer
#------------------------------------------------
# blueantelope@gmail.com
# blueantelope 2014-12-22
#************************************************

ROOT_DIR =.
INCLUDE =$(ROOT_DIR)/include
SRC =$(ROOT_DIR)/src
BACKEND_SRC =$(SRC)/backend
FRONTEND_SRC =$(SRC)/frontend
ETC_SRC =$(SRC)/etc
CONFIG_INI =$(SRC)/config.ini
LOGGING_INI =$(SRC)/log.ini
LAUNCH =@antelope.ci.webservice
TARGET =$(ROOT_DIR)/antelope.ci.webserver
BIN =$(TARGET)/bin
ETC =$(TARGET)/etc
LIB =$(TARGET)/lib
PY =python
PY_ARGS =-m py_compile
DEPS =$(ROOT_DIR)/depends

.PHONY : clean

deploy: clean init compile 
	cp -rf $(BACKEND_SRC)/*.pyc $(LIB)
	$(RM) $(BACKEND_SRC)/*.pyc $(BACKEND_SRC)/*.pyo
	cp $(ETC_SRC)/* $(ETC)
	cp $(CONFIG_INI) $(TARGET)
	cp $(LOGGING_INI) $(TARGET)
	cp $(LAUNCH) $(TARGET)
	chmod +x $(TARGET)/$(LAUNCH)

depend:
	pip install -r $(DEPS)

compile:
	$(PY) $(PY_ARGS) $(BACKEND_SRC)/*.py

init:
	mkdir -p $(TARGET)
	mkdir -p $(BIN)
	mkdir -p $(ETC)
	mkdir -p $(LIB)

clean:
	$(RM) $(BACKEND_SRC)/*.pyc $(BACKEND_SRC)/*.pyo
	$(RM) -r $(TARGET)

