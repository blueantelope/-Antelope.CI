#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
Model.
--
blueantelope@gmail.com
blueantelope 2015-04-21
"""

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
            "ext = %(ext)s}\n"
        ) %{
            "endian": self.endian,
            "type": self.type,
            "version": self.version,
            "oid": self.oid,
            "oc": self.oc,
            "ot": self.ot,
            "bt": self.bt,
            "bl": self.bl,
            "ext": self.ext
        }
        return s

class Model(object):
    def __init__(self, message):
        if message is not None:
            self.message = message

class User(Model):
    def __init__(self, **user):
        if user.has_key("message"):
            super(User, self).__init__(user["message"])

        if user.has_key("id"):
            self.id = user["id"]
        if user.has_key("username"):
            self.username = user["username"]

