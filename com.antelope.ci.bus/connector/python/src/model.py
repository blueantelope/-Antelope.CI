#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
Model, data structure.
--
blueantelope@gmail.com
blueantelope 2015-04-21
"""

from __init__ import *
from struct import *

logger = logging.getLogger("model")

class Message(object):
    endian = 0x04
    type = 0x00
    version = 0x01
    oid = 0x00
    oc = 0x00
    ot = 0x00
    bt = 0x00
    bl = 0x00
    ext = (0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
    body = None

    def __init__(self, **message):
        if message.has_key("endian"):
            self.endian = message.endian
        if message.has_key("type"):
            self.type = message["type"]
        if message.has_key("version"):
            self.version = message["version"]
        if message.has_key("oid"):
            self.oid = message["oid"]
        if message.has_key("oc"):
            self.oc = message["oc"]
        if message.has_key("ot"):
            self.ot = message["ot"]
        if message.has_key("bt"):
            self.bt = message["bt"]
        if message.has_key("bl"):
            self.bl = message["bl"]
        if message.has_key("ext"):
            self.ext = message["ext"]
        if message.has_key("body"):
            self.body = message["body"]
            self.bl = len(self.body)

    def __str__(self):
        s = (
            "{endian = %(endian)s, "
            "type = %(type)s, "
            "version = %(version)s, "
            "oid = %(oid)s, "
            "oc = %(oc)s, "
            "ot = %(ot)s, "
            "bt = %(bt)s, "
            "bl = %(bl)s, "
            "ext = %(ext)s, "
            "body = %(body)s}"
        ) %{
            "endian": self.endian,
            "type": self.type,
            "version": self.version,
            "oid": self.oid,
            "oc": self.oc,
            "ot": self.ot,
            "bt": self.bt,
            "bl": self.bl,
            "ext": self.ext,
            "body": self.body
        }
        return s

    def format_header(self, _endian=None):
        order = "!"
        if _endian is None:
            _endian = self.endian
        if _endian == 0:
            order = "@"
        elif _endian == 1:
            order = "="
        elif _endian == 2:
            order = "<"
        elif _endian == 3:
            order = ">"
        message_fmt = "BBBHHBBI7s"
        fmt = (
            "%(order)s"
            "%(message_fmt)s"
        ) % {
            "order" : order,
            "message_fmt" : message_fmt
        }
        logger.debug(fmt)
        return fmt

    def tobytes(self):
        extstr = ''.join(str(e) for e in self.ext)
        fmt_args = [self.endian, self.type, self.version, self.oid, self.oc, self.ot, self.bt, self.bl, extstr]
        fmt = self.format_header()
        if self.bl > 0:
            fmt += str(self.bl) + "s"
        return pack(fmt, *fmt_args)

    def frombytes(self, bs):
        fmt = self.format_header(bs[0])
        size = len(bs)
        if size > 20:
            fmt += str(size-20) + "s"
        array = unpack(fmt, bs)
        logger.debug(array)
        self.endian, self.type, self.version, self.oid, self.oc, self.ot, self.bt, self.bl, extstr = array[0:9]
        self.ext = tuple(extstr)
        if size > 20:
            self.body = tuple(array[9])

class Model(object):
    def __init__(self, message=None, id=None):
        if message is not None:
            self.message = message
        else:
            self.message = Message()
        if id is not None:
            self.id = id
        else:
            self.id = 0

    def serialize(self):
        return self.message.tobytes()

    def deserialize(self, bs):
        return self.message.frombytes(bs)

class User(Model):
    def __init__(self, **user):
        _message = None
        _id = None
        if user.has_key("message"):
            _message = user["message"]
        if user.has_key("id"):
            _id = user["id"]
        super(User, self).__init__(_message, _id)

        if user.has_key("username"):
            self.username = user["username"]

