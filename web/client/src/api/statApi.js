let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/statApi');
} else {
  api = require('./mock/statApi')
}

export const fetchStat = () => api.fetchStat();

