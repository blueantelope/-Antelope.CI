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

ROOT_PATH = util.get_parent_dir()
# for backend
BACKEND_PATH = util.get_current_path()
MAIN = os.path.join(BACKEND_PATH, "main.pyc")
APP_LIST = (os.path.join(BACKEND_PATH, "app.pyo"),
        os.path.join(BACKEND_PATH, "app.pyc"),
        os.path.join(BACKEND_PATH, "app.py")
)
WATCHDOG_LIST = (os.path.join(BACKEND_PATH, "watchdog_app.pyo"),
        os.path.join(BACKEND_PATH, "watchdog_app.pyc"),
        os.path.join(BACKEND_PATH, "watchdog_app.py")
)
# for frontend
FRONTEND_DIR = os.path.join(util.get_parent_dir(), "frontend")
PAGE_DIR = os.path.join(FRONTEND_DIR, "page")
RESOURCE_DIR = os.path.join(FRONTEND_DIR, "resource")
# for configuration
CONFIG_INI_PATH = os.path.join(ROOT_PATH, "config.ini")
LOG_INI_PATH = os.path.join(ROOT_PATH, "log.ini")
WATCHDOG_PATH = os.path.join(ROOT_PATH, ".watchdog")
ETC_DIR = os.path.join(util.get_parent_dir(), "etc")
ERROR_PROP_PATH = os.path.join(ETC_DIR, "error.properties")
MESSAGE_INI_PATH = os.path.join(ETC_DIR, "message.ini")
KEY_PATH = os.path.join(ETC_DIR, "@antelope.ci.key")
CERT_PATH = os.path.join(ETC_DIR, "@antelope.ci.crt")

# define in configuration files
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

# for template
HEADER = "header.html"
FOOTER = "footer.html"
ANGULAR_JS = "/resource/js/angularjs/angular.js"
FONTAWESOME_CSS = "/resource/css/font-awesome.css"
COMMON_CSS = "/resource/css/common.css"
OUTER_CSS = "/resource/css/outer.css"


