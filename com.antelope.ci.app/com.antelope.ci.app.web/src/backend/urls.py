#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
url route to access webserver
--
blueantelope@gmail.com
blueantelope 2014-12-26
"""

from django.conf.urls import patterns, include, url
from django.contrib import admin

admin.autodiscover()

urlpatterns = patterns('',
    url(r'^$', 'action.index'),
)

