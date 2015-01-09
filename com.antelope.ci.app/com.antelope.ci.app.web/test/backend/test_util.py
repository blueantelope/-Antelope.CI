 #!/usr/bin/env python
 # -*- coding:utf-8 -*-

"""
test unit for util.py.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
import util

class UtilsTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "util.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_load_ini(self):
        ini = util.load_ini(CONFIG_INI_PATH)
        ini_listening = ini.options("listening")
        print ini_listening
        ini_listening_ip = ini.get("listening", "ip")
        print ini_listening_ip

    def test_read_property(self):
        properties = util.read_property(ERROR_PROPERTY_PATH)
        print properties

if __name__ == "__main__":
    unittest.main()

