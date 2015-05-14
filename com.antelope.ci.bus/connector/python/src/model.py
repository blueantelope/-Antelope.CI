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

OC = {"user":0x01}

OT = {
    "ls" : 0x01,
    "add" : 0x02,
    "rm" : 0x03,
    "mod" : 0x04
}

BT = {
    "binary" : 0x01,
    "json" : 0x02
}

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

    def makeBody(self, body):
        self.body = body
        self.bl = len(self.body)

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
        message_fmt = "BBBHHBBI7B"
        fmt = (
            "%(order)s"
            "%(message_fmt)s"
        ) % {
            "order" : order,
            "message_fmt" : message_fmt
        }
        logger.debug("header format: %s" %(fmt))
        return fmt

    def tobytes(self):
        fmt_args = [self.endian, self.type, self.version, self.oid, self.oc, self.ot, self.bt, self.bl]
        fmt_args.extend(self.ext)
        fmt = self.format_header()
        if self.bl > 0:
            fmt += str(self.bl) + "s"
            fmt_args.append(self.body)
        logger.debug("format: %s" %(fmt))
        return pack(fmt, *fmt_args)

    def frombytes(self, bs):
        fmt = self.format_header(bs[0])
        size = len(bs)
        if size > 20:
            fmt += str(size-20) + "s"
        array = unpack(fmt, bs)
        logger.debug(array)
        self.endian, self.type, self.version, self.oid, self.oc, self.ot, self.bt, self.bl = array[0:8]
        self.ext = array[8:15]
        if size > 20:
            self.body = tuple(array[15])

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

    def ls(self):
        self._set("ls")

    def add(self):
        self._set("add")

    def rm(self):
        self._set("rm")

    def mod(self):
        self._set("mod")

    def _set(self, otkey):
        self.message.ot = OT[otkey]
        self._setBT("json")

    def _setBT(self, key):
        self.message.bt = BT[key]

    def _json(self, k, v):
        return ("\"%s\":\"%s\"" % (k, v))

    def id_json(self):
        return ("{%s, %s}" % (
                self._json("name", "id"),
                self._json("value", str(self.id))
            )
        )

    def toJson(self):
        return ("{%s, %s}" % (
                self._json("name", "id"),
                self._json("value", str(self.id))
            )
        )

    def serialize(self):
        self.message.makeBody(self.toJson())
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
        self.message.oc = OC["user"]

        if user.has_key("username"):
            self.username = user["username"]

    def username_json(self):
        return ("{%s, %s}" % (
                self._json("name", "username"),
                self._json("value", str(self.username))
            )
        )

    def toJson(self):
        return ("[%s, %s]" % (
                self.id_json(),
                self.username_json()
            )
        )

