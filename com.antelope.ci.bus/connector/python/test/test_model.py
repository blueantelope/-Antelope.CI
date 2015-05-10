#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
Test for Model.
--
blueantelope@gmail.com
blueantelope 2015-04-21
"""

from __init__ import *
from model import *

class ModelTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "model.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_user(self):
        """
        message = Message()
        message.bt = 1
        message.body = 2
        user = User(message=message, id=1)
        """
        user = User(username="test")
        print user.message
        print user.id
        bs = user.serialize()
        print "to bytes: " + bs
        user.deserialize(bs)
        print "from deserialize: " + str(user.message)

if __name__ == "__main__":
    unittest.main()

