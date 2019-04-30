let api

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/appConfigApi')
} else {
  api = require('./mock/appConfigApi')
}

export const fetchAppConfig = () => api.fetchAppConfig()
