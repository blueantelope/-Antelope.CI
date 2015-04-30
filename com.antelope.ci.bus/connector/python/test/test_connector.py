#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
Test for Connector.
--
blueantelope@gmail.com
blueantelope 2015-04-21
"""

from __init__ import *
from connector import *

class ModelTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        logger = logging.getLogger("test_connector")
        BaseTestCase.setUp(self, "connector.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_sshclient(self):
        sshClient = SshClient(host="localhost", port=22, username="root", password="root")
        sshClient.send("ls")


if __name__ == "__main__":
    unittest.main()
