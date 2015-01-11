#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
http server with ssl.
--
blueantelope@gmail.com
blueantelope 2015-01-10
"""

from __init__ import *
import ssl
from datetime import datetime
from optparse import make_option
from argparse import ArgumentParser
from distutils.version import LooseVersion
from ini import https

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "settings")

import django
from django import get_version
from django.core.servers.basehttp import WSGIRequestHandler, WSGIServer
from django.core.management.commands import runserver
#from django.contrib.staticfiles.handlers import StaticFilesHandler
from django.conf import settings
from django.utils import translation
from django.core.management import ManagementUtility
from django.core.management.base import handle_default_options
from django.utils import six
from django.utils.encoding import get_system_encoding
from django.core.exceptions import ImproperlyConfigured


try:
    from django.core.servers.basehttp import WSGIServerException
except ImportError:
    from socket import error as WSGIServerException

if LooseVersion(get_version()) >= LooseVersion('1.5'):
    from django.utils._os import upath
else:
    upath = unicode


class SSLWSGIRequestHandler(WSGIRequestHandler):
    def get_environ(self):
        env = super(SSLWSGIRequestHandler, self).get_environ()
        env["HTTPS"] = "on"
        return env

class SSLHTTPServer(WSGIServer):
    def __init__(self, listening_ip, listening_port, key, cert):
        super(SSLHTTPServer, self).__init__((listening_ip, listening_port), SSLWSGIRequestHandler)
        self.socket = ssl.wrap_socket(self.socket, keyfile=key, certfile=cert, server_side=True,
                ssl_version=ssl.PROTOCOL_TLSv1, cert_reqs=ssl.CERT_NONE)

class SSLHTTPCommand(runserver.Command):
    option_list = runserver.Command.option_list + (
        make_option("--key", default=https.keyfile,
                    help="Path to the key file"),
        make_option("--certificate", default=https.certfile,
                    help="Path to the certificate"),
        make_option("--nostatic", dest='use_static_handler',
                    action='store_false', default=None),
        make_option("--static", dest='use_static_handler',
                    action='store_true')
    )

    def get_handler(self, *args, **options):
        handler = super(SSLHTTPCommand, self).get_handler(*args, **options)
        """
        insecure_serving = options.get('insecure_serving', False)
        use_static_handler = options.get('use_static_handler')
        if use_static_handler:
            return StaticFilesHandler(handler)
        elif 'django.contrib.staticfiles' in settings.INSTALLED_APPS:
            return StaticFilesHandler(handler)
        """
        return handler

    def inner_run(self, *args, **options):
        self.addr = https.ip
        self.port = https.port

        threading = options.get('use_threading')
        shutdown_message = options.get('shutdown_message', '')
        quit_command = 'CTRL-BREAK' if sys.platform == 'win32' else 'CONTROL-C'

        self.stdout.write("Performing system checks...\n\n")
        self.validate(display_num_errors=True)
        try:
            self.check_migrations()
        except ImproperlyConfigured:
            pass
        now = datetime.now().strftime('%B %d, %Y - %X')
        if six.PY2:
            now = now.decode(get_system_encoding())
        self.stdout.write((
            "%(started_at)s\n"
            "Django version %(version)s, using settings %(settings)r\n"
            "Starting development server at https://%(addr)s:%(port)s/\n"
            "Using Key file is %(keyfile)s\n"
            "Using certification file is %(certfile)s\n"
            "Quit the server with %(quit_command)s.\n"
        ) % {
            "started_at": now,
            "version": self.get_version(),
            "settings": settings.SETTINGS_MODULE,
            "addr": '[%s]' % self.addr if self._raw_ipv6 else self.addr,
            "port": self.port,
            "keyfile": https.keyfile,
            "certfile": https.certfile,
            "quit_command": quit_command,
        })
        # django.core.management.base forces the locale to en-us. We should
        # set it up correctly for the first request (particularly important
        # in the "--noreload" case).
        translation.activate(settings.LANGUAGE_CODE)

        try:
            ssl_handler = self.get_handler(*args, **options)
            ssl_server = SSLHTTPServer(https.ip, https.port, https.keyfile, https.certfile)
            ssl_server.set_app(ssl_handler)
            ssl_server.serve_forever()
        except WSGIServerException as e:
            # Use helpful error messages instead of ugly tracebacks.
            ERRORS = {
                errno.EACCES: "You don't have permission to access that port.",
                errno.EADDRINUSE: "That port is already in use.",
                errno.EADDRNOTAVAIL: "That IP address can't be assigned-to.",
            }
            try:
                error_text = ERRORS[e.errno]
            except KeyError:
                error_text = force_text(e)
            self.stderr.write("Error: %s" % error_text)
            # Need to use an OS exit because sys.exit doesn't work in a thread
            os._exit(1)
        except KeyboardInterrupt:
            if shutdown_message:
                self.stdout.write(shutdown_message)
            sys.exit(0)

class CommandParser(ArgumentParser):
    """
    Customized ArgumentParser class to improve some error messages and prevent
    SystemExit in several occasions, as SystemExit is unacceptable when a
    command is called programmatically.
    """
    def __init__(self, cmd, **kwargs):
        self.cmd = cmd
        super(CommandParser, self).__init__(**kwargs)

    def parse_args(self, args=None, namespace=None):
        # Catch missing argument for a better error message
        if (hasattr(self.cmd, 'missing_args_message') and
                not (args or any(not arg.startswith('-') for arg in args))):
            self.error(self.cmd.missing_args_message)
        return super(CommandParser, self).parse_args(args, namespace)

    def error(self, message):
        if self.cmd._called_from_command_line:
            super(CommandParser, self).error(message)
        else:
            raise CommandError("Error: %s" % message)

class SSLManagementUtility(ManagementUtility):
    def execute(self):
        """
        Given the command-line arguments, this figures out which subcommand is
        being run, creates a parser appropriate to that command, and runs it.
        """
        try:
            subcommand = self.argv[1]
        except IndexError:
            subcommand = 'help'  # Display help if no arguments were given.

        # Preprocess options to extract --settings and --pythonpath.
        # These options could affect the commands that are available, so they
        # must be processed early.
        parser = CommandParser(None, usage="%(prog)s subcommand [options] [args]", add_help=False)
        parser.add_argument('--settings')
        parser.add_argument('--pythonpath')
        parser.add_argument('args', nargs='*')  # catch-all
        try:
            options, args = parser.parse_known_args(self.argv[2:])
            handle_default_options(options)
        except CommandError:
            pass  # Ignore any option errors at this point.

        no_settings_commands = [
            'help', 'version', '--help', '--version', '-h',
            'compilemessages', 'makemessages',
            'startapp', 'startproject',
        ]

        try:
            settings.INSTALLED_APPS
        except ImproperlyConfigured as exc:
            self.settings_exception = exc
            # A handful of built-in management commands work without settings.
            # Load the default settings -- where INSTALLED_APPS is empty.
            if subcommand in no_settings_commands:
                settings.configure()

        if settings.configured:
            django.setup()

        self.autocomplete()

        if subcommand == 'help':
            if '--commands' in args:
                sys.stdout.write(self.main_help_text(commands_only=True) + '\n')
            elif len(options.args) < 1:
                sys.stdout.write(self.main_help_text() + '\n')
            else:
                self.fetch_command(options.args[0]).print_help(self.prog_name, options.args[0])
        # Special-cases: We want 'django-admin --version' and
        # 'django-admin --help' to work, for backwards compatibility.
        elif subcommand == 'version' or self.argv[1:] == ['--version']:
            sys.stdout.write(django.get_version() + '\n')
        elif self.argv[1:] in (['--help'], ['-h']):
            sys.stdout.write(self.main_help_text() + '\n')
        else:
            SSLHTTPCommand().run_from_argv(self.argv)

def run():
    if sys.argv is not None and len(sys.argv) > 0:
        argv = [sys.argv[0]]
    else:
        argv = [None]
    argv.append("runserver")
    ssl_utility = SSLManagementUtility(argv)
    ssl_utility.execute()

