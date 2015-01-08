 #!/usr/bin/env python
 # -*- coding:utf-8 -*-

"""
test unit for inis.py.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
from inis import *

class InisTestCase(unittest.TestCase):
    def setUp(self):
        print "Test Start"

    def tearDown(self):
        print "Test Finish"

    def test_class_listening(self):
        listening = Listening()
        print("server listening: %s/%d" % (listening.ip, listening.port))

if __name__ == "__main__":
    unittest.main()

