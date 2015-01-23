#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
web server, includes http and https.
--
blueantelope@gmail.com
blueantelope 2015-01-10
"""

from __init__ import *
import socket
import ssl
import threading
from datetime import datetime
from optparse import make_option
import config

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'settings')
import django
from django import get_version
from django.core.servers.basehttp import WSGIRequestHandler, WSGIServer, run as basehttp_run
from django.core.management.commands import runserver
from django.conf import settings
from django.utils import translation, six
from django.core.management import LaxOptionParser, ManagementUtility
from django.core.management.base import handle_default_options
from django.utils.encoding import get_system_encoding
from django.core.exceptions import ImproperlyConfigured

logger = logging.getLogger("main.server")

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

server_checked = False
server_check_locker = threading.Lock()
class ServerCommand(runserver.Command):
    def set_server(self):
        raise NotImplementedError("subclasses of ServerCommand must provide a set_server() method")

    def start_service(self, *args, **options):
        raise NotImplementedError("subclasses of ServerCommand must provide a start_service() method")

    def check_server(self, *args, **options):
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
            "Quit the server with %(quit_command)s.\n"
        ) % {
            "started_at": now,
            "version": self.get_version(),
            "settings": settings.SETTINGS_MODULE,
            "quit_command": quit_command,
        })
        # django.core.management.base forces the locale to en-us. We should
        # set it up correctly for the first request (particularly important
        # in the "--noreload" case).
        translation.activate(settings.LANGUAGE_CODE)

    def run(self, *args, **options):
        self.inner_run(*args, **options)

    def inner_run(self, *args, **options):
        self.set_server()
        if server_check_locker.acquire():
            global server_checked
            logger.debug('检查：' + str(server_checked))
            if not server_checked:
                self.check_server(args, options)
                server_checked = True
            server_check_locker.release()
        self.run_server(args, options)

    def run_server(self, *args, **options):
        shutdown_message = options.get('shutdown_message', '')
        self.stdout.write(self.server_info)
        try:
            if self.switch:
                self.start_service(*args, **options)
        except socket.error as e:
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

class HTTPServerCommand(ServerCommand):
    def set_server(self):
        self.switch = config.http.switch
        self.addr = config.http.ip
        self.port = config.http.port
        self.server_info = (
            "Starting http server at http://%(addr)s:%(port)s/\n"
        ) % {
            "addr": '[%s]' % self.addr if self._raw_ipv6 else self.addr,
            "port": self.port,
        }

    def start_service(self, *args, **options):
        handler = self.get_handler(*args, **options)
        threading = options.get('use_threading')
        basehttp_run(self.addr, self.port, handler,
                ipv6=self.use_ipv6, threading=threading)

class SSLHTTPServerCommand(ServerCommand):
    option_list = runserver.Command.option_list + (
        make_option("--key", default=config.https.keyfile,
                    help="Path to the key file"),
        make_option("--certificate", default=config.https.certfile,
                    help="Path to the certificate"),
        make_option("--nostatic", dest='use_static_handler',
                    action='store_false', default=None),
        make_option("--static", dest='use_static_handler',
                    action='store_true')
    )

    def get_handler(self, *args, **options):
        """
        Returns the static files serving handler wrapping the default handler,
        if static files should be served. Otherwise just returns the default
        handler.
        """
        from django.contrib.staticfiles.handlers import StaticFilesHandler
        handler = super(SSLHTTPServerCommand, self).get_handler(*args, **options)
        insecure_serving = options.get('insecure_serving', False)
        if self.should_use_static_handler(options):
            return StaticFilesHandler(handler)
        return handler

    def should_use_static_handler(self, options):
        # it's a bit weird to import settings in the middle of the method, but
        # this is what inner_run does
        use_static_handler = options.get('use_static_handler')
        if use_static_handler:
            return True
        if (use_static_handler is None and
            'django.contrib.staticfiles' in settings.INSTALLED_APPS):
            return True
        return False

    def set_server(self):
        self.switch = config.https.switch
        self.addr = config.https.ip
        self.port = config.https.port
        self.keyfile = config.https.keyfile
        self.certfile = config.https.keyfile
        self.server_info = (
            "Starting https server at https://%(addr)s:%(port)s/\n"
            "Using Key file is %(keyfile)s\n"
            "Using certification file is %(certfile)s\n"
        ) % {
            "addr": '[%s]' % self.addr if self._raw_ipv6 else self.addr,
            "port": self.port,
            "keyfile": config.https.keyfile,
            "certfile": config.https.certfile,
        }

    def start_service(self, *args, **options):
        handler = self.get_handler(*args, **options)
        httpsd = SSLHTTPServer(config.https.ip, config.https.port, config.https.keyfile, config.https.certfile)
        httpsd.set_app(handler)
        httpsd.serve_forever()

class ServerThread(threading.Thread):
    def __init__(self, command, argv):
        super(ServerThread, self).__init__()
        self.command = command
        self.argv = argv

    def run(self):
        self.command.run_from_argv(self.argv)

class HTTPServerThread(ServerThread):
    def __init__(self, argv):
        super(HTTPServerThread, self).__init__(HTTPServerCommand(), argv)

class SSLHTTPServerThread(ServerThread):
    def __init__(self, argv):
        super(SSLHTTPServerThread, self).__init__(SSLHTTPServerCommand(), argv)

class ServerManagementUtility(ManagementUtility):
    def execute(self):
        """
        Given the command-line arguments, this figures out which subcommand is
        being run, creates a parser appropriate to that command, and runs it.
        """
        # Preprocess options to extract --settings and --pythonpath.
        # These options could affect the commands that are available, so they
        # must be processed early.
        parser = LaxOptionParser(usage="%prog subcommand [options] [args]",
                                 version=get_version(),
                                 option_list=ServerCommand.option_list)
        try:
            options, args = parser.parse_args(self.argv)
            handle_default_options(options)
        except:  # Needed because parser.parse_args can raise SystemExit
            pass  # Ignore any option errors at this point.

        try:
            subcommand = self.argv[1]
        except IndexError:
            subcommand = 'help'  # Display help if no arguments were given.

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
            if len(args) <= 2:
                parser.print_lax_help()
                sys.stdout.write(self.main_help_text() + '\n')
            elif args[2] == '--commands':
                sys.stdout.write(self.main_help_text(commands_only=True) + '\n')
            else:
                self.fetch_command(args[2]).print_help(self.prog_name, args[2])
        elif subcommand == 'version':
            sys.stdout.write(parser.get_version() + '\n')
        # Special-cases: We want 'django-admin.py --version' and
        # 'django-admin.py --help' to work, for backwards compatibility.
        elif self.argv[1:] == ['--version']:
            # LaxOptionParser already takes care of printing the version.
            pass
        elif self.argv[1:] in (['--help'], ['-h']):
            parser.print_lax_help()
            sys.stdout.write(self.main_help_text() + '\n')
        else:
            self.start_server()

    def start_server(self):
        if config.http.switch and config.https.switch:
            httpd_thread = HTTPServerThread(self.argv)
            httpd_thread.setDaemon(True)
            httpd_thread.start()
            time.sleep(2)
            httpsd_thread = SSLHTTPServerThread(self.argv)
            httpsd_thread.setDaemon(True)
            httpsd_thread.start()
            while 1:
                alive = False
                alive = alive or httpd_thread.isAlive() or httpsd_thread.isAlive()
                if not alive:
                    break
        elif config.http.switch:
            HTTPServerCommand().run_from_argv(self.argv)
        elif config.https.switch:
            SSLHTTPServerCommand().run_from_argv(self.argv)

def run():
    logger.debug("server startup...")
    if sys.argv is not None and len(sys.argv) > 0:
        argv = [sys.argv[0]]
    else:
        argv = [constant.MAIN]
    argv.append("@antelope.ci web server")
    utility = ServerManagementUtility(argv)
    utility.execute()

