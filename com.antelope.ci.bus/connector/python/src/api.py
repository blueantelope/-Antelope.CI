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

class Api(object):
    def __init__(self, **server):
        self.connector = createConnector(**server)

    def getUser(self, **user):
        _user = User(**user)
        self.connector.send(_user.serialize())

