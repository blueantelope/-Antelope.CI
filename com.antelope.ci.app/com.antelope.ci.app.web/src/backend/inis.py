#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
configuraion for webserver.
form ini file.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

import utils

config_ini_path = utils.get_parent_dir() + "/config.ini"
config_ini = utils.load_ini(config_ini_path)

# options and items from config.ini
LISTENING = "listening"
LISTENING_IP = "ip"
LISTENING_PORT = "port"

# parse and objectify option: listening
class Listening:
    ip = "0.0.0.0"
    port = 9430

    def __init__(self):
        self.ip = utils.get_ini_property(config_ini, LISTENING, LISTENING_IP, self.ip)
        self.port = utils.get_ini_property(config_ini, LISTENING, LISTENING_PORT, self.port)


