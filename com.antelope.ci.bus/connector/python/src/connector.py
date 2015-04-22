#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
connector.
--
blueantelope@gmail.com
blueantelope 2015-04-22
"""

class SshClient(object):
    import paramiko, base64

    def __init__(self, **server):
        self.client = paramiko.SSHClient()
        self.client.connect(server.host, port=server.port, username=server.username, password=server.password)

    def send(self, data):
        if self.client is None:
            raise ApiError("ssh client can not connect.")
        else:
            stdin, stdout, stderr = client.exec_command(data)

    def close(self):
        if self.client is None:
            raise ApiError("ssh client can not connect.")
        else:
            self.client.close()

