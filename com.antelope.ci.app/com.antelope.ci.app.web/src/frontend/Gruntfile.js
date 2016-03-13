module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    uglify: {
      options: {
        banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      build: {
        src: 'src/<%= pkg.name %>.js',
        dest: 'build/<%= pkg.name %>.min.js'
      }
    },

    connect: {
      options: {
        port: 9400,
        open: true,
        livereload: 35729,
        hostname: '0.0.0.0'
      },
      livereload: {
        options: {
          open: false,
          middleware: function (connect) {
            return [
              connect.static('.tmp'),
              connect.static('public'),
              require('grunt-connect-proxy/lib/utils').proxyRequest
            ];
          }
        }
      },
    },
  });

  // Load the plugin that provides the "uglify" task.
  grunt.loadNpmTasks('grunt-contrib-uglify');

  // Default task(s).
  grunt.registerTask('default', ['uglify']);

  grunt.loadNpmTasks('grunt-connect');

  grunt.registerTask('serve', 'start the server and preview your app, --allow-remote for remote access', function (target) {
    grunt.task.run([
        'connect:livereload'
    ]);
  });

  grunt.registerTask('server', function (target) {
    grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
    grunt.task.run([target ? ('serve:' + target) : 'serve']);
  });

};
