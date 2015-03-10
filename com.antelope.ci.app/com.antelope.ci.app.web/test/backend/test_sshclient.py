#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
test unit ssh client with paramiko.
--
blueantelope@gmail.com
blueantelope 2015-03-10
"""

from __init__ import *
import paramiko, base64

class SshClentTestCase(unittest.TestCase, BaseTestCase):
    def test_run(self):
#        key = paramiko.RSAKey(data=base64.decodestring('AAA...'))
        client = paramiko.SSHClient()
#        client.get_host_keys().add('192.168.31.1', 'ssh-rsa', key)
        client.connect('192.168.31.1', username='blueantelope', password='blueantelope', port=9428)
        stdin, stdout, stderr = client.exec_command('ls')
        client.close()

if __name__ == "__main__":
    unittest.main()

