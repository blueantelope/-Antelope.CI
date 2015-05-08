#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
Test for Api.
--
blueantelope@gmail.com
blueantelope 2015-05-01
"""

from __init__ import *
from api import *

class ApiTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        logger = logging.getLogger("test_api")
        BaseTestCase.setUp(self, "api.py")
        self.api = Api(
                type = "ssh",
                hostname = "192.168.31.1",
                port = 9428,
                username = "blueantelope",
                password = "blueantelope")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_getUser(self):
        self.api.getUser(username="test")


if __name__ == "__main__":
    unittest.main()
