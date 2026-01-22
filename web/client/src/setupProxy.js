const { createProxyMiddleware } = require('http-proxy-middleware')

module.exports = function (app) {
  const url = 'http://localhost:8070/'

  const proxyOptions = {
    target: url,
    changeOrigin: false,
    logLevel: 'debug',
    secure: false,
  }

  app.use(createProxyMiddleware('/services', proxyOptions))
  app.use(createProxyMiddleware('/fake', proxyOptions))
  app.use(createProxyMiddleware('/api', proxyOptions))
  app.use(createProxyMiddleware('/maillink', proxyOptions))
  app.use(createProxyMiddleware('/public-api', proxyOptions))
  app.use(createProxyMiddleware('/logout', proxyOptions))
}
