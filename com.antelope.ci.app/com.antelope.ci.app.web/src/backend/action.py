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

def index(request):
    msg = 'welcome!</br>'
    return HttpResponse(msg)

