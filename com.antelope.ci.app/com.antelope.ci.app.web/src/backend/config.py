#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
configuraion for webserver.
form ini file.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
import util
from constant import *

config_ini = util.load_ini(CONFIG_INI_PATH)
# parse and objectify option: listening
class Listening(object):
    switch = False
    ip = None
    port = 0

    def __init__(self, option, _port):
        switch_str = util.get_ini_value(config_ini, option, SWITCH_KEY, SWITCH)
        if util.str_equal(switch_str, "on"):
            self.switch = True
        self.ip = util.get_ini_value(config_ini, option, IP_KEY, LISTENING_IP)
        self.port = util.get_ini_value(config_ini, option, PORT_KEY, _port)

class HTTP(Listening):
    def __init__(self):
        super(HTTP, self).__init__(HTTP_KEY, HTTP_PORT)

class HTTPS(Listening):
    keyfile = KEY_PATH
    certfile = CERT_PATH
    def __init__(self, **sslinfo):
        super(HTTPS, self).__init__(HTTPS_KEY, HTTPS_PORT)
        if sslinfo.has_key("keyfile"):
            _keyfile = sslinfo["keyfile"]
            if ~util.str_isblank(_keyfile):
                keyfile = util.replace_curpath(_keyfile)
        if sslinfo.has_key("certfile"):
            _certfile = sslinfo["certfile"]
            if ~util.str_isblank(_certfile):
                certfile = util.replace_curpath(_certfile)

http = HTTP()
https = HTTPS()

