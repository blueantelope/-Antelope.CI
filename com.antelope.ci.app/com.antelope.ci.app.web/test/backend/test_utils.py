 #!/usr/bin/env python
 # -*- coding:utf-8 -*-

"""
test unit for utils.py.
--
blueantelope@gmail.com
blueantelope 2015-01-08
"""

from __init__ import *
from utils import *

class UtilsTestCase(unittest.TestCase):
    def setUp(self):
        print "Test Start"

    def tearDown(self):
        print "Test Finish"

    def test_load_ini(self):
        ini = load_ini(CONFIG_INI_PATH)
        ini_listening = ini.options("listening")
        print ini_listening
        ini_listening_ip = ini.get("listening", "ip")
        print ini_listening_ip

if __name__ == "__main__":
    unittest.main()

