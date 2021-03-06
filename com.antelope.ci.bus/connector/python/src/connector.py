#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
connector.
--
blueantelope@gmail.com
blueantelope 2015-04-22
"""
from __init__ import *
import paramiko, base64

logger = logging.getLogger("connector")

class SshClient(object):
    def __init__(self, **server):
        self.client = paramiko.SSHClient()
        self.client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        self.client.connect(
                hostname=server["hostname"],
                port=server["port"],
                username=server["username"],
                password=server["password"],
                look_for_keys=False)

    def send(self, data):
        if self.client is None:
            raise ApiError("ssh client can not connect.")
        else:
            stdin, stdout, stderr = self.client.exec_command(data)
            logger.debug(stdout.readlines())

    def close(self):
        if self.client is None:
            raise ApiError("ssh client can not connect.")
        else:
            self.client.close()

def createConnector(**server):
    if server.has_key("type"):
        type = server["type"]
    else:
        type = "ssh"

    if type == "ssh":
        return SshClient(**server)

    raise ApiError("unknown connector type: " + type)

