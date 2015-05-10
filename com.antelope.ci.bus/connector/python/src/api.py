#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
api.
--
blueantelope@gmail.com
blueantelope 2015-04-21
"""

from __init__ import *
from model import *
from connector import *

logger = logging.getLogger("api")

OC = {"user":0x01}

class Api(object):
    def __init__(self, **server):
        self.connector = createConnector(**server)

    def getUser(self, **user):
        _user = User(**user)
        _user.ls()
        self.connector.send(_user.serialize())

