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

    def test_http(self):
        print("http server listening: %s/%d" % (ini.http.ip, ini.http.port))

    def test_https(self):
        print("https server listening: %s/%d" % (ini.https.ip, ini.https.port))
        print("keyfile is %s" % (ini.https.keyfile))
        print("certfile is %s" % (ini.https.certfile))

if __name__ == "__main__":
    unittest.main()

