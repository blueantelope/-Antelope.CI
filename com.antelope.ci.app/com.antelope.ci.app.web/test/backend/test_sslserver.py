 #!/usr/bin/env python
 # -*- coding:utf-8 -*-

"""
test unit for sslserver.py.
--
blueantelope@gmail.com
blueantelope 2015-01-10
"""

from __init__ import *
import sslserver

class SSLServerTestCase(unittest.TestCase, BaseTestCase):
    def setUp(self):
        BaseTestCase.setUp(self, "sslserver.py")

    def tearDown(self):
        BaseTestCase.tearDown(self)

    def test_run(self):
        sslserver.run()

if __name__ == "__main__":
    unittest.main()

