#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
http actions
--
blueantelope@gmail.com
blueantelope 2014-12-26
"""

from django.http import HttpRequest
from django.http import HttpResponse
from django.template import Context, loader

def index(request):
    """
    msg = "{'index': 'welcome'}"
    return HttpResponse(msg)
    """
    t = loader.get_template('home.html')
    return HttpResponse(t.render(Context()))

