#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
constant definition.
--
blueantelope@gmail.com
blueantelope 2015-01-10
"""

from __init__ import *
import util

BACKEND_PATH = util.get_current_path()
MAIN = os.path.join(BACKEND_PATH, "main.pyc")
APP_LIST = (os.path.join(BACKEND_PATH, "app.pyo"),
        os.path.join(BACKEND_PATH, "app.pyc"),
        os.path.join(BACKEND_PATH, "app.py")
)
WATCHDOG_LIST = (os.path.join(BACKEND_PATH, "watchdog.pyo"),
        os.path.join(BACKEND_PATH, "watchdog.pyc"),
        os.path.join(BACKEND_PATH, "watchdog.py")
)
ROOT_PATH = util.get_parent_dir()
CONFIG_INI_PATH = os.path.join(ROOT_PATH, "config.ini")
LOGGING_INI_PATH = os.path.join(ROOT_PATH, "logging.ini")
WATCHDOG_PATH = os.path.join(ROOT_PATH, ".watchdog")
ETC_DIR = os.path.join(util.get_parent_dir(), "etc")
ERROR_PROP_PATH = os.path.join(ETC_DIR, "error.properties")
MESSAGE_INI_PATH = os.path.join(ETC_DIR, "message.ini")
KEY_PATH = os.path.join(ETC_DIR, "@antelope.ci.key")
CERT_PATH = os.path.join(ETC_DIR, "@antelope.ci.crt")

SWITCH_KEY = "switch"
HTTP_KEY = "http"
HTTPS_KEY = "https"
IP_KEY = "ip"
PORT_KEY = "port"
KEYFILE_KEY = "keyfile"
CERTFILE_KEY = "certfile"
SWITCH = "off"
LISTENING_IP = "0.0.0.0"
HTTP_PORT = 80
HTTPS_PORT = 443

TOOSHORT = "arguments too short."
ILLEGAL = "illegal argument."

