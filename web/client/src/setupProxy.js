const proxy = require('http-proxy-middleware');

module.exports = function(app) {
  const url = 'http://localhost:8070/';

  app.use(proxy('/services', { target: url, changeOrigin: false }));
  app.use(proxy('/fake', { target: url, changeOrigin: false }));
  app.use(proxy('/api', { target: url, changeOrigin: false }));
  app.use(proxy('/maillink', { target: url, changeOrigin: false }));
  app.use(proxy('/public-api', { target: url, changeOrigin: false }));
  app.use(proxy('/logout', { target: url, changeOrigin: false }));
};
