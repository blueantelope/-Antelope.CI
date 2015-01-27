#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
http actions
--
blueantelope@gmail.com
blueantelope 2014-12-26
"""

from __init__ import *
from rest_framework import generics
from django.http import HttpRequest
from django.http import HttpResponse
from django.template import Context, loader

logger = logging.getLogger("main.action")

base_context = Context({"header":constant.HEADER, "footer":constant.FOOTER, "angularjs":constant.ANGULARJS})

def index(request):
    """
    msg = "{'index': 'welcome'}"
    return HttpResponse(msg)
    """
    logger.debug("enter Action.index")
    t = loader.get_template('home.html')
    c = base_context
    return HttpResponse(t.render(c))

def login(request):
    t = loader.get_template('login.html')
    c = base_context
    return HttpResponse(t.render(c))


