#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
http server with ssl.
--
blueantelope@gmail.com
blueantelope 2015-01-10
"""

from __init__ import *
import ini.https
from django.core.servers.basehttp import WSGIServer
from django.core.servers.basehttp import WSGIRequestHandler

class SSLWSGIRequestHandler(WSGIRequestHandler):
    def get_environ(self):
        env = super(SSLWSGIRequestHandler, self).get_environ()
        env["HTTPS"] = on
        return env

class SSLServer(WSGIServer):
    def __init__(self, listening_ip, listening_port, cert, key):
        super(SSLServer, self).__init__((listening_ip, listening_port,), SSLWSGIRequestHandler)
        self.socket = ssl.wrap_socket(self.socket, certfile=cert, keyfile=key, server_side=True, 
                ssl_version=ssl.PROTOCOL_TLSv1, cert_reqs=ssl.CERT_NONE)

        
def run():
    ssl_http_server = SSLHTTPServer(ini.https.ip, ini.https.port, certfile, keyfile)
    ssl_http_server.serve_forever()

