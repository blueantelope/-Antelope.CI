 #!/usr/bin/env python
 # -*- coding:utf-8 -*-

"""
test unit for ini.py.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
import ini

class InisTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "ini.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_class_listening(self):
        listening = ini.Listening()
        print("server listening: %s/%d" % (listening.ip, listening.port))

if __name__ == "__main__":
    unittest.main()

